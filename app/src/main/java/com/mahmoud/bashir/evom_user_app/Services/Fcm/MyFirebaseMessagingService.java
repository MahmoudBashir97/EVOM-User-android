package com.mahmoud.bashir.evom_user_app.Services.Fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mahmoud.bashir.evom_user_app.R;


import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String MESSAGE = "message";

    public ImageView imageView;
    private DatabaseReference getbadgeCount;
    int count=0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(LOG_TAG, remoteMessage.getData().toString() + "");

        Log.d(LOG_TAG, "onMessageReceived: ");
        String request = remoteMessage.getData().get("request");

        if (remoteMessage.getData().get("request").equals("message")){

            String senderId = remoteMessage.getData().get("senderId");
            String senderName = remoteMessage.getData().get("senderName");
            String receiverId=remoteMessage.getData().get("receiverId");
            String message = remoteMessage.getData().get("message");
            String image=remoteMessage.getData().get("imageReceiver");
            String countbadge=remoteMessage.getData().get("countbadge");


            handleInviteIntent(senderId, senderName,receiverId,message,countbadge);

        }

        if (request.equals("req")){

            String senderId = remoteMessage.getData().get("senderId");
            String senderName = remoteMessage.getData().get("senderName");
            SendRequestChat(senderId,senderName);
        }
    }

    private void handleInviteIntent(String senderId, String senderName,String receiverId, String message,String countbadge) {

         count= Integer.parseInt(countbadge);

        Intent reciveMessage = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("message")
                .putExtra("senderId", senderId)
                .putExtra("senderName", senderName)
                .putExtra("receiverId",receiverId)
                .putExtra("message", message)
                //.putExtra("imageReceiver",image)
                .putExtra("countbadge",countbadge);

        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(reciveMessage);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        2,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.app.Notification build = new NotificationCompat.Builder(this, MESSAGE)
               // .setSmallIcon(R.drawable.chat_icon2)
                .setPriority(PRIORITY_MAX)
                .setContentTitle(String.format("You have new message from ", senderName))
                //.addAction(R.drawable.ic_check_black_24dp, "show message", pendingIntentAccept)
                .setVibrate(new long[3000])
                .setChannelId(MESSAGE)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(MESSAGE, MESSAGE, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(1, build);

    }

    private void SendRequestChat(String senderId, String senderName){

        Intent reciveMessage = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("chatrequest")
                .putExtra("senderId", senderId)
                .putExtra("senderName", senderName);

        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 3, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(reciveMessage);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        3,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.app.Notification build = new NotificationCompat.Builder(this, MESSAGE)
                //.setSmallIcon(R.drawable.ic_add_request)
                .setPriority(PRIORITY_MAX)
                .setContentTitle(String.format("You have new chat Request \n From", senderName))
                //.addAction(R.drawable.ic_check_black_24dp, "Open", pendingIntentAccept)
                .setVibrate(new long[3000])
                .setChannelId(MESSAGE)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(MESSAGE, MESSAGE, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(2, build);

    }



  /*
    @Override
    public void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }*/
}
