package com.teamsapi.entity.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {
    @JsonProperty("text")
    private String text;

    public Choice(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
