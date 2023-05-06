package com.example.lmc;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class NotificationAppointment extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {

            @SuppressLint("WrongConstant")
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(context ,dashboard.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder builder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                builder = new Notification.Builder(context.getApplicationContext(),
                        "com.offline.english.dictionary.speech.translator");
            }
            else {
                builder = new Notification.Builder(context);
            }

            builder.setContentTitle("Notification from HealthyWeb");
            builder.setContentText("you have an appointment with your doctor");
            builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setSmallIcon(R.drawable.logo);
            builder.setAutoCancel(true);
            builder.setContentIntent(resultPendingIntent);


            builder.build().defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "My Background Service";
                NotificationChannel chan = new NotificationChannel("com.offline.english.dictionary.speech.translator",
                        channelName, NotificationManager.IMPORTANCE_HIGH);
                chan.setLightColor(Color.BLUE);
                chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(chan);


                chan.enableLights(true);
                chan.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                chan.enableVibration(true);

            }
            //notificationManager.notify(1, notifyBuilder.build());
            notificationManager.notify(1, builder.build());

    }

}
