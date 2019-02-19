package com.cyrilcrozes.simulboat.Scenario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cyrilcrozes.simulboat.ClickListener;
import com.cyrilcrozes.simulboat.R;

import java.io.File;
import java.lang.ref.WeakReference;

public class ScenarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView name;
    TextView date;
    TextView etatMer;
    TextView sensNav;
    TextView duree;
    Button bMod;
    Button bSupp;
    private WeakReference<ClickListener> listenerRef;

    ScenarioViewHolder(View itemView, ClickListener listener) {
        super(itemView);
        listenerRef = new WeakReference<>(listener);

        name = (TextView)itemView.findViewById(R.id.nomScenario);
        date = (TextView)itemView.findViewById(R.id.dateScenario);
        etatMer = (TextView)itemView.findViewById(R.id.tvEtatMer);
        sensNav = (TextView)itemView.findViewById(R.id.tvSensNav);
        duree = (TextView)itemView.findViewById(R.id.tvDuree);
        bMod = (Button) itemView.findViewById(R.id.bModifier);
        bSupp = (Button) itemView.findViewById(R.id.bSupprimer);

        bSupp.setOnClickListener(this);
        bMod.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == bSupp.getId()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Supprimer")
                    .setMessage("Voulez vous vraiment supprimer ce scenario ? ")
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(v.getContext().getFilesDir()+"/"+name.getText());
                            file.delete();
                        }
                    });
            builder.create().show();
            //Toast.makeText(v.getContext(), "SUPP PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

        }
        else if (v.getId() == bMod.getId()){
            Toast.makeText(v.getContext(), "MOD PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(v.getContext(), "Pouet",Toast.LENGTH_SHORT).show();
        }

        listenerRef.get().onPositionClicked(getAdapterPosition());
    }

}