package com.fortunato.footballpredictions.DataStructures;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class PredictionStatisticTeam extends BaseType{
    private String team_id = null;
    private String teamName = null;
    private String team5MatForme = null;
    private String team5MatAtt = null;
    private String team5MatDef = null;
    private String team5MatGoals = null;
    private String team5MatGoalsAVG = null;
    private String team5MatGoalsAgainst = null;
    private String team5MatGoalsAgainstAVG = null;
    private String lH2hPlayedHome = null;
    private String lH2hPlayedAway = null;
    private String lH2hPlayedTotal = null;
    private String lH2hWinsHome = null;
    private String lH2hWinsAway = null;
    private String lH2hWinsTotal = null;
    private String lH2hDrawsHome = null;
    private String lH2hDrawsAway = null;
    private String lH2hDrawsTotal = null;
    private String lH2hLosesHome = null;
    private String lH2hLosesAway = null;
    private String lH2hLosesTotal = null;

    public PredictionStatisticTeam(JSONObject jsonObject) {
        JSONObject app;
        try {
            team_id = jsonObject.getString("team_id");
            teamName = jsonObject.getString("team_name");

            app = jsonObject.getJSONObject("last_5_matches");
            team5MatForme = app.getString("forme");
            team5MatAtt = app.getString("att");
            team5MatDef = app.getString("def");
            team5MatGoals = app.getString("goals");
            team5MatGoalsAVG = app.getString("goals_avg");
            team5MatGoalsAgainst = app.getString("goals_against");
            team5MatGoalsAgainstAVG = app.getString("goals_against_avg");

            JSONObject parent = jsonObject.getJSONObject("last_h2h");
            app = parent.getJSONObject("played");
            lH2hPlayedHome = app.getString("home");
            lH2hPlayedAway = app.getString("away");
            lH2hPlayedTotal = app.getString("total");

            app = parent.getJSONObject("wins");
            lH2hWinsHome = app.getString("home");
            lH2hWinsAway = app.getString("away");
            lH2hWinsTotal = app.getString("total");

            app = parent.getJSONObject("draws");
            lH2hDrawsHome = app.getString("home");
            lH2hDrawsAway = app.getString("away");
            lH2hDrawsTotal = app.getString("total");

            app = parent.getJSONObject("loses");
            lH2hLosesHome = app.getString("home");
            lH2hLosesAway = app.getString("away");
            lH2hLosesTotal = app.getString("total");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "PredictionStatisticteam = {" +
                "team_id='" + team_id + '\'' +
                ", teamName='" + teamName + '\'' +
                ", team5MatForme='" + team5MatForme + '\'' +
                ", team5MatAtt='" + team5MatAtt + '\'' +
                ", team5MatDef='" + team5MatDef + '\'' +
                ", team5MatGoals='" + team5MatGoals + '\'' +
                ", team5MatGoalsAVG='" + team5MatGoalsAVG + '\'' +
                ", team5MatGoalsAgainst='" + team5MatGoalsAgainst + '\'' +
                ", team5MatGoalsAgainstAVG='" + team5MatGoalsAgainstAVG + '\'' +
                ", lH2hPlayedHome='" + lH2hPlayedHome + '\'' +
                ", lH2hPlayedAway='" + lH2hPlayedAway + '\'' +
                ", lH2hPlayedTotal='" + lH2hPlayedTotal + '\'' +
                ", lH2hWinsHome='" + lH2hWinsHome + '\'' +
                ", lH2hWinsAway='" + lH2hWinsAway + '\'' +
                ", lH2hWinsTotal='" + lH2hWinsTotal + '\'' +
                ", lH2hDrawsHome='" + lH2hDrawsHome + '\'' +
                ", lH2hDrawsAway='" + lH2hDrawsAway + '\'' +
                ", lH2hDrawsTotal='" + lH2hDrawsTotal + '\'' +
                ", lH2hLosesHome='" + lH2hLosesHome + '\'' +
                ", lH2hLosesAway='" + lH2hLosesAway + '\'' +
                ", lH2hLosesTotal='" + lH2hLosesTotal + '\'' +
                '}';
    }

    public String getTeam_id() {
        return team_id;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeam5MatForme() {
        return team5MatForme;
    }

    public String getTeam5MatAtt() {
        return team5MatAtt;
    }

    public String getTeam5MatDef() {
        return team5MatDef;
    }

    public String getTeam5MatGoals() {
        return team5MatGoals;
    }

    public String getTeam5MatGoalsAVG() {
        return team5MatGoalsAVG;
    }

    public String getTeam5MatGoalsAgainst() {
        return team5MatGoalsAgainst;
    }

    public String getTeam5MatGoalsAgainstAVG() {
        return team5MatGoalsAgainstAVG;
    }

    public String getlH2hPlayedHome() {
        return lH2hPlayedHome;
    }

    public String getlH2hPlayedAway() {
        return lH2hPlayedAway;
    }

    public String getlH2hPlayedTotal() {
        return lH2hPlayedTotal;
    }

    public String getlH2hWinsHome() {
        return lH2hWinsHome;
    }

    public String getlH2hWinsAway() {
        return lH2hWinsAway;
    }

    public String getlH2hWinsTotal() {
        return lH2hWinsTotal;
    }

    public String getlH2hDrawsHome() {
        return lH2hDrawsHome;
    }

    public String getlH2hDrawsAway() {
        return lH2hDrawsAway;
    }

    public String getlH2hDrawsTotal() {
        return lH2hDrawsTotal;
    }

    public String getlH2hLosesHome() {
        return lH2hLosesHome;
    }

    public String getlH2hLosesAway() {
        return lH2hLosesAway;
    }

    public String getlH2hLosesTotal() {
        return lH2hLosesTotal;
    }
}
