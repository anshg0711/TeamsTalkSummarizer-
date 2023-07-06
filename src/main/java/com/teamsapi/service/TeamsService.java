package com.teamsapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsapi.custom_annotation.annotation.MethodExecutionTime;
import com.teamsapi.entity.teamsapi.Channel;
import com.teamsapi.entity.teamsapi.channelresponse.ChannelResponseBase;
import com.teamsapi.entity.teamsapi.channelresponse.Val;
import com.teamsapi.entity.teamsapi.Message;
import com.teamsapi.entity.teamsapi.messageresponse.MessageResponseBase;
import com.teamsapi.entity.teamsapi.messageresponse.Vall;
import com.teamsapi.respository.TeamsChannelRepository;
import com.teamsapi.respository.TeamsMessageRepository;
import com.teamsapi.utility.CONSTANT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TeamsService {
    private final TeamsMessageRepository teamsMessageRepository;
    private final TeamsChannelRepository teamsChannelRepository;
    private final ChatGptService chatGptService;
    private final String token;
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    public TeamsService(TeamsMessageRepository teamsMessageRepository, TeamsChannelRepository teamsChannelRepository, ChatGptService chatGptService, @Value("${team.bearer.token}") String token) {
        this.teamsMessageRepository = teamsMessageRepository;
        this.teamsChannelRepository = teamsChannelRepository;
        this.chatGptService = chatGptService;
        this.token = token;
    }


    public List<Message> messagesInChannel(String teamId, String channelId, String top) {
        String url = CONSTANT.TEAMS_API_ENDPOINT + teamId + CONSTANT.CHANNELS + channelId + CONSTANT.MESSAGESTOP + top;
        String response = responseDataReceived(url);
        MessageResponseBase messages = mappingResponseWithMessageResponseBase(response);
        return mappingMessageResponseBaseWithMessage(messages);
    }

    public List<Message> repliesOnMessage(String teamId, String channelId, String messageId, String top) {
        String url = CONSTANT.TEAMS_API_ENDPOINT + teamId + CONSTANT.CHANNELS + channelId + CONSTANT.MESSAGES + messageId + CONSTANT.REPLIESTOP + top;
        String response = responseDataReceived(url);
        MessageResponseBase messages = mappingResponseWithMessageResponseBase(response);
        List<Message> messageData = mappingMessageResponseBaseWithMessage(messages);
        Collections.reverse(messageData);
        return messageData;
    }
    public Message getMessageById(String teamId, String channelId, String messageId) {

        String url = CONSTANT.TEAMS_API_ENDPOINT + teamId + CONSTANT.CHANNELS + channelId + CONSTANT.MESSAGES + messageId;
        String response = responseDataReceived(url);
        Vall vall = mappingResponseWithVall(response);
        return mappingVallWithMessage(vall);
    }

    public List<Channel> channelNames(String teamId) {
        String url = CONSTANT.TEAMS_API_ENDPOINT + teamId + CONSTANT.ALLCHANNELS;
        String response = responseDataReceived(url);
        ChannelResponseBase channel = mappingResponseWithChannelResponseBase(response);
        return mappingChannelResponseBaseWithChannel(channel);
    }

    public String summarizeByMessageLink(String messageLink) {
        if (!messageLink.startsWith("http://") && !messageLink.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid link: " + messageLink + ". Link should start with 'http://' or 'https://'.");
        }
        try {
            URL url = new URL(messageLink);
            String query = url.getQuery();
            String teamId = null;
            String channelId = null;
            String messageId = null;
            String[] params = query.split(CONSTANT.AND);
            for (String param : params) {
                String[] keyValue = param.split(CONSTANT.EQUAL);
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];

                    if (key.equals("groupId")) {
                        teamId = value;
                    } else if (key.equals("parentMessageId")) {
                        messageId = value;
                    }
                }
            }
            String path = url.getPath();
            Pattern pattern = Pattern.compile("/([A-Za-z0-9@:]+@thread\\.tacv2)/");
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                channelId = matcher.group(1);
            }

            return summarizeReplies(teamId, channelId, messageId);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid link: " + messageLink, e);
        }

    }

    public String summarizeReplies(String teamId, String channelId, String messageId) {
        String question = concatenateReplies(teamId, channelId, messageId);
        return this.chatGptService.answerToPromptOfChatGpt(question);
    }

    private String concatenateReplies(String teamId, String channelId, String messageId) {
        StringBuilder questionBuilder = new StringBuilder(CONSTANT.SUMMARIZE);
        Message message = getMessageById(teamId, channelId, messageId);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.addAll(repliesOnMessage(teamId, channelId, messageId, CONSTANT.FIFTY));

        for (Message message1 : messages) {
            String chat = message1.getName() + CONSTANT.SPACE + CONSTANT.COLON + CONSTANT.SPACE + message1.getText() + CONSTANT.DOT;
            questionBuilder.append(chat);
        }
        return questionBuilder.toString();
    }

    private Message mappingVallWithMessage(Vall vall) {
        String messageId = CONSTANT.ANONYMOUS;
        String name = CONSTANT.ANONYMOUS;
        String text = CONSTANT.EMPTY;
        if (vall != null && vall.getId() != null) {
            messageId = vall.getId();
        }
        if (vall != null && vall.getForm() != null && vall.getForm().getUser() != null && vall.getForm().getUser().getDisplayName() != null) {
            name = vall.getForm().getUser().getDisplayName();
        }
        if (vall != null && vall.getBody() != null && vall.getBody().getMessage() != null) {
            text = vall.getBody().getMessage();
        }
        text = HtmlToTextConverter.convertHtmlToText(text);
        return new Message(messageId, name, text);
    }

    private Vall mappingResponseWithVall(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response, Vall.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Method mappingResponseWithVall in class TeamsService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private List<Channel> mappingChannelResponseBaseWithChannel(ChannelResponseBase channel) {
        Val[] check = channel.getVal();
        List<Channel> channelNames = new ArrayList<>();
        for (Val val : check) {
            String channelId = CONSTANT.EMPTY;
            String channelName = CONSTANT.EMPTY;
            if (val != null && val.getChannelId() != null) channelId = val.getChannelId();
            if (val != null && val.getChannelName() != null) channelName = val.getChannelName();
            channelNames.add(new Channel(channelId, channelName));
        }
        return channelNames;
    }

    private ChannelResponseBase mappingResponseWithChannelResponseBase(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response, ChannelResponseBase.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Method mappingResponseWithChannelResponseBase in class TeamsService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private List<Message> mappingMessageResponseBaseWithMessage(MessageResponseBase messages) {
        Vall[] messageValues = messages.getValue();
        List<Message> allMessages = new ArrayList<>();
        for (Vall vall : messageValues) {
            allMessages.add(mappingVallWithMessage(vall));
        }
        return allMessages;
    }

    private MessageResponseBase mappingResponseWithMessageResponseBase(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response, MessageResponseBase.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Method mappingResponseWithMessageResponseBase in class TeamsService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }


    private String responseDataReceived(String url) {
        try {
            headers.set(CONSTANT.AUTHORIZATION, CONSTANT.BEARER + CONSTANT.SPACE + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException.Gone e) {
            throw new IllegalArgumentException("The requested resource is no longer available: " + e.getMessage(), e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException("Failed to make HTTP request: " + e.getMessage(), e);
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Invalid URL or network error: " + e.getMessage(), e);
        }
    }


}