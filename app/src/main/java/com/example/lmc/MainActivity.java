package com.example.lmc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btn_register;
    EditText name,phoneno,setpass,conpass;
    CountryCodePicker ccp;
    Backend bd = new Backend();
    String flag_exist = "no";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        btn_register = (AppCompatButton) findViewById(R.id.btn_register);
        name = (EditText) findViewById(R.id.name);
        phoneno = (EditText) findViewById(R.id.pno);
        setpass = (EditText) findViewById(R.id.setp);
        conpass = (EditText) findViewById(R.id.conp);
        ccp=(CountryCodePicker)findViewById(R.id.ccp) ;
        ccp.registerCarrierNumberEditText(phoneno);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");
        Query query = databaseReference.orderByChild("user_phoneno").equalTo(phoneno.getText().toString().trim());

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, login.class);
                //startActivity(intent);

                String names = name.getText().toString().trim();
                String phonenos = phoneno.getText().toString().trim();
                String setpasss = setpass.getText().toString().trim();
                String conpasss = conpass.getText().toString().trim();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("User");
                Query query2 = databaseReference.orderByChild("user_phoneno").equalTo(phoneno.getText().toString().trim());
                query2.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Toast.makeText(MainActivity.this, "user already exist with this number", Toast.LENGTH_LONG).show();
                            name.setText("");
                            phoneno.setText("");
                            setpass.setText("");
                            conpass.setText("");

                        } else {
                            if(names.isEmpty()){
                                name.setError("Please enter your name");
                                name.requestFocus();
                            }

                            if(phonenos.isEmpty()){
                                phoneno.setError("Please enter your phone number");
                                phoneno.requestFocus();
                            }

                            if(setpasss.isEmpty()){
                                setpass.setError("Please set your password");
                                setpass.requestFocus();
                            }

                            if(conpasss.isEmpty()){
                                conpass.setError("Please enter password again");
                                conpass.requestFocus();
                            }

                            if(!setpasss.equals(conpasss)){
                                conpass.setError("password did not match");
                                conpass.requestFocus();
                            }



                            else{
                                otpsend();

                                User user = new User(names , phonenos, setpasss);
                                bd.add(user).addOnSuccessListener(suc ->{
                                    Toast.makeText(MainActivity.this, "successfull", Toast.LENGTH_LONG).show();
                                }).addOnFailureListener(er ->{
                                    Toast.makeText(MainActivity.this, ""+er.getMessage(), Toast.LENGTH_LONG).show();
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, " " + error, Toast.LENGTH_LONG).show();

                    }
                });

                //Toast.makeText(MainActivity.this,flag_exist,Toast.LENGTH_LONG).show();


            }
        });
    }

    private void otpsend() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent intent = new Intent(MainActivity.this,manageotp.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().trim());
                intent.putExtra("verificationId",verificationId);
                startActivity(intent);

            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(ccp.getFullNumberWithPlus().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}