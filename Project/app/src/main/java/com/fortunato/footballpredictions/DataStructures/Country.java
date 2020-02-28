package com.fortunato.footballpredictions.DataStructures;

import android.content.Context;
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

public class Country extends BaseType implements Serializable {
    private String country;
    private String code;
    private String urlImg;
    private transient Bitmap flag = null;
    private transient LoadImage loadImage = null;

    public Country(JSONObject jsonObject){
        try {
            country = jsonObject.getString("country");
            code = jsonObject.getString("code");
            urlImg = jsonObject.getString("flag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(){
        return country.isEmpty() && code.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country1 = (Country) o;
        return country.equals(country1.country) &&
                code.equals(country1.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, code);
    }

    @NonNull
    @Override
    public String toString() {
        return "Country = {" +
                "country='" + country + '\'' +
                ", code='" + code + '\'' +
                ", urlImg='" + urlImg + '\'' +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public void setflag(Bitmap flag) {
        this.flag = flag;
    }

    public Bitmap getflag() {
        return flag;
    }

    public LoadImage getLoadImage() {
        return loadImage;
    }

    public void setLoadImage(LoadImage loadImage) {
        this.loadImage = loadImage;
    }

    private void writeObject(ObjectOutputStream os){
        try {
            os.defaultWriteObject();
            if(flag!=null) {
                ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
                boolean success = flag.compress(Bitmap.CompressFormat.PNG, 50, byteOutStream);
                if(success) os.writeObject(byteOutStream.toByteArray());
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
                flag = BitmapFactory.decodeByteArray(img, 0, img.length);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
