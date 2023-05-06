package com.example.lmc;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class diet extends Fragment {
    View view3;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Button retreive;
    EditText image_name;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view3 = inflater.inflate(R.layout.fragment_diet, container, false);

        image = (ImageView) view3.findViewById(R.id.imageView2) ;

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String d_loginpno = pref.getString("Pnol", null);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("fetching image");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //String image_string = image_name.getText().toString();

        String image_string = "a";
        storageReference = FirebaseStorage.getInstance().getReference("images/"+d_loginpno+"diet.jpg");

        try {
            File localfile = File.createTempFile("tempFile",".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();

                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                image.setImageBitmap(bitmap);

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(), "failed to retreive", Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();

                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view3;
    }
}