package com.teamsapi.entity.teamsapi;

public class Channel {
    private String channelId;
    private String channelName;

    public Channel(String channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }
    public Channel(){

    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


}
