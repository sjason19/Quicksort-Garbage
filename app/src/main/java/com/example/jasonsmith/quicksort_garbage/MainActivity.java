package com.example.jasonsmith.quicksort_garbage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.ClarifaiRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
public class MainActivity extends AppCompatActivity {

    ClarifaiClient client;
    Button sortIt;
    ImageView localImage;
    Thread UIThreadImpl;
    ArrayList<String> list = new ArrayList<String>() {{
        add("food");
        add("vegetables");
        add("fruit");
    }};

    // Make this static because we only want a single instance of tags per photo
    static ArrayList<String> confidenceTags;

    private static final int REQUEST_TIME = 1000;

    private static final String COMPOST = "compost";
    private static final String RECYCLING = "recycling";
    private static final String GARBAGE = "garbage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new ClarifaiBuilder(getString(R.string.api_key)).buildSync();
        UIThreadImpl = new Thread();

        sortIt = (Button) findViewById(R.id.button);
        //TODO: Edit the imageview path
        localImage = (ImageView) findViewById(R.id.imageView);
        sortIt.setOnClickListener(new PhotoTaker());

    }

    class PhotoTaker implements Button.OnClickListener{

        @Override
        public void onClick(View v){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_TIME);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_TIME && resultCode == RESULT_OK) {   //if the result is from the camera and it worked
            Bitmap bmp = (Bitmap) data.getExtras().get("data");         //get the data of the picture
            new Connection().execute(bmp);
            new UIThreadImpl().execute();
        }
    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            if (! (arg0[0] instanceof Bitmap)){
                return false;
            }
            Bitmap bmp = (Bitmap)arg0[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            ClarifaiResponse response = client.getDefaultModels().generalModel().predict()
                    .withInputs(ClarifaiInput.forImage(byteArray)).executeSync();
            return null;
        }
    }


    public String getGarbageCategory(ArrayList<String> confidenceTags) {
        for (int i = 0; i < 3; i++) {
            if (confidenceTags.get(i).equals("food") ||
                    confidenceTags.get(i).equals("fruit") ||
                    confidenceTags.get(i).equals("vegetables") ||
                    confidenceTags.get(i).equals("grains")) {
                localImage.setImageResource(R.drawable.compost);
                return COMPOST;
            } else if (confidenceTags.get(i).equals("paper") ||
                    confidenceTags.get(i).equals("metal") ||
                    confidenceTags.get(i).equals("glass") ||
                    confidenceTags.get(i).equals("plastic") ||
                    confidenceTags.get(i).equals("bottle")) {
                localImage.setImageResource(R.drawable.recycling);
                return RECYCLING;
            }
        }
        localImage.setImageResource(R.drawable.trashcan);
        return GARBAGE;
    }

    private class UIThreadImpl extends AsyncTask {
        protected Object doInBackground(Object... arg0) {
            String imageSelector = getGarbageCategory(tagNames);
            Drawable d;
            String url = "";
            switch (GARBAGE) {
                case GARBAGE:
                    d = getDrawable(R.drawable.trashcan);
                case RECYCLING:
                    d = getDrawable(R.drawable.recycling);
                case COMPOST:
                    d = getDrawable(R.drawable.compost);
            }
            return null;
        }
    }
}
