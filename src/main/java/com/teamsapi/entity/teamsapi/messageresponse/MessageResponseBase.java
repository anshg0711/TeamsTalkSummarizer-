package com.teamsapi.entity.teamsapi.messageresponse;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MessageResponseBase {

    @JsonProperty("value")
    private Value value[];

    public MessageResponseBase(Value[] value) {

        this.value = value;
    }
    public MessageResponseBase(){

    }

    public void setValue(Value[] value) {
        this.value = value;
    }


    public Value[] getValue() {
        return value;
    }


}