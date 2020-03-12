package com.sew.smartchat.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class TopicDetail implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int topicID;

    public TopicDetail(String topicName) {
        this.topicName = topicName;
    }

    String topicName;


    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
