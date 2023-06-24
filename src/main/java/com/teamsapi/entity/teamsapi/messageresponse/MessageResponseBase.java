package com.teamsapi.entity.teamsapi.messageresponse;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MessageResponseBase {

    @JsonProperty("value")
    private Vall value[];

    public MessageResponseBase(Vall[] value) {

        this.value = value;
    }
    public MessageResponseBase(){

    }

    public void setValue(Vall[] value) {
        this.value = value;
    }


    public Vall[] getValue() {
        return value;
    }


}