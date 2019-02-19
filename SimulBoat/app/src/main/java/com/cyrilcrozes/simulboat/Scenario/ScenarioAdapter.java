package com.cyrilcrozes.simulboat.Scenario;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyrilcrozes.simulboat.ClickListener;
import com.cyrilcrozes.simulboat.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioViewHolder>{

    private List<Scenario> scenarios;
    private final ClickListener listener;

    public ScenarioAdapter(List<Scenario> scenarios, ClickListener listener){
        this.scenarios = scenarios;
        this.listener = listener;
    }

    @Override
    public ScenarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ScenarioViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scenariocard, viewGroup, false), listener);
    }

    @Override
    public void onBindViewHolder(ScenarioViewHolder scenarioViewHolder, int i) {
        scenarioViewHolder.name.setText(scenarios.get(i).getName());
        scenarioViewHolder.date.setText(scenarios.get(i).getDate());
        scenarioViewHolder.etatMer.setText(scenarios.get(i).getEtatMer());
        scenarioViewHolder.sensNav.setText(scenarios.get(i).getSensNav());
        int duree = Integer.parseInt(scenarios.get(i).getDuree());
        String dureeFormat = String.format("%d min %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duree),
                TimeUnit.MILLISECONDS.toSeconds(duree) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duree))
        );
        scenarioViewHolder.duree.setText(dureeFormat);
    }

    @Override
    public int getItemCount() {
        return scenarios.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Scenario getScenario(int position){
        return this.scenarios.get(position);
    }
}
