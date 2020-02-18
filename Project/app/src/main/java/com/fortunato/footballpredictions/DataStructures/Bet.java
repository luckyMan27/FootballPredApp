package com.fortunato.footballpredictions.DataStructures;

import androidx.annotation.NonNull;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bet extends BaseType implements Serializable {

    private String bet_id;
    private String date;
    private List<Bet_Item> matches;
    private String description;


    public Bet(){}


    public Bet(JSONObject jsonObject) {
        try {
            bet_id = jsonObject.getString("_id");
            date = jsonObject.getString("date");
            description = jsonObject.getString("description");
            matches = getListMatches(jsonObject.getJSONArray("matches"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty () {
        return date.isEmpty();
    }

    @Override
    public int hashCode () {
        return Objects.hash(date, bet_id);
    }

    @NonNull
    @Override
    public String toString () {
        return "Bet = {" +
                "bet_id='" + bet_id + '\'' +
                ", date='" + date + '\'' +
                '}'+ matches;
    }

    public String getBet_id () {
        return bet_id;
    }

    public String getDate () {
        return date;
    }

    public List<Bet_Item> getList () {
        return matches;
    }
    public void setList (List < Bet_Item > list) {
        this.matches = list;
    }

    public String getDescription () {
        return description;
    }

    private List<Bet_Item> getListMatches (JSONArray matches) {
        List<Bet_Item> bets = new ArrayList<Bet_Item>();
        for(int i = 0; i< matches.length(); i++){
            try {
                JSONObject obj = matches.getJSONObject(i);
                Bet_Item b = new Bet_Item(obj);
                bets.add(b);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bets;
    }
}
