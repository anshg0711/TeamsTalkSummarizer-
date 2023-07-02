package com.teamsapi.entity.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatGptResponseBase {
    @JsonProperty("choices")
    private Choice[] choices;

    public void setChoice(Choice[] choice) {
        this.choices = choice;
    }

    public Choice[] getChoice() {
        return choices;
    }


}
