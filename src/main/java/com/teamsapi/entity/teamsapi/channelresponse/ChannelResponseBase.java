package com.teamsapi.entity.teamsapi.channelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelResponseBase {
    @JsonProperty("value")
    private Value[] value;


    public ChannelResponseBase(Value[] value) {
        this.value = value;
    }

    public ChannelResponseBase() {

    }

    public Value[] getVal() {
        return value;
    }

    public void setVal(Value[] value) {
        this.value = value;
    }


}
