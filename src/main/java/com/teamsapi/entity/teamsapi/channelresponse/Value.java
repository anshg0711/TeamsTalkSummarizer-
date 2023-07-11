package com.teamsapi.entity.teamsapi.channelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {
    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("id")
    private String id;

    public Value(String channelName, String channelId) {
        this.displayName = channelName;
        this.id = channelId;
    }
    public Value(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
