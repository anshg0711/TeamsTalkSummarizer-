package com.teamsapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsapi.entity.chatgpt.ChatGptResponseBase;
import com.teamsapi.utility.CONSTANT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {
    private final String token;

    @Autowired
    public ChatGptService(@Value("${chatGpt.bearer.token}") String token) {
        this.token = token;
    }

    private String responseDataReceived(String question) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(CONSTANT.AUTHORIZATION, CONSTANT.BEARER + CONSTANT.SPACE + token);
        String requestBody = CONSTANT.CHATGPTREQUESTBODY;
        requestBody = requestBody.replace(CONSTANT.HI, question);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(CONSTANT.CHATGPT_ENDPOINT, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody();
    }

    private ChatGptResponseBase mappingResponseWithChatGptResponseBase(String response){
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response, ChatGptResponseBase.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Method mappingResponseWithChatGptResponseBase in class ChatGptService, Failed to deserialize JSON response: " + e.getMessage(), e);
        }
    }

    private String extractingTextFromChatGptResponseBase(ChatGptResponseBase chatGptResponseBase) {

        return HtmlToTextConverter.convertHtmlToText(chatGptResponseBase.getChoice()[0].getText());
    }

    public String answerToPromptOfChatGpt(String question){
        String response = responseDataReceived(question);
        ChatGptResponseBase chatGptResponseBase = mappingResponseWithChatGptResponseBase((response));
        return extractingTextFromChatGptResponseBase(chatGptResponseBase);
    }

}

