package com.example.lmc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class dashboard extends AppCompatActivity {

    ImageButton btn_logout;
    AppCompatButton virtual;
    AppCompatButton physical;
    AppCompatButton appoint, med_his, meet_doc;
    FrameLayout frame ;
    public String names = "";
    public String[] keys_array = new String[2];

    LinearLayout llf;
    ImageButton home,remind,diet,exercise,app_taken;
    TextView fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);





        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        SharedPreferences pref3 = getSharedPreferences("MyPref3", 0);
        SharedPreferences.Editor editor3 = pref3.edit();

        String enter_flg = pref3.getString("already_entered", null);

//       editor3.clear();
//       editor3.commit();


        btn_logout = (ImageButton) findViewById(R.id.btn_logout);
        appoint = (AppCompatButton) findViewById(R.id.appoint);
        med_his = (AppCompatButton) findViewById(R.id.med_his);
        meet_doc = (AppCompatButton) findViewById(R.id.meet_doc);


        home = (ImageButton) findViewById(R.id.home);
        remind = (ImageButton) findViewById(R.id.remind);
        diet = (ImageButton) findViewById(R.id.diet);
        exercise = (ImageButton) findViewById(R.id.exercise);
        app_taken = (ImageButton) findViewById(R.id.app_taken);

        llf = findViewById(R.id.llf);

        SharedPreferences pref2 = getSharedPreferences("MyPref2", 0);
        SharedPreferences.Editor editor2 = pref2.edit();

        String loginpno = pref.getString("Pnol", null);

        String checks = pref2.getString("check", null);

        //

        fullname = (TextView) findViewById(R.id.fullname);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");
        Query query = databaseReference.orderByChild("user_phoneno").equalTo(loginpno);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {

                        User usersBean = user.getValue(User.class);
                        //Toast.makeText(login.this, usersBean.user_password, Toast.LENGTH_LONG).show();

                        fullname.setText(usersBean.user_name);
                    }

                } else {
                    Toast.makeText(dashboard.this, "User not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //


        if(checks!=null && enter_flg == null){
            notification1("after_breakfast",loginpno);
        }

        if(enter_flg == null){
            editor3.putString("already_entered", "yes");
            editor3.commit();

        }




        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();

                Intent intent = new Intent(dashboard.this,login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

        });

        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this,takeAppointment.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llf.setVisibility(View.VISIBLE);
                appoint.setVisibility(View.VISIBLE);
                med_his.setVisibility(View.VISIBLE);
                meet_doc.setVisibility(View.VISIBLE);
                replaceFragment(new home());

            }
        });

        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llf.setVisibility(View.INVISIBLE);
                appoint.setVisibility(View.INVISIBLE);
                med_his.setVisibility(View.INVISIBLE);
                meet_doc.setVisibility(View.INVISIBLE);
                replaceFragment(new remind());

            }
        });

        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llf.setVisibility(View.INVISIBLE);
                appoint.setVisibility(View.INVISIBLE);
                med_his.setVisibility(View.INVISIBLE);
                meet_doc.setVisibility(View.INVISIBLE);
                replaceFragment(new diet());
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llf.setVisibility(View.INVISIBLE);
                appoint.setVisibility(View.INVISIBLE);
                med_his.setVisibility(View.INVISIBLE);
                meet_doc.setVisibility(View.INVISIBLE);
                replaceFragment(new exercise());
            }
        });

        app_taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llf.setVisibility(View.INVISIBLE);
                appoint.setVisibility(View.INVISIBLE);
                med_his.setVisibility(View.INVISIBLE);
                meet_doc.setVisibility(View.INVISIBLE);
                replaceFragment(new appointments());
            }
        });




    }
    public void btn_showDialog(View view) {
        final AlertDialog.Builder alert=new AlertDialog.Builder(dashboard.this);
        View mview= getLayoutInflater().inflate(R.layout.custom_dialog,null);
        alert.setView(mview);
        final AlertDialog alertDialog= alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void btn_showDialog2(View view) {
        final AlertDialog.Builder alert=new AlertDialog.Builder(dashboard.this);
        View mview2= getLayoutInflater().inflate(R.layout.app,null);
        alert.setView(mview2);
        final AlertDialog alertDialog2= alert.create();
        alertDialog2.setCanceledOnTouchOutside(true);

        virtual = (AppCompatButton) mview2.findViewById(R.id.virtual);
        physical = (AppCompatButton) mview2.findViewById(R.id.physical);

        virtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        alertDialog2.show();
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void scheduleNotification(long delay, String dosage, String loginpno) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);

        keys_array[0]=dosage;
        keys_array[1]=loginpno;


        notificationIntent.putExtra(NotificationPublisher.ret_keys_array, keys_array);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis =System.currentTimeMillis() + delay;

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis,AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        Toast.makeText(dashboard.this,"entered schedule",Toast.LENGTH_LONG).show();
    }

    private void notification1(String dosage , String loginpno){
        Calendar c = Calendar.getInstance();
        int delay1 = (int) c.getTimeInMillis();
        c.set(2022,06,21,12,9 ,0);
        int ex = (int) c.getTimeInMillis();
        long delay = (long) ex - (long) delay1;
        Toast.makeText(dashboard.this,delay+"",Toast.LENGTH_LONG).show();

        scheduleNotification(delay, dosage, loginpno);
    }

    private void notification2(String dosage , String loginpno){
        Calendar c = Calendar.getInstance();
        int delay1 = (int) c.getTimeInMillis();
        c.set(2022,04,31,19,03 ,0);
        int ex = (int) c.getTimeInMillis();
        long delay = (long) ex - (long) delay1;

        scheduleNotification(delay, dosage, loginpno);
    }

    private void notification3(String dosage , String loginpno){
        Calendar c = Calendar.getInstance();
        int delay1 = (int) c.getTimeInMillis();
        c.set(2022,06,21,11,31 ,0);
        int ex = (int) c.getTimeInMillis();
        long delay = (long) ex - (long) delay1;

        scheduleNotification(delay, dosage, loginpno);
    }

    private void notification4(String dosage , String loginpno){
        Calendar c = Calendar.getInstance();
        int delay1 = (int) c.getTimeInMillis();
        c.set(2022,04,31,19,03 ,0);
        int ex = (int) c.getTimeInMillis();
        long delay = (long) ex - (long) delay1;

        scheduleNotification(delay, dosage, loginpno);
    }

}