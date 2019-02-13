package com.cyrilcrozes.simulboat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateScenario extends MainActivity {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenariolayout);

        Spinner spinnerEtat = (Spinner) findViewById(R.id.spinnerEtat);
        Spinner spinnerSens = (Spinner) findViewById(R.id.spinnerSens);

        ArrayAdapter<CharSequence> adapterEtat = ArrayAdapter.createFromResource(this,
                R.array.etatmer_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterSens = ArrayAdapter.createFromResource(this,
                R.array.sensnav_array, android.R.layout.simple_spinner_item);


        adapterEtat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtat.setAdapter(adapterEtat);

        adapterSens.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSens.setAdapter(adapterSens);
    }
}
