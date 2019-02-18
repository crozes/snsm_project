package com.cyrilcrozes.simulboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cyrilcrozes.simulboat.Scenario.ListScenario;

import org.json.JSONException;
import org.json.JSONObject;

import static java.util.jar.Pack200.Packer.ERROR;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createScenario = findViewById(R.id.bScenario);
        createScenario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), CreateScenario.class);
                startActivityForResult(myIntent, 0);

            }
        });

        Button listScenario = findViewById(R.id.bScenarioList);
        listScenario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ListScenario.class);
                startActivityForResult(myIntent, 0);

            }
        });

        Log.d(ERROR, "Pas entrerr");
        if(getIntent().hasExtra("Result")) {
            Log.d(ERROR, "Entrerr");
            String resultValue = new String(getIntent().getStringExtra("Result"));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Alerte");
            if(resultValue != "KO"){
                builder.setMessage("Votre scénario a bien était enregistré sous le nom : " + resultValue);
            }
            else {
                builder.setMessage("Erreur, votre scenario n'a pas été enregistré.");

            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
