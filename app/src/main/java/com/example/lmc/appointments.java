package com.example.lmc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class appointments extends Fragment {
    View view;
    ArrayList<Apt_details> arrAppoint = new ArrayList<>();
    RecyclerAppoint adapter_app;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointments, container, false);
        getappoint(view);



        return view;
    }

    public void getappoint(View view){

        RecyclerView recyclerView_app = (RecyclerView) view.findViewById(R.id.recycler_app);
        recyclerView_app.setLayoutManager(new LinearLayoutManager(getActivity()));

//        RecyclerView recyclerView_pd = (RecyclerView) view4.findViewById(R.id.recycler_pd);
//        recyclerView_pd.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String r_loginpno = pref.getString("Pnol", null);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Apt_details");
        Query query = databaseReference.orderByChild("apt_pat_no").equalTo(r_loginpno);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot apt_details : dataSnapshot.getChildren()) {

                        Apt_details usersBean = apt_details.getValue(Apt_details.class);
                        //Toast.makeText(login.this, usersBean.user_password, Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), usersBean.apt_doctor+" ", Toast.LENGTH_SHORT).show();
                        Apt_details apt = new Apt_details(usersBean.apt_doctor, usersBean.apt_date, usersBean.apt_time, usersBean.apt_pat_no);

                        arrAppoint.add(new Apt_details(apt.apt_doctor, apt.apt_date, apt.apt_time, apt.apt_pat_no));
                        adapter_app = new RecyclerAppoint(view.getContext(),arrAppoint);
                        recyclerView_app.setAdapter(adapter_app);
                    }

                } else {
                    Toast.makeText(getContext(), "not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}