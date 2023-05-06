package com.example.lmc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAppoint extends RecyclerView.Adapter<RecyclerAppoint.ViewHolder>{

    Context context;
    ArrayList<Apt_details> arrAppoint;
    RecyclerAppoint(Context context, ArrayList<Apt_details> arrAppoint){

        this.context=context;
        this.arrAppoint = arrAppoint;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAppoint.ViewHolder holder, int position) {

        Apt_details model = (Apt_details) arrAppoint.get(position);

        holder.docname.setText(arrAppoint.get(position).apt_doctor);
        holder.Date_app.setText(arrAppoint.get(position).apt_date);
        holder.time_app.setText(arrAppoint.get(position).apt_time);



    }

    @Override
    public int getItemCount() {
        return arrAppoint.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView docname,Date_app,time_app;
        LinearLayout AppRow;

        public ViewHolder(View itemView){
            super(itemView);

            docname = itemView.findViewById(R.id.docName2);
            Date_app = itemView.findViewById(R.id.Date_app2);
            time_app = itemView.findViewById(R.id.time_app2);

            AppRow = itemView.findViewById(R.id.AppRow);

        }
    }
}
