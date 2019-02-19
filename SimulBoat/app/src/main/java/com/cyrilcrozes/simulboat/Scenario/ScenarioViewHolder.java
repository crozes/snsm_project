package com.cyrilcrozes.simulboat.Scenario;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cyrilcrozes.simulboat.R;

public class ScenarioViewHolder extends RecyclerView.ViewHolder {
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