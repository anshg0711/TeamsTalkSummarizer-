package com.teamsapi.entity.teamsapi.messageresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id")
    private String unknownId;
    @JsonProperty("displayName")
    private String displayName;

    public User(String unknownId, String displayName) {
        this.unknownId = unknownId;
        this.displayName = displayName;
    }

    public User() {

    }

    public String getUnknownId() {
        return unknownId;
    }

    public void setUnknownId(String unknownId) {
        this.unknownId = unknownId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}
