package com.teamsapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsapi.entity.teamsapi.Channel;
import com.teamsapi.entity.teamsapi.Message;
import com.teamsapi.entity.teamsapi.channelresponse.ChannelResponseBase;
import com.teamsapi.entity.teamsapi.messageresponse.MessageResponseBase;
import com.teamsapi.entity.teamsapi.messageresponse.Value;
import com.teamsapi.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TeamsService {
    private final ChatGptService chatGptService;
    private final String token;
    private final RestTemplate restTemplate = Utility.restTemplate;
    public final HttpHeaders headers = Utility.httpHeaders;
    public final ObjectMapper objectMapper = Utility.objectMapper();

    private final HtmlToTextConverter htmlToTextConverter;
    @Autowired
    public TeamsService(ChatGptService chatGptService, @org.springframework.beans.factory.annotation.Value("${team.bearer.token}") String token, HtmlToTextConverter htmlToTextConverter) {

        this.chatGptService = chatGptService;
        this.token = token;
        this.htmlToTextConverter = htmlToTextConverter;
    }

    public List<Message> getMessagesInChannel(String teamId, String channelId, String top) {
        String url = Utility.TEAMS_API_ENDPOINT + teamId + Utility.CHANNELS + channelId + Utility.MESSAGES_TOP + top;
        String response = getResponse(url);
        MessageResponseBase messages = mapResponseToMessageResponseBase(response);
        return mapMessageResponseToMessages(messages);
    }

    public List<Message> getRepliesOnMessage(String teamId, String channelId, String messageId) {
        String url = Utility.TEAMS_API_ENDPOINT + teamId + Utility.CHANNELS + channelId + Utility.MESSAGES + messageId + Utility.REPLIES;
        String response = getResponse(url);
        MessageResponseBase messages = mapResponseToMessageResponseBase(response);
        List<Message> messageData = mapMessageResponseToMessages(messages);
        Collections.reverse(messageData);
        return messageData;
    }
    public Message getMessageByIdentifier(String teamId, String channelId, String messageId) {
        String url = Utility.TEAMS_API_ENDPOINT + teamId + Utility.CHANNELS + channelId + Utility.MESSAGES + messageId;
        String response = getResponse(url);
        Value value = mapResponseToValue(response);
        return mapValueToMessage(value);
    }

    public List<Channel> getAllChannels(String teamId) {
        String url = Utility.TEAMS_API_ENDPOINT + teamId + Utility.ALL_CHANNELS;
        String response = getResponse(url);
        ChannelResponseBase channel = mapResponseToChannelResponseBase(response);
        return mapChannelResponseToChannels(channel);
    }

    public String summarizeRepliesForMessage(String teamId, String channelId, String messageId) {
        String question = buildQuestionFromReplies(teamId, channelId, messageId);
        return this.chatGptService.getAnswerToChatGptPrompt(question);
    }

    private String buildQuestionFromReplies(String teamId, String channelId, String messageId) {
        StringBuilder questionBuilder = new StringBuilder(Utility.SUMMARIZE);
        Message message = getMessageByIdentifier(teamId, channelId, messageId);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.addAll(getRepliesOnMessage(teamId, channelId, messageId));
        for (Message message1 : messages) {
            String chat = message1.getName() + Utility.SPACE + Utility.COLON + Utility.SPACE + message1.getText() + Utility.DOT;
            questionBuilder.append(chat);
        }
        return questionBuilder.toString();
    }
    private Message mapValueToMessage(Value value) {
        String messageId = Utility.ANONYMOUS;
        String name = Utility.ANONYMOUS;
        String text = Utility.EMPTY;
        if (value != null && value.getId() != null) {
            messageId = value.getId();
        }
        if (value != null && value.getForm() != null && value.getForm().getUser() != null && value.getForm().getUser().getDisplayName() != null) {
            name = value.getForm().getUser().getDisplayName();
        }
        if (value != null && value.getBody() != null && value.getBody().getMessage() != null) {
            text = value.getBody().getMessage();
        }
        text = htmlToTextConverter.convertHtmlToText(text);
        return new Message(messageId, name, text);
    }

    private Value mapResponseToValue(String response) {
        try {
            return objectMapper.readValue(response, Value.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Method mappingResponseWithValue in class TeamsService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private List<Channel> mapChannelResponseToChannels(ChannelResponseBase channel) {
        com.teamsapi.entity.teamsapi.channelresponse.Value[] check = channel.getVal();
        List<Channel> channelNames = new ArrayList<>();
        for (com.teamsapi.entity.teamsapi.channelresponse.Value value : check) {
            String channelId = Utility.EMPTY;
            String channelName = Utility.EMPTY;
            if (value != null && value.getId() != null) channelId = value.getId();
            if (value != null && value.getDisplayName() != null) channelName = value.getDisplayName();
            channelNames.add(new Channel(channelId, channelName));
        }
        return channelNames;
    }

    private ChannelResponseBase mapResponseToChannelResponseBase(String response) {
        try {
            return objectMapper.readValue(response, ChannelResponseBase.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Method mappingResponseWithChannelResponseBase in class TeamsService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private List<Message> mapMessageResponseToMessages(MessageResponseBase messages) {
        Value[] messageValues = messages.getValue();
        List<Message> allMessages = new ArrayList<>();
        for (Value value : messageValues) {
            allMessages.add(mapValueToMessage(value));
        }
        return allMessages;
    }

    private MessageResponseBase mapResponseToMessageResponseBase(String response) {
        try {
            return objectMapper.readValue(response, MessageResponseBase.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Method mappingResponseWithMessageResponseBase in class TeamsService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private String getResponse(String url) {
        try {
            headers.set(Utility.AUTHORIZATION, Utility.BEARER + Utility.SPACE + token);
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