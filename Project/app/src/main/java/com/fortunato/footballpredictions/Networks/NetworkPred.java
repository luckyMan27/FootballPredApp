package com.fortunato.footballpredictions.Networks;

import android.view.View;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.Activities.PredictionActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.FixturePrediction;

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

public class NetworkPred implements Runnable {

    private static final String BASE_URL = "https://server1.api-football.com/";
    private String fullString;
    private List<BaseType> list = null;
    private PredictionActivity activity;
    private int requestType;

    public NetworkPred(String otherParms, int requestType, PredictionActivity activity) {
        this.fullString = BASE_URL+otherParms;
        this.requestType = requestType;
        this.activity = activity;
    }

    @Override
    public void run() {

        if(MainActivity.NETWORK_CONNECTION == false) return;

        switch (requestType) {
            case 0:
                try {
                    Response response = doRequest(null);
                    if (response != null && response.isSuccessful()) {
                        parseResponsePredictions(Objects.requireNonNull(response.body()).string());
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
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
            activity.addItem(obj);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.flush();
                activity.getProgBar().setVisibility(View.GONE);
            }
        });
    }

    private void parseResponsePredictions(String body){
        list = new LinkedList<>();
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(jsonObject.getInt("results") != 0 && !jsonObject.isNull("predictions")) {
                JSONArray predictions = jsonObject.getJSONArray("predictions");

                FixturePrediction prediction;
                for (int i = 0; i < predictions.length(); i++) {
                    prediction = new FixturePrediction(predictions.getJSONObject(i));
                    if (!prediction.isEmpty()) list.add(prediction);
                }
            } else {
                throw new RuntimeException("Unable to download data!");
            }
            notifyUpdate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
