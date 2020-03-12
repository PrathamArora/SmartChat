package com.sew.smartchat.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.sew.smartchat.database.RoomDBHelper;

public class InsertTopicAsyncTask extends AsyncTask<String, Void, Void> {

    public InsertTopicInterface insertTopicInterface = null;
    @SuppressLint("StaticFieldLeak")
    public Context context = null;


    @Override
    protected Void doInBackground(String... strings) {
        String topic = strings[0];

        RoomDBHelper roomDBHelper = new RoomDBHelper(context);
        roomDBHelper.insertTopic(topic);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        insertTopicInterface.topicInserted(true);
    }
}
