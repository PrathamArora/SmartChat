package com.sew.smartchat.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MessageDetail implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int _id;

    String userToken;
    int topicID;
    String time;
    String date;
    String message;

    public MessageDetail(String userToken, int topicID, String time, String date, String message) {
        this.userToken = userToken;
        this.topicID = topicID;
        this.time = time;
        this.date = date;
        this.message = message;
    }




}
