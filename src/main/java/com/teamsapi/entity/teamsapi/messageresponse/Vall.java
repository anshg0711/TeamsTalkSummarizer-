package com.teamsapi.entity.teamsapi.messageresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vall {
    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private Form form;

    @JsonProperty("body")
    private Body body;

    public Vall(String id, Form form, Body body) {
        this.id = id;
        this.form = form;
        this.body = body;
    }

    public Vall() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}