package com.mahmoud.bashir.evom_user_app.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.Services.Fcm.MyReceiver;

import java.text.DecimalFormat;
import java.util.List;


public class Notification_Receiver extends BroadcastReceiver {

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    public void onReceive(Context context, Intent intent) {


        String  myPh = intent.getStringExtra("myPh");
        String  DriverId = intent.getStringExtra("DriverId");
        reference = FirebaseDatabase.getInstance().getReference().child("Requests");

        reference.child(myPh).child(DriverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){

                    String requestStatus = dataSnapshot.child("requestStatus").getValue().toString();
                    String driverName = dataSnapshot.child("driverName").getValue().toString() ;

                    String messageContent;
                    switch (requestStatus){
                        case "accept":
                             messageContent = driverName +"تم قبول طلب التوصيل من قبل ";
                             Notification_fun(context,messageContent);break;
                        case "reject":
                             messageContent = "نأسف تم رفض طلب التوصيل , ابحث عن سائقين متوفرين اخرين .";
                             Notification_fun(context,messageContent);break;
                        case "arrived":
                            messageContent = "تنويه! السائق وصل المكان الخاص بيك يا فندم , نتمنى توصيله مريحة.";
                            Notification_fun(context,messageContent);break;
                        case "Done":
                            messageContent = "شكرا لاستخدامكم EVOM للتوصيل.";
                            Notification_fun(context,messageContent);break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Notification_fun (Context context ,String messageContent){

        Intent reciveMessage = new Intent(context, MyReceiver.class)
                .setAction("request");

        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(context, 2, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("EVOM Driver app")
                .setContentText(messageContent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info")
                .setVibrate(new long[3000])
                .addAction(R.drawable.ic_baseline_done_all_24, "عرض", pendingIntentAccept);

        NotificationManager notificationManager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }
}
