package com.fortunato.footballpredictions.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fortunato.footballpredictions.DataStructures.Bet;
import com.fortunato.footballpredictions.DataStructures.Country;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SaveData {

    private static final String COUNTRY="country";
    private static final String LEAGUE="league";
    private static final String MATCH="match";
    private Context context;

    public SaveData() { }

    public SaveData(Context passContex) {
        this.context = passContex;
    }

    public void saveCountries(List<Country> list){
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, Country.class);
        JsonAdapter<List<Country>> jsonAdapter = moshi.adapter(listMyData);
        String json = jsonAdapter.toJson(list);

        SharedPreferences sharedPreferences = context.getSharedPreferences(COUNTRY+"data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COUNTRY, json);
        editor.commit();
    }

    public List<Country> loadCountries(){
        List<Country> list = new LinkedList<Country>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(COUNTRY+"data", MODE_PRIVATE);
        String json = sharedPreferences.getString(COUNTRY, "");
        if(json.isEmpty()) return list;

        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, Country.class);
        JsonAdapter<List<Country>> jsonAdapter = moshi.adapter(listMyData);

        try {
            list = jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveLeagues(List<League> list, String countryName){
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, League.class);
        JsonAdapter<List<League>> jsonAdapter = moshi.adapter(listMyData);
        String json = jsonAdapter.toJson(list);

        SharedPreferences sharedPreferences = context.getSharedPreferences(LEAGUE+countryName+"data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COUNTRY, json);
        editor.commit();
    }

    public List<League> loadLeagues(String countryName){
        List<League> list = new LinkedList<League>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(LEAGUE+countryName+"data", MODE_PRIVATE);
        String json = sharedPreferences.getString(COUNTRY, "");
        if(json.isEmpty()) return list;

        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, League.class);
        JsonAdapter<List<League>> jsonAdapter = moshi.adapter(listMyData);

        try {
            list = jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveLeagueFixtures(List<LeagueFixture> list){
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, LeagueFixture.class);
        JsonAdapter<List<LeagueFixture>> jsonAdapter = moshi.adapter(listMyData);
        String json = jsonAdapter.toJson(list);

        SharedPreferences sharedPreferences = context.getSharedPreferences(MATCH+"data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COUNTRY, json);
        editor.commit();
    }

    public List<LeagueFixture> loadLeagueFixtures(){
        List<LeagueFixture> list = new LinkedList<LeagueFixture>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(MATCH+"data", MODE_PRIVATE);
        String json = sharedPreferences.getString(COUNTRY, "");
        if(json.isEmpty()) return list;

        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, LeagueFixture.class);
        JsonAdapter<List<LeagueFixture>> jsonAdapter = moshi.adapter(listMyData);

        try {
            list = jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveBets(List<Bet> list){
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, Bet.class);
        JsonAdapter<List<Bet>> jsonAdapter = moshi.adapter(listMyData);
        String json = jsonAdapter.toJson(list);

        SharedPreferences sharedPreferences = context.getSharedPreferences(COUNTRY+"data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COUNTRY, json);
        editor.commit();
    }

    public List<Bet> loadBets(){
        List<Bet> list = new LinkedList<Bet>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(COUNTRY+"data", MODE_PRIVATE);
        String json = sharedPreferences.getString(COUNTRY, "");
        if(json.isEmpty()) return list;

        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, Country.class);
        JsonAdapter<List<Bet>> jsonAdapter = moshi.adapter(listMyData);

        try {
            list = jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
