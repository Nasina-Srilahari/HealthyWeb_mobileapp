package com.example.lmc;

import android.app.AlarmManager;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerdoctorAdapter extends RecyclerView.Adapter<RecyclerdoctorAdapter.ViewHolder> {

    Context context;
    ArrayList<doctors>  arrdoctors;
    public int pos;
    takeAppointment ta = new takeAppointment();
    Backend2 bd = new Backend2();
    Apt_details apt_details;

   //
   Context applicationContext = takeAppointment.getContextOfApplication();

    //

    //
    public Button set_time,set_date,book;
    public EditText edit_time,edit_date;

    public Calendar c1 = Calendar.getInstance();
    public Calendar c2 = Calendar.getInstance();
    public int mYear, mMonth, mDay, mHour, mMinute,exMIN,exHour,exYear,exMonth,exDay;
    SharedPreferences pref =applicationContext.getSharedPreferences("MyPref", 0);

    //
    RecyclerdoctorAdapter(Context context, ArrayList<doctors> arrdoctors){

        this.context=context;
        this.arrdoctors= arrdoctors;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        doctors model = (doctors) arrdoctors.get(position);

        holder.name.setText(arrdoctors.get(position).name);
        holder.specialization.setText(arrdoctors.get(position).experience);
        holder.experience.setText(arrdoctors.get(position).specialization);
        holder.location.setText(arrdoctors.get(position).location);

        pos = position;
        Toast.makeText(context, "entered pos", Toast.LENGTH_SHORT).show();

        holder.physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
                pos = position;

                final androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(context);
                LayoutInflater li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                View mview2= li.inflate(R.layout.book_doctor,null);
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

                        Toast.makeText(context, delay + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), adapter_s.pos+"", Toast.LENGTH_SHORT).show();


                        Intent notificationIntent = new Intent(context, NotificationAppointment.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        long futureInMillis =System.currentTimeMillis() + delay;
                        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);


                        String date = exYear + "-" + exMonth + "-" + exDay;
                        String time = exHour + ":" + exMIN;
                        String doctor_name = arrdoctors.get(position).name;
                        String phoneNo =pref.getString("Pnol", null);


                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Apt_details");
                        Query query2 = databaseReference.orderByChild("apt_pat_no").equalTo(phoneNo);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                        apt_details = new Apt_details(doctor_name , date, time,phoneNo);
                                        bd.add(apt_details).addOnSuccessListener(suc ->{
                                            Toast.makeText(context, "successfull", Toast.LENGTH_LONG).show();
                                        }).addOnFailureListener(er ->{
                                            Toast.makeText(context, ""+er.getMessage(), Toast.LENGTH_LONG).show();
                                        });


                                }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, " " + error, Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

                alertDialog2.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrdoctors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,experience,specialization,location;
        ImageView img;
        LinearLayout llRow;
        AppCompatButton physical,virtual;
        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.dsName2);
            experience = itemView.findViewById(R.id.dsexp2);
            specialization = itemView.findViewById(R.id.dssp2);
            location = itemView.findViewById(R.id.dsploc2);
            physical = (AppCompatButton) itemView.findViewById(R.id.physical2);
            virtual = (AppCompatButton) itemView.findViewById(R.id.virtual2);
            llRow = itemView.findViewById(R.id.llRow);

        }

    }



}

