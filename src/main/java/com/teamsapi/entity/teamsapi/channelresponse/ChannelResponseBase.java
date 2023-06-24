package com.teamsapi.entity.teamsapi.channelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelResponseBase {
    @JsonProperty("value")
    private Val[] val;

    public ChannelResponseBase(Val[] val) {
        this.val = val;
    }

    public ChannelResponseBase() {

    }

    public Val[] getVal() {
        return val;
    }

    public void setVal(Val[] val) {
        this.val = val;
    }


}
