package com.teamsapi.utility;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class utility {

    private utility() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TEAMS_API_ENDPOINT = "https://graph.microsoft.com/v1.0/teams/";
    public static final String CHANNELS = "/channels/";
    public static final String MESSAGES = "/messages/";
    public static final String MESSAGES_TOP = "/messages?$top=";
    public static final String REPLIES = "/replies";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String DOT = ".";
    public static final String SUMMARIZE = "Summarize the following chat in an shashi tharoor style: ";
    public static final String ALL_CHANNELS = "/allChannels";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String EMPTY = "";
    public static final String ANONYMOUS = "anonymous";
    public static final String CHATGPT_ENDPOINT = "https://api.openai.com/v1/completions";
    public static final String CHATGPT_REQUEST_BODY = """
            {
              "model": "text-davinci-003","prompt": "Hi","temperature": 1,"max_tokens": 256,"top_p": 1,"frequency_penalty": 0,"presence_penalty": 0
            }
            """;
    public static final String HI = "Hi";
    private static ObjectMapper objectMapper = null;
    private static HttpHeaders httpHeaders =  httpHeaders = new HttpHeaders();
    private static RestTemplate restTemplate = restTemplate = new RestTemplate();

    public static ObjectMapper objectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }


}
