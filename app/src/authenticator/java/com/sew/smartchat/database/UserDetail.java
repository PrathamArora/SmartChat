package com.sew.smartchat.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class UserDetail implements Serializable {

    @NonNull
    @PrimaryKey
    String userToken;

    String userName;

    public UserDetail(String userToken, String userName) {
        this.userToken = userToken;
        this.userName = userName;
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
}
