package com.cyrilcrozes.simulboat.Scenario;

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
}
