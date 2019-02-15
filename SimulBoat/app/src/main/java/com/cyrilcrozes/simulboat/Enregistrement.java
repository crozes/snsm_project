package com.cyrilcrozes.simulboat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Enregistrement extends CreateScenario {
    ImageView im;
    Button bArret;
    Chronometer simpleChronometer;
    String data;
    JSONObject mJsonObject;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enregistrement);

        Intent intent = getIntent();
        JSONObject data = new JSONObject();
        if(getIntent().hasExtra("json")) {
            try {
                mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
                TextView test = (TextView) findViewById(R.id.test);
                test.setText(mJsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Animation amB = AnimationUtils.loadAnimation(this,R.anim.blink_anim);
        bArret = (Button) findViewById(R.id.bArret);
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        simpleChronometer.start();

        im = (ImageView) findViewById(R.id.iEnregistrement);
        im.setImageResource(R.drawable.logo);

        amB.setDuration(1800);

        im.startAnimation(amB);

        amB.setRepeatCount(Animation.INFINITE);

        bArret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    File file = new File(getApplicationContext().getFilesDir(),mJsonObject.get("nom")+".json");
                    FileOutputStream outputStream;
                    outputStream = openFileOutput(mJsonObject.get("nom")+".json", getApplicationContext().MODE_APPEND);
                    outputStream.write(mJsonObject.toString().getBytes());
                    Log.d("INFO","Creation fichier :"+ getApplicationContext().getFilesDir()+mJsonObject.get("nom")+".json");
                    outputStream.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
