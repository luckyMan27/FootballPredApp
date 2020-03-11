package com.fortunato.footballpredictions.Networks;

import android.app.Activity;
import android.view.View;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;

import com.fortunato.footballpredictions.Fragments.BetFragment;



import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetworkBackend implements Runnable {

    private static final String BASE_URL = "https://footballpredictions80i.herokuapp.com";
    private List<BaseType> list = null;

    private List<BaseType> objlist;
    private BetFragment f;
    private int type;

    private List<BaseType> elements;
    private Activity a;

    private String uid;
    private String description;
    private String date;
    private String id;

    private String body;

    private boolean flag_no_bets = false;

    public NetworkBackend(BetFragment f, List<BaseType> objlist, int type, String date, String description, String uid, Activity a, String id) {
        this.objlist = objlist;
        this.uid = uid;
        this.description = description;
        this.date = date;
        this.f = f;
        this.a = a;
        this.type = type;
        this.id = id;
    }

    @Override
    public void run() {
        if (MainActivity.NETWORK_CONNECTION == false) return;

        Response response;
        switch (type) {
            case 0:
                response = postBets(BASE_URL + "/bets", objlist);
                if (response != null && response.isSuccessful()) {
                    response.close();
                }
                break;
            case 1:

                //getRequest(BASE_URL + "/bets", uid);
                String body = getRequests(BASE_URL + "/bets", uid);
                if(body != null){
                    parseBets(body);
                }
                break;
            case 2:
                response = removeBets(BASE_URL + "/bets", uid, id);
                if (response != null && response.isSuccessful()) {
                    response.close();
                }
                break;
        }


    }

    private Response postBets(String url, List<BaseType> body) {
        OkHttpClient client = new OkHttpClient();


        String s = "{ \"uid\": \"" + uid + "\", \"date\": \"" + date + "\", \"description\": \"" + description + "\", \"matches\": [";
        int i = body.size();
        int j = 0;
        for (BaseType b : body) {
            if (b instanceof Bet_Item) {
                Bet_Item bet = (Bet_Item) b;
                s += bet.toString();

            }
            if (j < i - 1) {
                s += ",";
            }
            j += 1;
        }
        s += "]}";
        Response resp = null;
        RequestBody body_req = RequestBody.create(s, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body_req)
                .build();
        try (Response response = client.newCall(request).execute()) {
            resp = response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }

    private Response removeBets(String url, String uid, String id){
        OkHttpClient client = new OkHttpClient();
        Response resp = null;
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("uid",uid)
                .url(url+"/"+id)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            resp = response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;

    }

    private void notifyUpdate() {
        for (BaseType obj : list) {
            f.addItem(obj);
        }
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                f.flush();
                f.getProgBar().setVisibility(View.GONE);
                if(flag_no_bets){
                    f.notifyNoBets();
                }
            }
        });
    }

    private void parseBets(String body){
        list = new LinkedList<>();
        try {
            JSONArray array = new JSONArray(body);
            if (!array.isNull(0)) {
                Bet bet;
                for (int i = 0; i < array.length(); i++) {
                    bet = new Bet(array.getJSONObject(i));
                    if (!bet.isEmpty() && !list.contains(bet)) {
                        list.add(bet);
                    }
                }
                flag_no_bets = false;
            } else {
                flag_no_bets = true;
            }

            Collections.reverse(list);

            notifyUpdate();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getRequests(String url, String uid){
        OkHttpClient client = new OkHttpClient();
        Response resp = null;
        String body = null;
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("uid",uid)
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                resp = response;
                body = resp.body().string();
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;

    }


}
