package com.cyrilcrozes.simulboat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Enregistrement extends CreateScenario implements SensorEventListener {
    ArrayList<JSONObject> listObjectJson = new ArrayList<JSONObject>();

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

    private boolean isPassed = false;


    String filename = null;
    String path = null;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.enregistrement);

        if(getIntent().hasExtra("json")) {
            try {
                mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
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
            float vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // Fail! we don't have an accelerometer!
        }

        bArret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                simpleChronometer.stop();
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                JSONObject result = new JSONObject();
                try {
                    mJsonObject.put("duree",(int) (SystemClock.elapsedRealtime() - simpleChronometer.getBase()));
                    mJsonObject.put("data", listObjectJson);
                    path = getApplicationContext().getFilesDir().toString();
                    Log.d("INFO","Path : "+path);
                    filename = mJsonObject.get("nom").toString();
                    Log.d("INFO","Creation fichier :"+ path+filename);
                    new File(path).mkdir();

                    File file = new File(path+"/"+filename+".json");
                    if (!file.exists()) {
                        file.createNewFile();
                        file.setReadable(true, false);
                        file.setWritable(true,false);
                        file.setExecutable(true,false);
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write((mJsonObject.toString().getBytes()));

                    Log.d("INFO","JSON :"+ mJsonObject.toString());
                    result.put("status","OK");
                    result.put("value","Votre scénario a bien était enregistré sous le nom : "+mJsonObject.get("nom").toString());
                } catch (JSONException e) {
                    try {
                        result.put("status","KO");
                        result.put("value","Erreur, votre scenario n'a pas été enregistré. "+e.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    try {
                        result.put("status","KO");
                        result.put("value","Erreur, votre scenario n'a pas été enregistré. "+e.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (IOException e) {
                    try {
                        result.put("status","KO");
                        result.put("value","Erreur, votre scenario n'a pas été enregistré. "+e.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                myIntent.putExtra("resultJson",result.toString());
                startActivityForResult(myIntent, 0);
                finish();
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
        new CountDownTimer(3000000, 1000) {

            public void onFinish() {
                // When timer is finished
            }

            public void onTick(long millisUntilFinished) {

                // get the change of the x,y,z values of the accelerometer
                deltaX = Math.abs(lastX - event.values[0]);
                deltaY = Math.abs(lastY - event.values[1]);
                deltaZ = Math.abs(lastZ - event.values[2]);

                try {
                    JSONObject dataFromGyroXYZ = new JSONObject();
                    dataFromGyroXYZ.put("X", deltaX);
                    dataFromGyroXYZ.put("Y", deltaY);
                    dataFromGyroXYZ.put("Z", deltaZ);
                    listObjectJson.add(dataFromGyroXYZ);
                }
                catch (Exception e){
                    Log.d("ERROR","Erreur Jsonisation des valeurs du gyro");
                }
            }
        }.start();
    }
}