package com.sew.smartchat.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.sew.smartchat.database.RoomDBHelper;

import java.util.List;

public class InsertMessageAsyncTask extends AsyncTask<String, Void, Void> {

    public InsertMessageInterface insertMessageInterface = null;
    @SuppressLint("StaticFieldLeak")
    public Context context = null;

    @Override
    protected Void doInBackground(String... strings) {
        RoomDBHelper roomDBHelper = new RoomDBHelper(context);

        String token  = strings[0];
        String title = strings[1];

        String topic = strings[2];


        String time = strings[3];
        String date = strings[4];
        String message = strings[5];

        List<String> username = roomDBHelper.getUsername(token);
        if(username == null || username.size() == 0){
            roomDBHelper.insertUser(token, title);
        }

        List<Integer> topicID = roomDBHelper.getTopicID(topic);
        if(topicID == null || topicID.size() == 0) {
            roomDBHelper.insertTopic(topic);
        }

        int currentTopicID = roomDBHelper.getTopicID(topic).get(0);

        roomDBHelper.insertMessage(token, currentTopicID, time, date, message);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        insertMessageInterface.messageInserted(true);
    }
}
