package com.sew.smartchat.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sew.smartchat.R;
import com.sew.smartchat.activities.BaseActivity;
import com.sew.smartchat.activities.ChatActivity;
import com.sew.smartchat.database.RoomDBHelper;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.sew.smartchat.activities.BaseActivity.CHAT_TOPIC;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//            //            Toast.makeText(getApplicationContext(), "Message Received " + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
//        }
//        if (remoteMessage.getData().size() > 0) {
//            Toast.makeText(getApplicationContext(), "Message Received " + remoteMessage.getData(), Toast.LENGTH_SHORT).show();
//        }

        SharedPreferences sharedPreferences = getSharedPreferences(BaseActivity.USER_TOKEN, MODE_PRIVATE);
        String myToken = sharedPreferences.getString(BaseActivity.TOKEN, "empty");

        if (remoteMessage.getData().containsKey("userToken") &&
                (Objects.equals(remoteMessage.getData().get("userToken"), "empty") || !Objects.equals(remoteMessage.getData().get("userToken"), myToken))) {

            final Intent intent = new Intent(this, ChatActivity.class);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID = new Random().nextInt(3000);

          /*
            Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
            to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
          */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.notify_icon);

            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.notify_icon)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);

            //Set notification color to match your app color template
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark, null));

            assert notificationManager != null;
            notificationManager.notify(notificationID, notificationBuilder.build());
        }

        String time = BaseActivity.getCurrentTime(), date = BaseActivity.getCurrentDate();

        if(remoteMessage.getData().containsKey("date") && remoteMessage.getData().get("date") != null){
            date = remoteMessage.getData().get("date");
        }

        if(remoteMessage.getData().containsKey("time") && remoteMessage.getData().get("time") != null){
            time = remoteMessage.getData().get("time");
        }

        insertMessageInDB(  remoteMessage.getData().get("time"), remoteMessage.getData().get("date"),
                            remoteMessage.getData().get("title"), remoteMessage.getData().get("message"),
                            remoteMessage.getData().get("userToken") );

        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent()
                        .putExtra("userToken", remoteMessage.getData().get("userToken"))
                        .putExtra("title", remoteMessage.getData().get("title"))
                        .putExtra("message", remoteMessage.getData().get("message"))
                        .putExtra("time", time)
                        .putExtra("date", date)
                        .setAction(BaseActivity.FIREBASE_NOTIFICATION));

    }

    private void insertMessageInDB(String time, String date, String title, String message, String userToken) {
        RoomDBHelper roomDBHelper = new RoomDBHelper(getApplicationContext());



        List<String> username = roomDBHelper.getUsername(userToken);
        if(username == null || username.size() == 0){
            roomDBHelper.insertUser(userToken, title);
        }

        List<Integer> topicID = roomDBHelper.getTopicID(CHAT_TOPIC);
        if(topicID == null || topicID.size() == 0) {
            roomDBHelper.insertTopic(CHAT_TOPIC);
        }

        int currentTopicID = roomDBHelper.getTopicID(CHAT_TOPIC).get(0);

        roomDBHelper.insertMessage(userToken, currentTopicID, time, date, message);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

}
