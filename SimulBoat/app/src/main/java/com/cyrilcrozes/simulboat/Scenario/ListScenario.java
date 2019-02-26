package com.cyrilcrozes.simulboat.Scenario;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.cyrilcrozes.simulboat.ClickListener;
import com.cyrilcrozes.simulboat.MainActivity;
import com.cyrilcrozes.simulboat.R;

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
    RecyclerView rvScenariosList;
    TextView tvAlerte;
    private List<Scenario> listScenarios = new ArrayList<>();
    ScenarioAdapter scenarioAdapter;

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
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            return retBuf.toString();
        }
    }

    private void initializeData(){
        String directoryPath = getApplicationContext().getExternalFilesDir(null).toString();
        File directory = new File(directoryPath);
        File[] tabFiles = directory.listFiles();
        Log.d("INFO", "Fichier présent dans le dossier : ");
        for (File file : tabFiles) {
            Log.d("INFO", file.getName());
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                String fileData = readFromFileInputStream(fileInputStream);
                JSONObject jsonData = new JSONObject(fileData);
                Scenario scenario = new Scenario();
                scenario.initializeWithJson(jsonData);
                listScenarios.add(scenario);
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
        tvAlerte = (TextView) findViewById(R.id.tvAlerteScenarioList);
        initializeData();
        if (listScenarios.isEmpty()){
            tvAlerte.setText("Pas de scenario enregistré");
        }
        else
        {
            rvScenariosList = (RecyclerView) findViewById(R.id.rv);
            rvScenariosList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rvScenariosList.setLayoutManager(llm);
            scenarioAdapter = new ScenarioAdapter(listScenarios, new ClickListener() {
                @Override public void onPositionClicked(int position) {

                }
            });
            rvScenariosList.setAdapter(scenarioAdapter);
        }
    }

    public void onResume() {
        super.onResume();
    }
}
