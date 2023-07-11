package com.teamsapi.entity.teamsapi.messageresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {
    @JsonProperty("content")
    private String message;

    public Body(String message) {
        this.message = message;
    }

    public Body() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
