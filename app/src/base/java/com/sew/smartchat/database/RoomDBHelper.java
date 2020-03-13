package com.sew.smartchat.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.sew.smartchat.activities.BaseActivity;
import com.sew.smartchat.model.SavedMessageFormat;

import java.util.List;

public class RoomDBHelper {

    private ChatDatabase chatDatabase;

    public RoomDBHelper(Context context){
        chatDatabase = Room.databaseBuilder(context, ChatDatabase.class, BaseActivity.DBNAME).allowMainThreadQueries().build();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertTopic(final String topic){
        TopicDetail topicDetail = new TopicDetail(topic);
        insertTopic(topicDetail);
    }

    public void insertUser(String token, String username){
        UserDetail userDetail = new UserDetail(token, username);
        insertUser(userDetail);
    }

    private void insertUser(UserDetail userDetail){
        chatDatabase.daoAccess().insertUser(userDetail);
    }

    private void insertTopic(TopicDetail topicDetail){
        chatDatabase.daoAccess().insertTopic(topicDetail);
    }

    public void insertMessage(String userToken, int topicID, String time, String date, String message) {
        MessageDetail messageDetail = new MessageDetail(userToken, topicID, time, date, message);
        insertMessage(messageDetail);
    }

    private void insertMessage(MessageDetail messageDetail){
        chatDatabase.daoAccess().insertMessage(messageDetail);
    }

    public List<Integer> getTopicID(String topicName){
        return chatDatabase.daoAccess().getTopicID(topicName);
    }

    public List<String> getUsername(String userToken){
        return chatDatabase.daoAccess().getUsername(userToken);
    }

    public List<SavedMessageFormat> getAllMessages(){
        return chatDatabase.daoAccess().getAllMessages();
    }

    public List<UserDetail> getAllUsers(){
        return chatDatabase.daoAccess().getAllUsers();
    }

}
