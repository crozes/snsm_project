package com.cyrilcrozes.simulboat.Scenario;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyrilcrozes.simulboat.R;

import java.util.List;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder>{

    public static class ScenarioViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView date;
        TextView etatMer;
        TextView sensNav;
        TextView duree;

        ScenarioViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.nomScenario);
            date = (TextView)itemView.findViewById(R.id.dateScenario);
            etatMer = (TextView)itemView.findViewById(R.id.tvEtatMer);
            sensNav = (TextView)itemView.findViewById(R.id.tvSensNav);
            duree = (TextView)itemView.findViewById(R.id.tvDuree);
        }
    }

    private List<Scenario> scenarios;

    ScenarioAdapter(List<Scenario> scenarios){
        this.scenarios = scenarios;
    }

    @Override
    public ScenarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scenariocard, viewGroup, false);
        ScenarioViewHolder pvh = new ScenarioViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ScenarioViewHolder scenarioViewHolder, int i) {
        scenarioViewHolder.name.setText(scenarios.get(i).getName());
        scenarioViewHolder.date.setText(scenarios.get(i).getDate());
        scenarioViewHolder.etatMer.setText(scenarios.get(i).getEtatMer());
        scenarioViewHolder.sensNav.setText(scenarios.get(i).getSensNav());
        scenarioViewHolder.duree.setText(scenarios.get(i).getDuree());
    }

    @Override
    public int getItemCount() {
        return scenarios.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
