package com.example.lmc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class login extends AppCompatActivity{

    AppCompatButton btn_login;
    TextView txt_register;
    EditText pnol , pass;
    CountryCodePicker ccp2;
    String flag = "0";
    Dialog_class dialog_class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_register = findViewById(R.id.txt_register);
        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        pnol = (EditText) findViewById(R.id.pnol);
        pass = (EditText) findViewById(R.id.pass);
        ccp2=(CountryCodePicker)findViewById(R.id.ccp2) ;
        ccp2.registerCarrierNumberEditText(pnol);

        dialog_class = new Dialog_class(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        String loginpno = pref.getString("Pnol", null);
        String loginpass = pref.getString("Pass", null);

        System.out.println(loginpno);

        //Toast.makeText(login.this ,loginpno+" "+loginpass, Toast.LENGTH_LONG).show();

        if(loginpno!=null && loginpass!=null ){
            Intent intent = new Intent(login.this, dashboard.class);
            startActivity(intent);
        }


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");
        Query query = databaseReference.orderByChild("user_phoneno").equalTo(pnol.getText().toString().trim());

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,MainActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("User");
                Query query = databaseReference.orderByChild("user_phoneno").equalTo(pnol.getText().toString().trim());
                dialog_class.ShowDialog("login");

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot user : dataSnapshot.getChildren()) {

                                User usersBean = user.getValue(User.class);
                                //Toast.makeText(login.this, usersBean.user_password, Toast.LENGTH_LONG).show();

                                if (usersBean.user_password.equals(pass.getText().toString().trim())) {

                                    flag ="1";
                                    break;
                                }
                                //Toast.makeText(login.this,flag,Toast.LENGTH_LONG).show();
                            }

                            if(flag == "1"){

                                editor.putString("Pnol", pnol.getText().toString().trim()); // Storing string
                                editor.putString("Pass", pass.getText().toString().trim()); // Storing string
                                editor.commit();


                                Intent intent = new Intent(login.this, dashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                dialog_class.HideDialog();
                            }
                            else if(flag == "0"){
                                Toast.makeText(login.this, "Password is wrong", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(login.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}