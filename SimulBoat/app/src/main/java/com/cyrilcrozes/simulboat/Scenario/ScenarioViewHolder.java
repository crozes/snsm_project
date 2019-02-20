package com.cyrilcrozes.simulboat.Scenario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public void onClick(View view) {
        if (view.getId() == bSupp.getId()) {
            suppFunction(view);
        }
        else if (view.getId() == bMod.getId()){
            modFunction(view);
            //Toast.makeText(view.getContext(), "MOD PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(v.getContext(), "Pouet",Toast.LENGTH_SHORT).show();
        }

        listenerRef.get().onPositionClicked(getAdapterPosition());
    }

    private void suppFunction(final View v){
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
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        JSONObject result = new JSONObject();
                        try {
                            result.put("status","OK");
                            result.put("value",name.getText()+" a été supprimé");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myIntent.putExtra("resultJson",result.toString());
                        v.getContext().startActivity(myIntent);
                    }
                });
        builder.create().show();
    }

    private void modFunction (final View v) {
        // File
        Context ctx = v.getContext();
        JSONObject dataGet = null;
        String oldName = null;
        try {
            FileInputStream fileInputStream = ctx.openFileInput(name.getText().toString());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineData = bufferedReader.readLine();
            dataGet = new JSONObject(lineData);
            oldName = dataGet.get("nom").toString();
        }
        catch (Exception e){
            Log.d("ERROR","Erreur : "+e.toString());
        }

        // Dialog Custom
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.modifiercard);
        dialog.setTitle("Modifier");

        final EditText ed = (EditText) dialog.findViewById(R.id.etName);
        final Spinner spEtatmer = dialog.findViewById(R.id.spinnerEtatMer);
        final Spinner spSensNav = dialog.findViewById(R.id.spinnerSensNav);
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        Button buttonAnnuler = (Button) dialog.findViewById(R.id.buttonAnnuler);

        try {
            ed.setText(dataGet.get("nom").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<CharSequence> adapterEtat = ArrayAdapter.createFromResource(v.getContext(),
                R.array.etatmer_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSens = ArrayAdapter.createFromResource(v.getContext(),
                R.array.sensnav_array, android.R.layout.simple_spinner_item);

        adapterEtat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSens.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEtatmer.setAdapter(adapterEtat);
        spSensNav.setAdapter(adapterSens);

        try {
            Log.d("INFO","Etat : "+dataGet.get("etat") + ", Navigation : "+ dataGet.get("navigation"));
            int spinnerPositionEtat = adapterEtat.getPosition(dataGet.get("etat").toString());
            int spinnerPositionNav = adapterSens.getPosition(dataGet.get("navigation").toString());
            Log.d("INFO","Etat Position : "+spinnerPositionEtat + ", Navigation Position : "+ spinnerPositionNav);

            spEtatmer.setSelection(spinnerPositionEtat);
            spSensNav.setSelection(spinnerPositionNav);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JSONObject finalDataGet = dataGet;
        final String finalOldName = oldName;
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INFO","Button OK pushed");
                JSONObject result = new JSONObject();
                try {
                    finalDataGet.put("nom",ed.getText().toString());
                    finalDataGet.put("etat",spEtatmer.getSelectedItem().toString());
                    finalDataGet.put("navigation",spSensNav.getSelectedItem().toString());

                    Log.d("INFO","New data : "+finalDataGet.toString());

                    File file = new File(v.getContext().getFilesDir()+"/"+finalDataGet.get("nom").toString());
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file,false);
                    fileOutputStream.write((finalDataGet.toString().getBytes()));

                    if (!finalOldName.equals(finalDataGet.get("nom").toString())){
                        File fileToDelete = new File(v.getContext().getFilesDir()+"/"+ finalOldName);
                        fileToDelete.delete();
                    }

                    result.put("status","OK");
                    result.put("value","Modification effectué");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                myIntent.putExtra("resultJson",result.toString());
                v.getContext().startActivity(myIntent);
            }
        });
        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}