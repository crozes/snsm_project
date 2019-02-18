package com.cyrilcrozes.simulboat;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Enregistrement extends CreateScenario implements SensorEventListener {
    JSONObject dataFromGyroXYZ = new JSONObject();
    JSONArray dataFromGyroAll = new JSONArray();

    ImageView im;
    Button bArret;
    Chronometer simpleChronometer;
    String data;
    JSONObject mJsonObject;

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 0;

    private boolean isPassed = false;


    private float currentX, currentY, currentZ;

    private String resultatJSON = "";

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enregistrement);

        Intent intent = getIntent();
        JSONObject data = new JSONObject();
        if(getIntent().hasExtra("json")) {
            try {
                mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
                resultatJSON = mJsonObject.toString();
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

        //Enregistrement données gyro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success! we have an accelerometer.
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // Fail! we don't have an accelerometer!
        }

        bArret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                simpleChronometer.stop();
                try {
                    mJsonObject.put("duree",(int) (SystemClock.elapsedRealtime() - simpleChronometer.getBase()));
                    mJsonObject.put("data", dataFromGyroAll);
                    File file = new File(getApplicationContext().getFilesDir(),mJsonObject.get("nom")+".json");
                    FileOutputStream outputStream;
                    outputStream = openFileOutput(mJsonObject.get("nom")+".json", getApplicationContext().MODE_APPEND);
                    outputStream.write(mJsonObject.toString().getBytes());
                    Log.d("INFO","Creation fichier :"+ getApplicationContext().getFilesDir()+mJsonObject.get("nom")+".json");
                    Log.d("INFO","JSON :"+ mJsonObject.toString());
                    outputStream.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                //startActivityForResult(myIntent, 0);
                //finish();
            }

        });

    }

    //onResume() register the accelerometer for listening the events
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(!isPassed){
            isPassed = true;
            onEveryTick(event);
        }
    }

    public void onEveryTick(final SensorEvent event) {
        new CountDownTimer(30000, 2000) {

            public void onFinish() {
                // When timer is finished
            }

            public void onTick(long millisUntilFinished) {

                // millisUntilFinished    The amount of time until finished.
                // display the current x,y,z accelerometer values
                displayCurrentValues();

                // get the change of the x,y,z values of the accelerometer
                deltaX = Math.abs(lastX - event.values[0]);
                deltaY = Math.abs(lastY - event.values[1]);
                deltaZ = Math.abs(lastZ - event.values[2]);
            }
        }.start();
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX = deltaX;
        currentY = deltaY;
        currentZ = deltaZ;
        try {
            dataFromGyroXYZ.put("X", currentX);
            dataFromGyroXYZ.put("Y", currentY);
            dataFromGyroXYZ.put("Z", currentZ);
            dataFromGyroAll.put(dataFromGyroXYZ);
        }
        catch (Exception e){
            Log.d("ERROR","Erreur Jsonisation des valeurs du gyro");
        }
    }
}