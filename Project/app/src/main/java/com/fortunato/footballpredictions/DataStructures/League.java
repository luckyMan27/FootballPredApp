package com.fortunato.footballpredictions.DataStructures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.fortunato.footballpredictions.Networks.LoadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public class League extends BaseType implements Serializable {
    private String league_id;
    private String name;
    private transient Bitmap logo;
    private String country;
    /*
    private String country_code = null;
    private String season_start = null;
    private String season_end = null;*/

    public League(JSONObject jsonObject){
        JSONObject app;

        try {
            league_id = jsonObject.getString("league_id");
            name = jsonObject.getString("name");

            String urlLogo = jsonObject.getString("logo");
            if(urlLogo != null && !urlLogo.equals("null")){
                LoadImage loadImg = new LoadImage(urlLogo, null,League.this);
                loadImg.run();
            }


            country = jsonObject.getString("country");
            /*
            country_code = jsonObject.getString("country_code");
            season_start = jsonObject.getString("season_start");
            season_end = jsonObject.getString("season_end");
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(){
        return league_id.isEmpty() && name.isEmpty() && country.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof League)) return false;
        League league = (League) o;
        return league_id.equals(league.league_id) &&
                name.equals(league.name) &&
                country.equals(league.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(league_id, name, country);
    }

    @NonNull
    @Override
    public String toString() {
        return "League = {" +
                "league_id='" + league_id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", country='" + country + '\'' +/*
                ", country_code='" + country_code + '\'' +
                ", season_start='" + season_start + '\'' +
                ", season_end='" + season_end + '\'' +
                ", stadings=" + stadings +*/
                '}';
    }

    public String getLeague_id() {
        return league_id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public String getCountry() {
        return country;
    }
    /*
    public String getCountry_code() {
        return country_code;
    }

    public String getSeason_start() {
        return season_start;
    }

    public String getSeason_end() {
        return season_end;
    }*/

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    private void writeObject(ObjectOutputStream os){
        try {
            os.defaultWriteObject();
            if(logo!=null) {
                ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
                boolean success = logo.compress(Bitmap.CompressFormat.PNG, 100, byteOutStream);
                if (success) os.writeObject(byteOutStream.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream is){
        try {
            is.defaultReadObject();
            byte[] img = (byte[]) is.readObject();
            if(img != null && img.length > 0){
                logo = BitmapFactory.decodeByteArray(img, 0, img.length);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

