package com.example.jasonsmith.quicksortgarbage;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button takePhoto;
    ImageView imageTaken;
    Thread UIThreadImpl;

    ClarifaiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new ClarifaiBuilder(getString(R.string.api_key)).buildSync();
    }




}
