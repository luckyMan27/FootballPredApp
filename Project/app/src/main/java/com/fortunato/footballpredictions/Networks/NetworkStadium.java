package com.fortunato.footballpredictions.Networks;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.Activities.StadiumActivity;
import com.fortunato.footballpredictions.DataStructures.League;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkStadium implements Runnable {

    private static final String BASE_URL = "https://server1.api-football.com/teams/team/";
    private static final String LOCATION_URL = "http://www.mapquestapi.com/geocoding/v1/address";

    private StadiumActivity s;
    private int type;

    private String location;
    private String home;

    public NetworkStadium(StadiumActivity s, int type, String home, String location) {

        this.s = s;
        this.type = type;
        this.home = home;
        this.location = location;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void run() {
        if (MainActivity.NETWORK_CONNECTION == false) return;

        Response response;
        switch (type) {
            case 0:
                response = getStadiumAddress(BASE_URL + home);

                if (response != null && response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        Log.i("res", res);
                        parseResponseAddress(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.close();
                }
                break;
            case 1:
                /*
                response = getStadiumLocation(LOCATION_URL, location);
                if (response != null && response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        Log.i("res", res);
                        parseResponseLocation(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.close();
                }*/
                break;
        }


    }

    private Response getStadiumAddress(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //.header("X-RapidAPI-Key", "4e09d9f0ee3c6a1777a1ed192fe1437d") // Account 4
                //.header("X-RapidAPI-Key", "c2ebe78de8a3c018cac16ba29d278c6f") // Account 3
                //.header("X-RapidAPI-Key", "e28ec9b1e641f085727d792f16e41271") // Account 2
                .header("X-RapidAPI-Key", "1c9263f72d96f81d03f5f55009ac668d") // Account 1
                .header("Accept", "application/json")
                .cacheControl(new CacheControl.Builder()
                        .maxAge(1, TimeUnit.DAYS)
                        .build())
                        .url(url)
                        .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
    /*
    private Response getStadiumLocation(String url, String location) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("www.mapquestapi.com")
                .addPathSegment("geocoding")
                .addPathSegment("v1")
                .addPathSegment("address")
                .addQueryParameter("key", "LWTCq8sV7LN6dNTbvsdNFoMHEFWH03zE")
                .addQueryParameter("location", location)

                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
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

     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parseResponseAddress(String body){
        String location;
        try {
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("api");
            if(jsonObject.getInt("results") != 0 && !jsonObject.isNull("teams")) {
                JSONArray teams = jsonObject.getJSONArray("teams");

                JSONObject team;

                team = teams.getJSONObject(0);
                if (team != null && team.getString("venue_address") != null && team.getString("venue_city") != null) {
                    location = team.getString("venue_address")+", "+team.getString("venue_city")+", "+ team.getString("country");
                }
                else if(team != null && team.getString("country") != null){
                    location = team.getString("country");
                }
                else{
                    location = null;
                }
                s.notify_address(location);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parseResponseLocation(String body) {

        Log.i("location", body);
        ArrayList<Double> res = new ArrayList<Double>();
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONArray arr = jsonObject.getJSONArray("results");
            if(arr != null && arr.length() > 0) {
                JSONObject loc = arr.getJSONObject(0);
                JSONArray arr2 = loc.getJSONArray("locations");
                if(arr2 != null && arr2.length() > 0) {
                    JSONObject target = arr2.getJSONObject(0);
                    target = target.getJSONObject("displayLatLng");

                    res.add(target.getDouble("lat"));
                    res.add(target.getDouble("lng"));
                }

                s.notify_coordinates(res);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

     */

}
