package com.fortunato.footballpredictions.DataStructures;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamStatistic extends BaseType{
    private String mPlayedHome = null;
    private String mPlayedAway = null;
    private String mPlayedTotal = null;
    private String winsHome = null;
    private String winsAway = null;
    private String winsTotal = null;
    private String drawsHome = null;
    private String drawsAway = null;
    private String drawsTotal = null;
    private String loesHome = null;
    private String losesAway = null;
    private String loeseTotal = null;
    private String goalsForHome = null;
    private String goalsForAway = null;
    private String goalForTotal = null;
    private String goalsAgainstHome = null;
    private String goalsAgainstAway = null;
    private String goalsAgainstTotal = null;
    private String avgGoalsForHome = null;
    private String avgGoalsForAway = null;
    private String avgGoalForTotal = null;
    private String avgGoalsAgainstHome = null;
    private String avgGoalsAgainstAway = null;
    private String avgGoalsAgainstTotal = null;

    public TeamStatistic(JSONObject jsonObject) {
        JSONObject app;
        try {
            JSONObject parent = jsonObject.getJSONObject("matchs");
            app = parent.getJSONObject("matchsPlayed");
            mPlayedHome = app.getString("home");
            mPlayedAway = app.getString("away");
            mPlayedTotal = app.getString("total");

            app = parent.getJSONObject("wins");
            winsHome = app.getString("home");
            winsAway = app.getString("away");
            winsTotal = app.getString("total");

            app = parent.getJSONObject("draws");
            drawsHome = app.getString("home");
            drawsAway = app.getString("away");
            drawsTotal = app.getString("total");

            app = parent.getJSONObject("loses");
            loesHome = app.getString("home");
            losesAway = app.getString("away");
            loeseTotal = app.getString("total");

            parent = jsonObject.getJSONObject("goals");
            app = parent.getJSONObject("goalsFor");
            goalsForHome = app.getString("home");
            goalsForAway = app.getString("away");
            goalForTotal = app.getString("total");

            app = parent.getJSONObject("goalsAgains");
            goalsAgainstHome = app.getString("home");
            goalsAgainstAway = app.getString("away");
            goalsAgainstTotal = app.getString("total");

            parent = jsonObject.getJSONObject("goalsAvg");
            app = parent.getJSONObject("goalsFor");
            avgGoalsForHome = app.getString("");
            avgGoalsForAway = app.getString("");
            avgGoalForTotal = app.getString("");

            app = parent.getJSONObject("goalsAgains");
            avgGoalsAgainstHome = app.getString("");
            avgGoalsAgainstAway = app.getString("");
            avgGoalsAgainstTotal = app.getString("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "TeamStatistic = {" +
                "mPlayedHome='" + mPlayedHome + '\'' +
                ", mPlayedAway='" + mPlayedAway + '\'' +
                ", mPlayedTotal='" + mPlayedTotal + '\'' +
                ", winsHome='" + winsHome + '\'' +
                ", winsAway='" + winsAway + '\'' +
                ", winsTotal='" + winsTotal + '\'' +
                ", drawsHome='" + drawsHome + '\'' +
                ", drawsAway='" + drawsAway + '\'' +
                ", drawsTotal='" + drawsTotal + '\'' +
                ", loesHome='" + loesHome + '\'' +
                ", losesAway='" + losesAway + '\'' +
                ", loeseTotal='" + loeseTotal + '\'' +
                ", goalsForHome='" + goalsForHome + '\'' +
                ", goalsForAway='" + goalsForAway + '\'' +
                ", goalForTotal='" + goalForTotal + '\'' +
                ", goalsAgainstHome='" + goalsAgainstHome + '\'' +
                ", goalsAgainstAway='" + goalsAgainstAway + '\'' +
                ", goalsAgainstTotal='" + goalsAgainstTotal + '\'' +
                ", avgGoalsForHome='" + avgGoalsForHome + '\'' +
                ", avgGoalsForAway='" + avgGoalsForAway + '\'' +
                ", avgGoalForTotal='" + avgGoalForTotal + '\'' +
                ", avgGoalsAgainstHome='" + avgGoalsAgainstHome + '\'' +
                ", avgGoalsAgainstAway='" + avgGoalsAgainstAway + '\'' +
                ", avgGoalsAgainstTotal='" + avgGoalsAgainstTotal + '\'' +
                '}';
    }

    public String getmPlayedHome() {
        return mPlayedHome;
    }

    public String getmPlayedAway() {
        return mPlayedAway;
    }

    public String getmPlayedTotal() {
        return mPlayedTotal;
    }

    public String getWinsHome() {
        return winsHome;
    }

    public String getWinsAway() {
        return winsAway;
    }

    public String getWinsTotal() {
        return winsTotal;
    }

    public String getDrawsHome() {
        return drawsHome;
    }

    public String getDrawsAway() {
        return drawsAway;
    }

    public String getDrawsTotal() {
        return drawsTotal;
    }

    public String getLoesHome() {
        return loesHome;
    }

    public String getLosesAway() {
        return losesAway;
    }

    public String getLoeseTotal() {
        return loeseTotal;
    }

    public String getGoalsForHome() {
        return goalsForHome;
    }

    public String getGoalsForAway() {
        return goalsForAway;
    }

    public String getGoalForTotal() {
        return goalForTotal;
    }

    public String getGoalsAgainstHome() {
        return goalsAgainstHome;
    }

    public String getGoalsAgainstAway() {
        return goalsAgainstAway;
    }

    public String getGoalsAgainstTotal() {
        return goalsAgainstTotal;
    }

    public String getAvgGoalsForHome() {
        return avgGoalsForHome;
    }

    public String getAvgGoalsForAway() {
        return avgGoalsForAway;
    }

    public String getAvgGoalForTotal() {
        return avgGoalForTotal;
    }

    public String getAvgGoalsAgainstHome() {
        return avgGoalsAgainstHome;
    }

    public String getAvgGoalsAgainstAway() {
        return avgGoalsAgainstAway;
    }

    public String getAvgGoalsAgainstTotal() {
        return avgGoalsAgainstTotal;
    }
}
