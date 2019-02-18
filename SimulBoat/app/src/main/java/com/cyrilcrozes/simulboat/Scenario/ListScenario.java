package com.cyrilcrozes.simulboat.Scenario;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.cyrilcrozes.simulboat.MainActivity;
import com.cyrilcrozes.simulboat.R;
import com.cyrilcrozes.simulboat.Scenario.Scenario;
import com.cyrilcrozes.simulboat.Scenario.ScenarioAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListScenario extends MainActivity {
    RecyclerView scenariolist;
    TextView alerte;
    private List<Scenario> scenarios = new ArrayList<>();

    // This method will read data from FileInputStream.
    private String readFromFileInputStream(FileInputStream fileInputStream)
    {
        StringBuffer retBuf = new StringBuffer();

        try {
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        }catch(IOException ex)
        {

        }finally
        {
            return retBuf.toString();
        }
    }

    private void initializeData(){

        String path = getApplicationContext().getFilesDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
            String filename = files[i].getName();
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = getApplicationContext().openFileInput(filename);
                String fileData = readFromFileInputStream(fileInputStream);
                JSONObject jsondata = new JSONObject(fileData);
                Scenario scenario = new Scenario();
                scenario.initializeWithJson(jsondata);
                scenarios.add(scenario);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
