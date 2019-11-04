package com.fortunato.footballpredictions.Networks;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Country;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.fortunato.footballpredictions.Fragments.HomeFragment;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadImage extends Thread{
    private String urlHome;
    private String urlAway;
    private Object obj;

    public LoadImage(String urlHome, String urlAway, Object obj) {
        this.urlHome = urlHome;
        this.urlAway = urlAway;
        this.obj = obj;
    }

    @Override
    public void run() {

        if(MainActivity.NETWORK_CONNECTION == false) return;

        if(urlHome != null && urlHome.contains("http")) {
            if (obj instanceof Country ) {
                parseSVGImg();
            } else if(obj instanceof League){
                parsePNGLeague();
            } else if(obj instanceof LeagueFixture && urlAway!=null && !urlAway.isEmpty()){
                parsePNGMatch();
            } else if(obj instanceof MainActivity){
                parsePNG();
            }
        }
    }

    private void parsePNG() {
        InputStream is;
        Bitmap bmapImg = null;
        try {
            is = (InputStream) new URL(urlHome).getContent();
            bmapImg = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainActivity mainActivity = (MainActivity) obj;
        mainActivity.setUserBitmap(bmapImg);
    }

    private Response getData(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlHome)
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void parseSVGImg(){
        Response response = getData();
        if (response != null && response.isSuccessful()) {
            InputStream stream = response.body().byteStream();
            Bitmap bmap = null;

            try {
                SVG svg = SVG.getFromInputStream(stream);
                svg.setDocumentHeight(100);
                svg.setDocumentWidth(100);
                Picture pic = svg.renderToPicture();
                PictureDrawable drawable = new PictureDrawable(pic);
                bmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmap);
                canvas.drawPicture(drawable.getPicture());
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SVGParseException e) {
                e.printStackTrace();
            }

            Country country = (Country) obj;
            country.setflag(bmap);
        }
    }
    
    private void parsePNGLeague() {
        InputStream is;
        Bitmap bmap = null;
        try {
            is = (InputStream) new URL(urlHome).getContent();
            bmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        League league = (League) obj;
        league.setLogo(bmap);
    }

    private void parsePNGMatch(){
        InputStream is;
        Bitmap bmap = null;
        try {
            is = (InputStream) new URL(urlHome).getContent();
            bmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LeagueFixture leagueFixture = (LeagueFixture) obj;
        leagueFixture.sethLogo(bmap);

        try {
            is = (InputStream) new URL(urlAway).getContent();
            bmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        leagueFixture.setaLogo(bmap);
    }
}
