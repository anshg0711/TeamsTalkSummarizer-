package com.teamsapi.entity.teamsapi.messageresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Form {
    @JsonProperty("user")
    private User user;

    public Form(User user) {
        this.user = user;
    }

    public Form() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
