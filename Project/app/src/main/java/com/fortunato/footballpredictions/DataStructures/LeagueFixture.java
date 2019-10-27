package com.fortunato.footballpredictions.DataStructures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

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

public class LeagueFixture extends BaseType implements Serializable {
    private String fixture_id;
    private String league_id;
    private String event_timestamp;
    private String round = null;
    private String status = null;
    private String statsShort = null;
    private String elapsed_time = null;
    private String venue = null;
    private String referee = null;
    private String hTeam_id = null;
    private String hTeamName = null;
    private transient Bitmap hLogo = null;
    private String aTeam_id = null;
    private String aTeamName = null;
    private transient Bitmap aLogo = null;
    private String goalsHomeTeam = null;
    private String goalsAwayTeam = null;
    private String sHalftime = null;
    private String sFulltime = null;
    private String sExtratime = null;
    private String sPenalty = null;

    public LeagueFixture(JSONObject jsonObject) {
        JSONObject app;
        try {
            fixture_id = jsonObject.getString("fixture_id");
            league_id = jsonObject.getString("league_id");
            event_timestamp = jsonObject.getString("event_timestamp");
            round = jsonObject.getString("round");
            status = jsonObject.getString("status");
            statsShort = jsonObject.getString("statusShort");
            elapsed_time = jsonObject.getString("elapsed");
            venue = jsonObject.getString("venue");
            referee = jsonObject.getString("referee");
            goalsHomeTeam = jsonObject.getString("goalsHomeTeam");
            goalsAwayTeam = jsonObject.getString("goalsAwayTeam");

            app = jsonObject.getJSONObject("homeTeam");
            hTeam_id = app.getString("team_id");
            hTeamName = app.getString("team_name");
            String urlHLogo = app.getString("logo");

            app = jsonObject.getJSONObject("awayTeam");
            aTeam_id = app.getString("team_id");
            aTeamName = app.getString("team_name");

            String urlALogo = app.getString("logo");
            LoadImage loadImageAway = new LoadImage(urlHLogo, urlALogo, LeagueFixture.this);
            loadImageAway.run();

            app = jsonObject.getJSONObject("score");
            sHalftime = app.getString("halftime");
            sFulltime = app.getString("fulltime");
            sExtratime = app.getString("extratime");
            sPenalty = app.getString("penalty");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(){
        return  fixture_id.isEmpty() && league_id.isEmpty() && event_timestamp.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeagueFixture)) return false;
        LeagueFixture fixture = (LeagueFixture) o;
        return fixture_id.equals(fixture.fixture_id) &&
                league_id.equals(fixture.league_id) &&
                event_timestamp.equals(fixture.event_timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixture_id, league_id, event_timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "Fixture = {" +
                "fixture_id='" + fixture_id + '\'' +
                ", league_id='" + league_id + '\'' +
                ", event_timestamp='" + event_timestamp + '\'' +
                ", round='" + round + '\'' +
                ", status='" + status + '\'' +
                ", statsShort='" + statsShort + '\'' +
                ", elapsed_time='" + elapsed_time + '\'' +
                ", venue='" + venue + '\'' +
                ", referee='" + referee + '\'' +
                ", hTeam_id='" + hTeam_id + '\'' +
                ", hTeam_name='" + hTeamName + '\'' +
                ", hLogo='" + hLogo + '\'' +
                ", aTeam_id='" + aTeam_id + '\'' +
                ", aTeam_name='" + aTeamName + '\'' +
                ", aLogo='" + aLogo + '\'' +
                ", goalsHomeTeam='" + goalsHomeTeam + '\'' +
                ", goalsAwayTeam='" + goalsAwayTeam + '\'' +
                ", sHalftime='" + sHalftime + '\'' +
                ", sFulltime='" + sFulltime + '\'' +
                ", sExtratime='" + sExtratime + '\'' +
                ", sPenalty='" + sPenalty + '\'' +
                '}';
    }

    public String getFixture_id() {
        return fixture_id;
    }

    public String getLeague_id() {
        return league_id;
    }

    public String getEvent_timestamp() {
        return event_timestamp;
    }

    public String getRound() {
        return round;
    }

    public String getStatus() {
        return status;
    }

    public String getStatsShort() {
        return statsShort;
    }

    public String getElapsed_time() {
        return elapsed_time;
    }

    public String getVenue() {
        return venue;
    }

    public String getReferee() {
        return referee;
    }

    public String gethTeam_id() {
        return hTeam_id;
    }

    public String gethTeam_name() {
        return hTeamName;
    }

    public Bitmap gethLogo() {
        return hLogo;
    }

    public String getaTeam_id() {
        return aTeam_id;
    }

    public String getaTeam_name() {
        return aTeamName;
    }

    public Bitmap getaLogo() {
        return aLogo;
    }

    public String getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public String getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    public String getsHalftime() {
        return sHalftime;
    }

    public String getsFulltime() {
        return sFulltime;
    }

    public String getsExtratime() {
        return sExtratime;
    }

    public String getsPenalty() {
        return sPenalty;
    }

    public void sethLogo(Bitmap hLogo) {
        this.hLogo = hLogo;
    }

    public void setaLogo(Bitmap aLogo) {
        this.aLogo = aLogo;
    }

    private void writeObject(ObjectOutputStream os){
        try {
            os.defaultWriteObject();
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            boolean success = hLogo.compress(Bitmap.CompressFormat.PNG, 100, byteOutStream);
            if(success) os.writeObject(byteOutStream.toByteArray());
            byteOutStream.reset();
            success = aLogo.compress(Bitmap.CompressFormat.PNG, 100, byteOutStream);
            if(success) os.writeObject(byteOutStream.toByteArray());
            byteOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream is){
        try {
            is.defaultReadObject();
            byte[] img = (byte[]) is.readObject();
            if(img != null && img.length > 0){
                hLogo = BitmapFactory.decodeByteArray(img, 0, img.length);
            }
            img = (byte[]) is.readObject();
            if(img != null && img.length > 0){
                aLogo = BitmapFactory.decodeByteArray(img, 0, img.length);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
