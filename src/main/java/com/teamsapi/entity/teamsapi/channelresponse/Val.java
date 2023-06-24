package com.teamsapi.entity.teamsapi.channelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Val {
    @JsonProperty("displayName")
    private String channelName;

    @JsonProperty("id")
    private String channelId;

    public Val(String channelName, String channelId) {
        this.channelName = channelName;
        this.channelId = channelId;
    }
    public Val(){

    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


}
