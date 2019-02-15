package com.cyrilcrozes.simulboat.Scenario;

import org.json.JSONException;
import org.json.JSONObject;

public class Scenario {
    String name;
    String date;
    String etatMer;
    String sensNav;

    Scenario(String name, String date, String etatMer, String sensNav) {
        this.name = name;
        this.date = date;
        this.etatMer = etatMer;
        this.sensNav = sensNav;
    }

    JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("nom",name);
        object.put("date",date);
        object.put("etat",etatMer);
        object.put("navigation",sensNav);
        return object;
    }
}
