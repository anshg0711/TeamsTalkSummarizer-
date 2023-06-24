package com.teamsapi.entity.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatGptResponseBase {
    @JsonProperty("choices")
    private Choice[] choice;

    public void setChoice(Choice[] choice) {
        this.choice = choice;
    }

    public Choice[] getChoice() {
        return choice;
    }


}
