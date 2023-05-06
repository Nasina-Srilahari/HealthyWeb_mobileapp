package com.example.lmc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;


public class Dialog_class {
    Context context;
    Dialog dialog;

    public Dialog_class(Context context){
        this.context = context;
    }

    public void ShowDialog( String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tileTextView = dialog.findViewById(R.id.text_dialog);

        tileTextView.setText(title);
        dialog.create();
        dialog.show();
    }

    public void HideDialog(){
        dialog.dismiss();
    }

}
