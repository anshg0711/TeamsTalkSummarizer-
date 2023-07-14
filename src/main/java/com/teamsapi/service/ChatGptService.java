package com.teamsapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsapi.entity.chatgpt.ChatGptResponseBase;
import com.teamsapi.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {
    private final String token;
    private final HttpHeaders headers= Utility.httpHeaders;
    private final ObjectMapper objectMapper= Utility.objectMapper();
    private final RestTemplate restTemplate= Utility.restTemplate;
    
    private final HtmlToTextConverter htmlToTextConverter;

    @Autowired
    public ChatGptService(@Value("${chatGpt.bearer.token}") String token, HtmlToTextConverter htmlToTextConverter) {
        this.token = token;
        this.htmlToTextConverter = htmlToTextConverter;
    }

    private String sendChatGptRequest(String question) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Utility.AUTHORIZATION, Utility.BEARER + Utility.SPACE + token);
        String requestBody = Utility.CHATGPT_REQUEST_BODY;
        requestBody = requestBody.replace(Utility.HI, question);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(Utility.CHATGPT_ENDPOINT, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody();
    }

    private ChatGptResponseBase mapResponseToChatGptResponseBase(String response) {
        try {
            return objectMapper.readValue(response, ChatGptResponseBase.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Method mappingResponseWithChatGptResponseBase in class ChatGptService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private String extractTextFromChatGptResponse(ChatGptResponseBase chatGptResponseBase) {
        return htmlToTextConverter.convertHtmlToText(chatGptResponseBase.getChoice()[0].getText());
    }
    public String getAnswerToChatGptPrompt(String question) {
        String response = sendChatGptRequest(question);
        ChatGptResponseBase chatGptResponseBase = mapResponseToChatGptResponseBase((response));
        return extractTextFromChatGptResponse(chatGptResponseBase);
    }

}

