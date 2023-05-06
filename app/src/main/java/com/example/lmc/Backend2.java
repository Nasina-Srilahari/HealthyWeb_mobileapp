package com.example.lmc;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Backend2 {

    private DatabaseReference databaseReference;

    public Backend2(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Apt_details.class.getSimpleName());
    }
    public Task<Void> add(Apt_details apt_details){
        return databaseReference.push().setValue(apt_details);
    }
}
