package com.example.jasonsmith.quicksort_garbage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;

public class MainActivity extends AppCompatActivity {

    ClarifaiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new ClarifaiBuilder(getString(R.string.api_key)).buildSync();

    }
}
