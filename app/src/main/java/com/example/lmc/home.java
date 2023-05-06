package com.example.lmc;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class home extends Fragment {
    View view5;
    ImageButton btn_logout;
    AppCompatButton virtual;
    AppCompatButton physical;
    AppCompatButton appoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view5 = inflater.inflate(R.layout.fragment_home, container, false);
        return view5;

    }
}