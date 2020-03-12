package com.sew.smartchat.model;

import java.io.Serializable;

public class SavedMessageFormat implements Serializable {
    private String date;
    private String time;
    private String message;
    private String userToken;
    private String userName;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SavedMessageFormat(String date, String time, String message, String userToken, String userName) {
        this.date = date;
        this.time = time;
        this.message = message;
        this.userToken = userToken;
        this.userName = userName;
    }
}
