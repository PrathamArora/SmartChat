package com.sew.smartchat.activities;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sew.smartchat.R;
import com.sew.smartchat.adapter.ChatAdapter;
import com.sew.smartchat.database.RoomDBHelper;
import com.sew.smartchat.model.ChatMessage;
import com.sew.smartchat.model.SavedMessageFormat;
import com.sew.smartchat.threads.InsertMessageInterface;
import com.sew.smartchat.threads.InsertTopicAsyncTask;
import com.sew.smartchat.threads.InsertTopicInterface;
import com.sew.smartchat.threads.InsertUserAsyncTask;
import com.sew.smartchat.threads.InsertUserInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends BaseActivity implements InsertUserInterface, InsertTopicInterface, InsertMessageInterface {

    public boolean checkIfUserTokenPresent(String token, String username) {
        List<String> alreadyUserPresent = roomDBHelper.getUsername(token);

        return alreadyUserPresent != null && alreadyUserPresent.size() != 0;
    }

    public boolean checkIfTopicPresent(String topic) {
        List<Integer> alreadyTopicPresent = roomDBHelper.getTopicID(topic);

        return alreadyTopicPresent != null && alreadyTopicPresent.size() != 0;
    }

    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Add data to linearlayout all_messages layout
//            Toast.makeText(context, "Broadcast Notification : " + intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
            String myToken = sharedPreferences.getString(TOKEN, "empty");


            String token = intent.getStringExtra("userToken");
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");


            ChatMessage chatMessage = new ChatMessage(true, title, message, date, time);

            if (!myToken.equals(token)) {
                chatMessage.setSelf(false);
            }

            allMessages.add(chatMessage);

            chatAdapter.setAllMessages(allMessages);
            chatAdapter.notifyDataSetChanged();

//            InsertMessageAsyncTask insertMessageAsyncTask = new InsertMessageAsyncTask();
//            insertMessageAsyncTask.insertMessageInterface = ChatActivity.this;
//            insertMessageAsyncTask.context = getApplicationContext();
//            insertMessageAsyncTask.execute(token, title, CHAT_TOPIC, time, date, message);

            chat_layout.smoothScrollToPosition(allMessages.size() - 1);

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(FIREBASE_NOTIFICATION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllMessages();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }


    @Override
    public void userInserted(boolean result) {

        showToast("here");

        FirebaseMessaging.getInstance().subscribeToTopic(CHAT_TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                roomDBHelper.insertTopic(CHAT_TOPIC);
                if (!checkIfTopicPresent(CHAT_TOPIC)) {
                    insertTopicAsyncTask.execute(CHAT_TOPIC);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Unable to subscribe to " + CHAT_TOPIC);
            }
        });

    }


    @Override
    public void topicInserted(boolean result) {
        showToast("Topic Inserted");
    }

    @Override
    public void messageInserted(boolean result) {
        showToast("Message Inserted");
    }

    public void loadAllMessages() {
        List<SavedMessageFormat> allSavedMessages = roomDBHelper.getAllMessages();

        SharedPreferences sharedPreferences = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
        String myToken = sharedPreferences.getString(BaseActivity.TOKEN, "empty");

        allMessages = new ArrayList<>();

        for (int i = 0; i < allSavedMessages.size(); i++) {
            String title = allSavedMessages.get(i).getUserName();
            String message = allSavedMessages.get(i).getMessage();
            String date = allSavedMessages.get(i).getDate();
            String time = allSavedMessages.get(i).getTime();
            String token = allSavedMessages.get(i).getUserToken();
            ChatMessage chatMessage = new ChatMessage(true, title, message, date, time);

            if (!myToken.equals(token)) {
                chatMessage.setSelf(false);
            }

            allMessages.add(chatMessage);
        }

        chatAdapter.setAllMessages(allMessages);
        chatAdapter.notifyDataSetChanged();
        if (allMessages.size() != 0) {
            chat_layout.smoothScrollToPosition(allMessages.size() - 1);
        }

    }


    InsertUserAsyncTask insertUserAsyncTask = new InsertUserAsyncTask();
    InsertTopicAsyncTask insertTopicAsyncTask = new InsertTopicAsyncTask();

    EditText editTextMessage = null;
//    @BindView(R.id.editTextMessage) EditText editTextMessage;
    Button sendButton = null;
//    @BindView(R.id.sendButton) Button sendButton;

//    @OnClick(R.id.sendButton)
//    public void onClick(View view){
//
//    }

    RecyclerView chat_layout = null;
//    @BindView(R.id.chat_layout) RecyclerView chat_layout;

    ArrayList<ChatMessage> allMessages = new ArrayList<>();
    ChatAdapter chatAdapter;

    RoomDBHelper roomDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_chat_layout);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.sendButton);
        chat_layout = findViewById(R.id.chat_layout);
        roomDBHelper = new RoomDBHelper(this);
        insertUserAsyncTask.insertUserInterface = this;
        insertUserAsyncTask.context = this;

        insertTopicAsyncTask.insertTopicInterface = this;
        insertTopicAsyncTask.context = this;

        chatAdapter = new ChatAdapter(this, allMessages);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerLayoutManager.setOrientation(RecyclerView.VERTICAL);
        chat_layout.setLayoutManager(recyclerLayoutManager);
        chat_layout.setAdapter(chatAdapter);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        if(task.getResult() != null){
                            String token = task.getResult().getToken();

                            SharedPreferences sharedPreferences = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TOKEN, token);
                            editor.apply();

                            sharedPreferences = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
                            String username = sharedPreferences.getString(USER_NAME, "default user");

                            if (!checkIfUserTokenPresent(token, username)) {
                                insertUserAsyncTask.execute(token, username);
                            }

                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, msg);
                        }
                    }
                });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMessage.getText().toString().length() != 0) {
                    sendMessage();
                }
            }
        });

        loadAllMessages();

    }


    public void sendMessage() {
        try {
            JSONObject inputBody = new JSONObject();
            inputBody.put("to", URL_ROUTE);

            JSONObject data = new JSONObject();

            SharedPreferences userToken = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
            SharedPreferences userDetails = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);

            data.put("title", userDetails.getString(USER_NAME, "username"));
            data.put("message", editTextMessage.getText().toString());
            editTextMessage.setText("");
            data.put("userToken", userToken.getString(TOKEN, "empty"));
            data.put("date", getCurrentDate());
            data.put("time", getCurrentTime());

            inputBody.put("data", data);


            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, inputBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Not done");
                            showToast("Unable to send message");

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", AUTH_TOKEN);
                    params.put("Content-Type", CONTENT_TYPE);
                    return params;
                }
            };


            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            showToast("Unable to send data!");
        }

    }


//    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Add data to linearlayout all_messages layout
////            Toast.makeText(context, "Broadcast Notification : " + intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();
//
//            SharedPreferences sharedPreferences = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
//            String myToken = sharedPreferences.getString(BaseActivity.TOKEN, "empty");
//
//
//            String token = intent.getStringExtra("userToken");
//            String title = intent.getStringExtra("title");
//            String message = intent.getStringExtra("message");
//            String date = intent.getStringExtra("date");
//            String time = intent.getStringExtra("time");
//
//
//            if(myToken.equals(token)){
//
//                View own_message_layout = LayoutInflater.from(context).inflate(R.layout.own_message, all_messages, false);
//                TextView own_message_textview = own_message_layout.findViewById(R.id.message_text);
//                own_message_textview.setText(message);
//
//                TextView date_textview = own_message_layout.findViewById(R.id.date);
//                date_textview.setText(date);
//
//                TextView time_textview = own_message_layout.findViewById(R.id.time);
//                time_textview.setText(time);
//                all_messages.addView(own_message_layout);
//
//            }else{
//
//                View other_message_layout = LayoutInflater.from(context).inflate(R.layout.others_message, all_messages, false);
//
//                TextView other_message_textview = other_message_layout.findViewById(R.id.message_text);
//                other_message_textview.setText(message);
//
//                TextView other_message_username = other_message_layout.findViewById(R.id.message_username);
//                other_message_username.setText(title);
//
//                TextView date_textview = other_message_layout.findViewById(R.id.date);
//                date_textview.setText(date);
//
//                TextView time_textview = other_message_layout.findViewById(R.id.time);
//                time_textview.setText(time);
//
//                all_messages.addView(other_message_layout);
//            }
//
//            chat_layout.fullScroll(ScrollView.FOCUS_DOWN);
//        }
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(FIREBASE_NOTIFICATION));
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
//    }
//
//    EditText editTextMessage = null;
//    Button sendButton = null;
//    LinearLayout all_messages = null;
//    ScrollView chat_layout = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        editTextMessage = findViewById(R.id.editTextMessage);
//        sendButton = findViewById(R.id.sendButton);
//        all_messages = findViewById(R.id.all_messages);
//        chat_layout = findViewById(R.id.chat_layout);
//
//
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        String token = task.getResult().getToken();
//
//                        SharedPreferences sharedPreferences = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString(TOKEN, token);
//                        editor.apply();
//
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                    }
//                });
//
//        FirebaseMessaging.getInstance().subscribeToTopic(CHAT_TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                showToast("Unable to subscribe to " + CHAT_TOPIC);
//            }
//        });
//
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(editTextMessage.getText().toString().length() != 0){
//                    sendMessage();
//                }
//            }
//        });
//
//    }
//
//
//    public void sendMessage(){
//        try{
//            JSONObject inputBody = new JSONObject();
//            inputBody.put("to", URL_ROUTE);
//
//            JSONObject data = new JSONObject();
//
//            SharedPreferences userToken = getSharedPreferences(USER_TOKEN, MODE_PRIVATE);
//            SharedPreferences userDetails = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
//
//            data.put("title", userDetails.getString(USER_NAME, "username"));
//            data.put("message", editTextMessage.getText().toString());
//            editTextMessage.setText("");
//            data.put("userToken", userToken.getString(TOKEN, "empty"));
//            data.put("date", getCurrentDate());
//            data.put("time", getCurrentTime());
//
//            inputBody.put("data", data);
//
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, inputBody,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d(TAG, response.toString());
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d(TAG, "Not done");
//                            showToast("Unable to send message");
//
//                        }
//                    }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Authorization", AUTH_TOKEN);
//                    params.put("Content-Type", CONTENT_TYPE);
//                    return params;
//                }
//            };
//
//
//            requestQueue.add(jsonObjectRequest);
//
//        }catch(JSONException e){
//            showToast("Unable to send data!");
//        }
//
//    }
//
}
