package com.sew.smartchat.activities;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    public static final String USER_DETAILS = "UserDetail";
    public static final String USER_NAME = "username";
    public static final String FIREBASE_NOTIFICATION = "FIREBASE_NOTIFICATION";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String TOKEN = "TOKEN";
    public static final String CHAT_TOPIC = "groupChat";
    public static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    public static final String TAG = "firebase";
    public static final String URL_ROUTE = "/topics/groupChat";
    public static final String AUTH_TOKEN = "key=AAAAHW9jODM:APA91bGaJtTkV85DPaHkrZdK-qPtgWw5MdXLuYZW5qcVAJNBYfDGgJUwVKVbE9ttV3TDxwxhtQhlSsV6b4WTIzpK8BITgapT0DBen2STLCb5C7BB0Yqwtpfk33Q7wsaGcNT5Dwbrp8uu";
    public static final String CONTENT_TYPE = "application/json";

    public static final String DBNAME = "ChatsDatabase.db";

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String getCurrentDate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        return dateFormat.format(currentTime);
    }

    public static String getCurrentTime(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.US);
        return timeFormat.format(currentTime);
    }

}
