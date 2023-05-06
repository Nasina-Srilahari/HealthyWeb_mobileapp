package com.example.lmc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerMedAdapter extends RecyclerView.Adapter<RecyclerMedAdapter.ViewHolder> {

    Context context;
    ArrayList<Prescriptions>  arrPrescriptions;
    RecyclerMedAdapter(Context context, ArrayList<Prescriptions> arrPrescriptions){

        this.context=context;
        this.arrPrescriptions = arrPrescriptions;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pres_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Prescriptions model = (Prescriptions) arrPrescriptions.get(position);

        holder.Medname.setText(arrPrescriptions.get(position).medicine);
        holder.dosage.setText(arrPrescriptions.get(position).dosage);
        holder.timing.setText(arrPrescriptions.get(position).timing);



    }

    @Override
    public int getItemCount() {
        return arrPrescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Medname,dosage,timing;
        LinearLayout presRow;

        public ViewHolder(View itemView){
            super(itemView);

            Medname = itemView.findViewById(R.id.MedName2);
            dosage = itemView.findViewById(R.id.dosage2);
            timing = itemView.findViewById(R.id.timing2);

            presRow = itemView.findViewById(R.id.presRow);

        }
    }
}

