package com.teamsapi.entity.teamsapi;


import org.springframework.data.mongodb.core.mapping.Document;

@Document("messages")
public class Message {
    private String messageId;
    private String name;
    private String text;

    public Message(String messageId, String name, String text) {
        this.messageId = messageId;
        this.name = name;
        this.text = text;
    }

    public Message() {

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
