package com.example.lmc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity {
    EditText otp;
    Button btn_verify;
    String verificationId;
    FirebaseAuth mAuth;
    String phonenumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);

        phonenumber = getIntent().getStringExtra("mobile").toString();
        otp = (EditText) findViewById(R.id.otp);
        btn_verify=(Button) findViewById(R.id.btn_verify);

        verificationId = getIntent().getStringExtra("verificationId");

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificationId!= null){
                    String code = otp.getText().toString().trim();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

                    FirebaseAuth
                            .getInstance()
                            .signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(manageotp.this, "registration successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(manageotp.this,login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(manageotp.this,"otp invalid" , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }


}