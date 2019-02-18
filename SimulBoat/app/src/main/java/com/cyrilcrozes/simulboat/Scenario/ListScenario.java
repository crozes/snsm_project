package com.cyrilcrozes.simulboat.Scenario;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cyrilcrozes.simulboat.MainActivity;
import com.cyrilcrozes.simulboat.R;
import com.cyrilcrozes.simulboat.Scenario.Scenario;
import com.cyrilcrozes.simulboat.Scenario.ScenarioAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListScenario extends MainActivity {
    RecyclerView scenariolist;
    TextView alerte;
    private List<Scenario> scenarios = new ArrayList<>();

    private void initializeData(){
        scenarios.add(new Scenario("Jour Tempete", "01/12/1232","0 - Calme","Mer de face","00:12"));
        scenarios.add(new Scenario("Jour Calme", "02/12/1232","1 - Agité","Mer de dos","02:39"));
        scenarios.add(new Scenario("Ça va bouger bébé", "03/12/1232","2 - Tempete","Mer de coté","32:12"));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenariolist);

        alerte = (TextView) findViewById(R.id.tvAlerteScenarioList);

        initializeData();

        if (scenarios.isEmpty()){
            alerte.setText("Pas de scenario enregistrés");
        }
        else
        {
            scenariolist = (RecyclerView) findViewById(R.id.rv);
            scenariolist.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            scenariolist.setLayoutManager(llm);

            ScenarioAdapter adapter = new ScenarioAdapter(scenarios);
            scenariolist.setAdapter(adapter);
        }
    }
}
