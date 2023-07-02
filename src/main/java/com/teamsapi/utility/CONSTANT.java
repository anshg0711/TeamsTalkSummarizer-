package com.teamsapi.utility;

public class CONSTANT {
    public static final String TEAMS_API_ENDPOINT = "https://graph.microsoft.com/v1.0/teams/";
    public static final String CHANNELS = "/channels/";

    public static final String MESSAGES = "/messages/";
    public static final String MESSAGESTOP = "/messages?$top=";
    public static final String REPLIESTOP = "/replies?$top=";

    public static final String TOP = "top";
    public static final String SLASH = "/";
    public static final String REPLIES = "replies";
    public static final String QUESTIONMARK="?";


    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String DOT = ".";
    public static final String SUMMARIZE = "Summarize the following chats: ";
    public static final String FIFTY = "50";

    public static final String ALLCHANNELS = "/allChannels";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String EMPTY="";
    public static final String ANONYMOUS="anonymous";

    public static final String CHATGPT_ENDPOINT = "https://api.openai.com/v1/completions";

    public static final String EQUAL="=";

    public static final String AND="&";
    public static final String CHATGPTREQUESTBODY="""
                {
                  "model": "text-davinci-003","prompt": "Hi","temperature": 1,"max_tokens": 256,"top_p": 1,"frequency_penalty": 0,"presence_penalty": 0
                }
                """;
    public static  final String HI="Hi";


}
