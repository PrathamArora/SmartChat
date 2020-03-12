package com.sew.smartchat.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.sew.smartchat.database.RoomDBHelper;

public class InsertUserAsyncTask extends AsyncTask<String, Void, Void> {

    public InsertUserInterface insertUserInterface = null;

    @SuppressLint("StaticFieldLeak")
    public Context context = null;

    @Override
    protected Void doInBackground(String... strings) {
        String token = strings[0];
        String username = strings[1];

        RoomDBHelper roomDBHelper = new RoomDBHelper(context);
        roomDBHelper.insertUser(token, username);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        insertUserInterface.userInserted(true);

    }
}
