package com.example.lmc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class takeAppointment extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    Spinner spinner_l,spinner_s;
    ImageButton imbtn2;
    ArrayList<doctors> arrdoctors_search = new ArrayList<>();
    RecyclerdoctorAdapter adapter_s;
    RecyclerView recyclerView_d;
    String flag = "no";
    AppCompatButton virtual,physical;
    Dialog_class dialog_class2;

    //
    public static Context contextOfApplication;

    //



    //book time
    public Button set_time,set_date,book;
    public EditText edit_time,edit_date;

    public Calendar c1 = Calendar.getInstance();
    public Calendar c2 = Calendar.getInstance();
    public int mYear, mMonth, mDay, mHour, mMinute,exMIN,exHour,exYear,exMonth,exDay;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_appointment);

        contextOfApplication = getApplicationContext();


        recyclerView_d = findViewById(R.id.recyclerContact_d);

        spinner_l =(Spinner) findViewById(R.id.spinner_l);
        spinner_s =(Spinner) findViewById(R.id.spinner_s);

        dialog_class2 = new Dialog_class(this);

        imbtn2 = (ImageButton) findViewById(R.id.imbtn2);

        spinner_l = findViewById(R.id.spinner_l);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.loctaion_doctor, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_l.setAdapter(adapter2);
        spinner_l.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner_s = findViewById(R.id.spinner_s);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.specialization_doctor, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_s.setAdapter(adapter1);
        spinner_s.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        imbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrdoctors_search.clear();
                String loca_spn = (String) spinner_l.getSelectedItem();
                String spec_spn = (String) spinner_s.getSelectedItem();
                //Toast.makeText(search_activity.this,category_spn + " "+ location_spn ,Toast.LENGTH_LONG).show();

                dialog_class2.ShowDialog("searching");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("doctors");
                Query query2 = databaseReference.orderByChild("location").equalTo(loca_spn);
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot user : dataSnapshot.getChildren()) {

                                doctors doc = user.getValue(doctors.class);

                                if (doc.specialization.equals(spec_spn)) {
                                    //Toast.makeText(takeAppointment.this,doc.name,Toast.LENGTH_LONG).show();
                                    //ContactModel contact = user.getValue(ContactModel.class);
                                    //Toast.makeText(announcement.this,"found", Toast.LENGTH_LONG).show();
                                    arrdoctors_search.add(new doctors(doc.name, doc.experience, doc.specialization, doc.location));
                                    adapter_s = new RecyclerdoctorAdapter(takeAppointment.this,arrdoctors_search);
                                    recyclerView_d.setAdapter(adapter_s);
                                    flag="yes";
                                }
                                else{
                                    flag = "no";
                                }
                            }
                            if (flag == "yes") {
                                 dialog_class2.HideDialog();
                            }
                            else if(flag=="no"){
                                dialog_class2.HideDialog();
                                Toast.makeText(takeAppointment.this, "no doctors in this location", Toast.LENGTH_LONG).show();
                                adapter_s = new RecyclerdoctorAdapter(takeAppointment.this,arrdoctors_search);
                                recyclerView_d.setAdapter(adapter_s);

                            }
                        }
                        else {
                            dialog_class2.HideDialog();
                            Toast.makeText(takeAppointment.this, "no doctors in this location", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        recyclerView_d.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void btn_showDialog2(View view) {
        final AlertDialog.Builder alert=new AlertDialog.Builder(takeAppointment.this);
        View mview2= getLayoutInflater().inflate(R.layout.app,null);
        alert.setView(mview2);
        final AlertDialog alertDialog2= alert.create();
        alertDialog2.setCanceledOnTouchOutside(true);

//        virtual = (AppCompatButton) mview2.findViewById(R.id.virtual2);
//        physical = (AppCompatButton) mview2.findViewById(R.id.physical2);
        alertDialog2.show();

    }

    public void book_app(View view){

        Toast.makeText(this, "book func", Toast.LENGTH_SHORT).show();

        final AlertDialog.Builder alert=new AlertDialog.Builder(takeAppointment.this);
        View mview2= getLayoutInflater().inflate(R.layout.book_doctor,null);
        alert.setView(mview2);
        final AlertDialog alertDialog2= alert.create();
        alertDialog2.setCanceledOnTouchOutside(true);

        set_date = (Button) mview2.findViewById(R.id.set_date);
        set_time = (Button) mview2.findViewById(R.id.set_time);
        edit_date = (EditText) mview2.findViewById(R.id.edit_date);
        edit_time = (EditText) mview2.findViewById(R.id.edit_time);
        book = (Button) mview2.findViewById(R.id.book);

        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                //Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(mview2.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edit_date.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                exYear = year;
                                exMonth = monthOfYear;
                                exDay = dayOfMonth;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                //Calendar c2 = Calendar.getInstance();
                mHour = c2.get(Calendar.HOUR_OF_DAY);
                mMinute = c2.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mview2.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edit_time.setText(hourOfDay + ":" + minute);
                                exMIN = minute;
                                exHour = hourOfDay;
                                //Toast.makeText(booking.this, String.valueOf(exMIN), Toast.LENGTH_SHORT).show();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                int delay1 = (int) c.getTimeInMillis();
                c.set(exYear,exMonth,exDay,exHour,exMIN ,0);
                int ex = (int) c.getTimeInMillis();
                long delay = (long) ex - (long) delay1;

                Toast.makeText(takeAppointment.this, delay + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), adapter_s.pos+"", Toast.LENGTH_SHORT).show();


                Intent notificationIntent = new Intent(takeAppointment.this, NotificationAppointment.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(takeAppointment.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                long futureInMillis =System.currentTimeMillis() + delay;
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);





            }
        });

        alertDialog2.show();
    }

    public void display(){
        Toast.makeText(takeAppointment.this, adapter_s.pos+"", Toast.LENGTH_SHORT).show();
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}