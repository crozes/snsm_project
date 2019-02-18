package com.cyrilcrozes.simulboat.Scenario;

import org.json.JSONException;
import org.json.JSONObject;

public class Scenario {
    private String name;
    private String date;
    private String etatMer;
    private String sensNav;
    private String duree;

    Scenario(){
        this.name = null;
        this.date = null;
        this.etatMer = null;
        this.sensNav = null;
        this.duree = null;
    }

    Scenario(String name, String date, String etatMer, String sensNav, String duree) {
        this.name = name;
        this.date = date;
        this.etatMer = etatMer;
        this.sensNav = sensNav;
        this.duree = duree;
    }

    public Scenario(JSONObject jsondata) {

    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("nom",name);
        object.put("date",date);
        object.put("etat",etatMer);
        object.put("navigation",sensNav);
        object.put("duree",duree);
        return object;
    }

    public void initializeWithJson (JSONObject jsonData) throws JSONException {
        this.name = jsonData.get("nom").toString();
        this.date = jsonData.get("date").toString();
        this.etatMer = jsonData.get("etat").toString();
        this.sensNav = jsonData.get("navigation").toString();
        this.duree = jsonData.get("duree").toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtatMer() {
        return etatMer;
    }

    public void setEtatMer(String etatMer) {
        this.etatMer = etatMer;
    }

    public String getSensNav() {
        return sensNav;
    }

    public void setSensNav(String sensNav) {
        this.sensNav = sensNav;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}
