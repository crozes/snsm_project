package com.cyrilcrozes.simulboat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Enregistrement extends CreateScenario implements SensorEventListener {
    JSONArray listObjectJson = new JSONArray();

    ImageView imageView;
    Button bArret;
    Chronometer chronometer;
    JSONObject mJsonObject;

    float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private boolean isPassed = false;


    String filename = null;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.enregistrement);

        /** Récupération des données de la page précédente */
        if(getIntent().hasExtra("json")) {
            try {
                mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /** Création de l'annimation du bateau */
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.blink_anim);
        bArret = (Button) findViewById(R.id.bArret);
        chronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        chronometer.start();

        imageView = (ImageView) findViewById(R.id.iEnregistrement);
        imageView.setImageResource(R.drawable.vedette);

        animation.setDuration(1800);

        imageView.startAnimation(animation);

        animation.setRepeatCount(Animation.INFINITE);


        /** Enregistrement */
        Log.d("INFO","Enregistrement des données du gyroscope ->");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.d("INFO","Accelerometre present");
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.d("ERROR","Pas d'acceleromettre");
        }

        /** Action boutton Arret */
        bArret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                chronometer.stop();
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                JSONObject result = new JSONObject();
                try {
                    filename = mJsonObject.get("nom").toString();
                    File scenarioDir =  new File (getApplicationContext().getExternalFilesDir(null).toString());
                    if(!scenarioDir.exists())
                        scenarioDir.mkdirs();
                    File file = new File(scenarioDir, filename+".json");
                    Log.d("INFO","Creation fichier :"+ file.getAbsolutePath());
                    if (!file.exists()) {
                        file.createNewFile();
                        file.setReadable(true, false);
                        file.setWritable(true,false);
                        file.setExecutable(true,false);
                    }

                    mJsonObject.put("duree",(int) (SystemClock.elapsedRealtime() - chronometer.getBase()));
                    mJsonObject.put("data", listObjectJson);
                    Log.d("INFO","JSON :"+ mJsonObject.toString());

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(mJsonObject.toString().getBytes());

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

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

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
                Log.d("INFO","Fin du de l'enregistrement - Fin du timer");
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
                    listObjectJson.put(dataFromGyroXYZ);
                }
                catch (Exception e){
                    Log.d("ERROR","Erreur Jsonisation des valeurs du gyro");
                }
            }
        }.start();
    }
}