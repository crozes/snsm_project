package com.cyrilcrozes.simulboat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateScenario extends MainActivity {

    EditText name;
    Spinner spinnerEtat;
    Spinner spinnerSens;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenariolayout);

        spinnerEtat = (Spinner) findViewById(R.id.spinnerEtat);
        spinnerSens = (Spinner) findViewById(R.id.spinnerSens);

        name = (EditText) findViewById(R.id.editText);

        ArrayAdapter<CharSequence> adapterEtat = ArrayAdapter.createFromResource(this,
                R.array.etatmer_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterSens = ArrayAdapter.createFromResource(this,
                R.array.sensnav_array, android.R.layout.simple_spinner_item);


        adapterEtat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtat.setAdapter(adapterEtat);

        adapterSens.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSens.setAdapter(adapterSens);

        Button button = findViewById(R.id.bEnregistrement);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(name.getText().toString().equals("")){
                    name.setError("Merci de renseigner un nom");
                }
                else{
                    Intent myIntent = new Intent(v.getContext(), Enregistrement.class);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    myIntent.putExtra("data","{\"nom\":\""+name.getText().toString()+"\",\"date\":\""+dateFormat.format(date)+"\",\"etat\":\""+spinnerEtat.getSelectedItem().toString()+"\",\"navigation\":\""+spinnerSens.getSelectedItem().toString()+"\"}");
                    startActivityForResult(myIntent, 0);
                }
            }
        });
    }
}
