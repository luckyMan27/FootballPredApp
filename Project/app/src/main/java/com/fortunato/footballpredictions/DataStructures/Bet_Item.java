package com.fortunato.footballpredictions.DataStructures;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Bet_Item extends BaseType implements Serializable {

    private String home;
    private String away;
    private String value;

    public Bet_Item(){}

    public Bet_Item(JSONObject obj){
        try {
            this.home = obj.getString("home");
            this.away = obj.getString("away");
            this.value = obj.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Bet_Item(String home, String away, String value){
        this.home = home;
        this.away = away;
        this.value = value;
    }

    public String getHome() {
        return home;
    }

    public String getAway() {
        return away;
    }

    public String getValue() {
        return value;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{\"home\" :\"" + home + "\",\"away\": \""+ away + "\", \"value\": \""+ value +"\" }";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Bet_Item)) return false;
        Bet_Item item = (Bet_Item) obj;
        return this.home.equals(item.home) &&
                this.away.equals(item.away);
    }

}
