package com.mahmoud.bashir.evom_user_app.Services.Fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.mahmoud.bashir.evom_user_app.Maps.Home_Maps_Activity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "MyReceiver";
    private int count=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: " + intent.getAction());

                        String senderId = intent.getExtras().getString("senderID");
                        String senderName = intent.getExtras().getString("senderName");
                        String receiverId = intent.getExtras().getString("receiverId");
                        String message = intent.getExtras().getString("message");
                       // String image = intent.getExtras().getString("image");
                        String countbadge= intent.getExtras().getString("countbadge");


/*

                         if (intent.getAction().equals("message")) {
                            context.startActivity(new Intent(context, Chat_Activity.class)
                                    .putExtra("senderId", senderId)
                                    .putExtra("senderName", senderName)
                                    .putExtra("message", message)
                                    .putExtra("visit_user_id",receiverId)
                                    //.putExtra("image",image)
                                    .putExtra("countbadge",countbadge)
                                    .addFlags(FLAG_ACTIVITY_NEW_TASK));
                        */

        if (intent.getAction().equals("request")) {
            context.startActivity(new Intent(context, Home_Maps_Activity.class)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
