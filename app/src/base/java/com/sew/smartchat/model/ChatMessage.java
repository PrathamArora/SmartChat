package com.sew.smartchat.model;

public class ChatMessage {
    private boolean self;
    private String title, message, date, time;

    public ChatMessage(boolean self, String title, String message, String date, String time) {
        this.self = self;
        this.title = title;
        this.message = message;
        this.date = date;
        this.time = time;
    }


    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }





}
