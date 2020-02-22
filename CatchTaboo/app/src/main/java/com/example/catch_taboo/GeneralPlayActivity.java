package com.example.catch_taboo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GeneralPlayActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) { //method gets triggered as soon as the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_play);
    }
}
