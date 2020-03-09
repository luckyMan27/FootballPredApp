package com.fortunato.footballpredictions.Networks;

import android.app.Activity;
import android.view.View;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Country;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.fortunato.footballpredictions.Fragments.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkHome implements Runnable {

    private static final String BASE_URL = "https://server1.api-football.com/";
    private String fullString;
    private List<BaseType> list = null;
    private Activity activity;

    private BaseFragment fragment;
    private int requestType;
    private String id;

    public NetworkHome(String otherParms, int requestType, String ligueId,
                       BaseFragment fragment, Activity activity) {
        this.fullString = BASE_URL+otherParms;
        this.fragment = fragment;
        this.requestType = requestType;
        this.id = ligueId;
        this.activity = activity;
    }

    @Override
    public void run() {
        if(MainActivity.NETWORK_CONNECTION == false) return;

        Response response;
        switch (requestType){
            case 0:
                try {
                    response = doRequest(null);
                    if(response != null && response.isSuccessful()) {
                        parseResponseCountry(Objects.requireNonNull(response.body()).string());
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    response = doRequest(null);
                    if(response != null && response.isSuccessful()) {
                        parseResponseLigue(Objects.requireNonNull(response.body()).string());
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    response = doRequest(BASE_URL+"fixtures/rounds/"+id+"/current");
                    String regSeason = null;
                    if(response != null && response.isSuccessful()) {
                        regSeason = parseResponseRegSeason(
                                Objects.requireNonNull(response.body()).string());
                        response.close();
                    }

                    response = doRequest(BASE_URL+"fixtures/league/"+id+"/"+regSeason);
                    if(response != null && response.isSuccessful()) {
                        parseResponseFixture(Objects.requireNonNull(response.body()).string());
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private Response doRequest(String passUrl){
        OkHttpClient client = new OkHttpClient();

        String urlReq = (passUrl != null) ? passUrl : fullString;

        Request request = new Request.Builder()
                //.header("X-RapidAPI-Key", "4e09d9f0ee3c6a1777a1ed192fe1437d") // Account 4
                //.header("X-RapidAPI-Key", "c2ebe78de8a3c018cac16ba29d278c6f") // Account 3
                //.header("X-RapidAPI-Key", "e28ec9b1e641f085727d792f16e41271") // Account 2
                .header("X-RapidAPI-Key", "1c9263f72d96f81d03f5f55009ac668d") // Account 1
                .header("Accept", "application/json")
                .cacheControl(new CacheControl.Builder()
                        .maxAge(1, TimeUnit.DAYS)
                        .build())
                .url(urlReq)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void notifyUpdate(){
        for(BaseType obj : list){
            fragment.addItem(obj);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.flush();
                fragment.getProgBar().setVisibility(View.GONE);
            }
        });
    }

    private void parseResponseCountry(String body){
        list = new LinkedList<>();
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(jsonObject.getInt("results") != 0 && !jsonObject.isNull("countries")) {
                JSONArray countries = jsonObject.getJSONArray("countries");

                Country country;
                for(int i=0; i<countries.length(); i++){
                    country = new Country(countries.getJSONObject(i));
                    if(!country.isEmpty() && !list.contains(country)){
                        list.add(country);
                        if(country.getUrlImg()!=null && !country.getUrlImg().equals("null")){
                            country.setLoadImage( new LoadImage(country.getUrlImg(), null, country));
                            country.getLoadImage().start();
                        }
                    }
                }
            } else {
                // Set a dialog bar
                throw new RuntimeException("Unable to download data!");
            }
            notifyUpdate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseResponseLigue(String body){
        list = new LinkedList<>();
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(jsonObject.getInt("results") != 0 && !jsonObject.isNull("leagues")) {
                JSONArray leagues = jsonObject.getJSONArray("leagues");

                League league;
                for (int i = 0; i<leagues.length(); i++) {
                    league = new League(leagues.getJSONObject(i));
                    if (!league.isEmpty() && league.getPredictions()) {
                        list.add(league);
                        if(league.getLoadImage()!=null)
                            league.getLoadImage().start();
                    }
                }
            } else {
                throw new RuntimeException("Unable to download data!");
            }
            notifyUpdate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseResponseFixture(String body){
        list = new LinkedList<>();
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(jsonObject.getInt("results") != 0 && !jsonObject.isNull("fixtures")) {
                JSONArray fixtures = jsonObject.getJSONArray("fixtures");

                LeagueFixture fixture;
                for (int i = 0; i < fixtures.length(); i++) {
                    fixture = new LeagueFixture(fixtures.getJSONObject(i));
                    if (!fixture.isEmpty()) list.add(fixture);
                }
            } else {
                throw new RuntimeException("Unable to download data!");
            }
            notifyUpdate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseResponseRegSeason(String body){
        String result = null;
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(jsonObject.getInt("results") != 0 && !jsonObject.isNull("fixtures")) {
                JSONArray app = jsonObject.getJSONArray("fixtures");
                result =  app.getString(0);
            }else{
                throw new RuntimeException("Unable to get current regular season!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
