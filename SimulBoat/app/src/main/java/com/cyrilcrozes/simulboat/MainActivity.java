package com.cyrilcrozes.simulboat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cyrilcrozes.simulboat.Scenario.ListScenario;

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
    }
}
