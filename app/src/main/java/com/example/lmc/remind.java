package com.example.lmc;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class remind extends Fragment {

    ArrayList<Prescriptions> arrprescriptions = new ArrayList<>();
    RecyclerMedAdapter adapter_pd;


    public remind(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view4 =inflater.inflate(R.layout.fragment_remind, container, false);

        //
        Switch swn = (Switch) view4.findViewById(R.id.switchn);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String r_loginpno = pref.getString("Pnol", null);

        SharedPreferences pref2 = getContext().getSharedPreferences("MyPref2", 0); // 0 - for private mode
        SharedPreferences.Editor editor2 = pref2.edit();

        String checks = pref2.getString("check", null);

        if(checks!=null){
            swn.setChecked(true);
        }
        swn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor2.putString("check", String.valueOf(isChecked));
                    editor2.commit();
                    //Toast.makeText(getContext(),checks,Toast.LENGTH_LONG).show();
                } else {
                    editor2.clear();
                    editor2.commit();
                    //Toast.makeText(getContext(),checks,Toast.LENGTH_LONG).show();
                }
            }
        });
        //
        getMedicines(view4,"before_breakfast");
        getMedicines(view4,"after_breakfast");
        getMedicines(view4,"lunch");
        getMedicines(view4,"dinner");
        return view4;

    }

    public void getMedicines(View view4 ,String rem_dosage){

        RecyclerView recyclerView_pd = (RecyclerView) view4.findViewById(R.id.recycler_pd);
        recyclerView_pd.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String r_loginpno = pref.getString("Pnol", null);


        //Toast.makeText(getContext(),"prescriptions/"  + r_loginpno + "/" + rem_dosage,Toast.LENGTH_LONG).show();
        String[] med_array = new String[20];
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("prescriptions/"  + r_loginpno + "/" + rem_dosage);
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

                    Prescriptions pres = new Prescriptions(med_array[i],"1",rem_dosage);
                    i = i+1;


                    arrprescriptions.add(new Prescriptions(pres.medicine, pres.dosage, pres.timing));
                    adapter_pd = new RecyclerMedAdapter(view4.getContext(),arrprescriptions);
                    recyclerView_pd.setAdapter(adapter_pd);
                    //recyclerView_pd.setItemAnimator(new DefaultItemAnimator());
                }

                Toast.makeText(getContext(),"completed",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }



}



