package com.cyrilcrozes.simulboat.Scenario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
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
    Button bPart;

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
        bPart = (Button) itemView.findViewById(R.id.bPartager);

        bSupp.setOnClickListener(this);
        bMod.setOnClickListener(this);
        bPart.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String fileName = name.getText().toString()+".json";
        if (view.getId() == bSupp.getId()) {
            suppFunction(view, fileName);
        }
        else if (view.getId() == bPart.getId()) {
            shareFunction(view, fileName);
        }
        else if (view.getId() == bMod.getId()){
            modFunction(view, fileName);
        }
        else{
            Log.d("ERROR","Erreur au click du boutton, id non reconnu");
        }

        listenerRef.get().onPositionClicked(getAdapterPosition());
    }

    private void suppFunction(final View v, final String fileName){
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
                        File file = getFileInDir(v,fileName);
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        JSONObject result = new JSONObject();
                        if (file.delete()){
                            try {
                                result.put("status","OK");
                                result.put("value",name.getText()+" a été supprimé");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            try {
                                result.put("status","KO");
                                result.put("value","Echec de suppression de " + name.getText());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        myIntent.putExtra("resultJson",result.toString());
                        v.getContext().startActivity(myIntent);
                    }
                });
        builder.create().show();
    }

    private void shareFunction (final View v, String fileName){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File filelocation = getFileInDir(v,fileName);
        if (filelocation.exists()){
            Log.d("INFO","Fichier "+fileName+" existe");
            Uri path = Uri.fromFile(filelocation);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent .setType("text/html");
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Scenario SimulBoat");
            emailIntent .putExtra(Intent.EXTRA_TEXT, "Scenario SimulBoat");
            emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            v.getContext().startActivity(Intent.createChooser(emailIntent , "Scenario SimulBoat"));
        }
        else{
            Log.d("ERROR","Fichier "+fileName+" n'existe pas");
        }
    }

    private void modFunction (final View v, String fileName) {
        // File
        JSONObject dataGet = null;
        String oldName = null;
        try {
            File file = getFileInDir(v,fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
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
            Log.d("INFO","NOm : "+dataGet.get("nom").toString());
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

                    File file = getFileInDir(v,finalDataGet.get("nom").toString()+".json");
                    if (!file.exists()) {
                        file.createNewFile();
                        file.setExecutable(true,false);
                        file.setWritable(true,false);
                        file.setReadable(true,false);
                        FileOutputStream fileOutputStream = new FileOutputStream(file,false);
                        fileOutputStream.write((finalDataGet.toString().getBytes()));

                        if (!finalOldName.equals(finalDataGet.get("nom").toString())){
                            File fileToDelete = getFileInDir(v,finalOldName+".json");
                            fileToDelete.delete();
                        }

                        result.put("status","OK");
                        result.put("value",finalOldName+ " a été modifié");

                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.putExtra("resultJson",result.toString());
                        v.getContext().startActivity(myIntent);
                    }
                    else{
                        ed.setError("Nom scenario deja existant");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    private File getFileInDir(View v, String fileName){
        return new File(v.getContext().getExternalFilesDir(null)+"/"+fileName);
    }

}