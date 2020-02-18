package com.fortunato.footballpredictions.Networks;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;

import com.android.volley.RequestQueue;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;
import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;

import com.fortunato.footballpredictions.Fragments.BetFragment;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

                getRequest(BASE_URL + "/bets", uid);
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
            } else {
                Toast.makeText(a, "No items present", Toast.LENGTH_SHORT).show();;
            }
            Log.i("list",list.toString());
            notifyUpdate();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getRequest(String url, final String uid) {
        RequestQueue queue = Volley.newRequestQueue(f.getContext());
        JsonArrayRequest getRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseBets(response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("uid",uid);

                return params;
            }
        };
        queue.add(getRequest);

    }

}