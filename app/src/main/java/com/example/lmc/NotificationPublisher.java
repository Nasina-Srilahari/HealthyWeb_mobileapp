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

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String str_dosage = "";
    public String names = "";
    public static String str_pno = "";
    public static String ret_keys_array = "";


    public void onReceive(Context context, Intent intent) {

        //String ret_dosage = intent.getStringExtra(str_dosage);
        //String ret_pno = intent.getStringExtra(str_pno);

        Bundle extras = intent.getExtras();
        String[] arrayB = extras.getStringArray(ret_keys_array);

        String ret_dosage = arrayB[0];
        String ret_pno = arrayB[1];

        boolean net = checkConnection(context);
        Toast.makeText(context,String.valueOf(net),Toast.LENGTH_LONG).show();

        if(net == false){
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

            builder.setContentTitle("Scheduled Notification");
            builder.setContentText("you have to take medicine");
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


        if(net == true){
            String[] med_array = new String[20];
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("prescriptions/"  + ret_pno + "/" + ret_dosage);
            Toast.makeText(context,"prescriptions/"  + ret_pno + "/" + ret_dosage,Toast.LENGTH_LONG).show();
            Query query = databaseReference.orderByChild("medicine");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        String med = (String) next.child("medicine").getValue();
                        med_array[i] = med;
                        i = i+1;
                    }
                    medName(med_array);

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

                    builder.setContentTitle("Scheduled Notification");
                    builder.setContentText(names);
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
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void medName( String[] rmed_array){
        for(int i=0;i< rmed_array.length;i++){
            if(rmed_array[i]==null){
                break;
            }
            names = names + " " + rmed_array[i];

        }
    }

    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }
}
