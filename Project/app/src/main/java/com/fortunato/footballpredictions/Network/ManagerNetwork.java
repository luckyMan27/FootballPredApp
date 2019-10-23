package com.fortunato.footballpredictions.Network;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.fortunato.footballpredictions.Fragments.HomeFragment;

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

public class ManagerNetwork implements Runnable {

    private static final String BASE_URL = "https://server1.api-football.com/";
    private String fullString;
    private List<String> list = null;
    private Activity activity;
    private Fragment fragment;

    public ManagerNetwork(String otherParms, Fragment fragment, Activity activity) {
        this.fullString = BASE_URL+otherParms;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public void run() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("X-RapidAPI-Key", "e28ec9b1e641f085727d792f16e41271")
                .header("Accept", "application/json")
                .cacheControl(new CacheControl.Builder()
                        .maxAge(1, TimeUnit.DAYS)
                        .build())
                .url(fullString)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                parseResponse(Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseResponse(String body){
        list = new LinkedList<String>();
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(!jsonObject.isNull("leagues")) {
                JSONArray leagues = jsonObject.getJSONArray("leagues");

                JSONObject ligue;
                String ligueStr = null;

                for (int i = 0; i < leagues.length(); i++) {
                    ligue = new JSONObject(leagues.get(i).toString());
                    if (ligue.get("type").toString().equals("League"))
                        ligueStr = ligue.get("name").toString() + " (" + ligue.get("country").toString() + " "
                                + ligue.get("league_id").toString() + ")";
                    if (ligueStr != null) list.add(ligueStr);
                }
            } else {
                list.add("Impossibile scaricare dati!");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (fragment instanceof HomeFragment) {
            final HomeFragment homefrag = (HomeFragment) fragment;

            for(String str : list){
                homefrag.addItem(str);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    homefrag.flush();
                    homefrag.getProgBar().setVisibility(View.GONE);
                }
            });
        }
    }
}
