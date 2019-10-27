package com.fortunato.footballpredictions.DataStructures;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class PredictionStatisticComparison extends BaseType{
    private String formeHome = null;
    private String formeAway = null;
    private String attHome = null;
    private String attAway = null;
    private String defHome = null;
    private String defAway = null;
    private String fishLawHome = null;
    private String fishLawAway = null;
    private String h2hHome = null;
    private String h2hAway = null;
    private String goalsH2hHome = null;
    private String goalsH2hAway = null;

    public PredictionStatisticComparison(JSONObject jsonObject) {
        JSONObject app;
        try {
            app = jsonObject.getJSONObject("forme");
            formeHome = app.getString("home");
            formeAway = app.getString("away");

            app = jsonObject.getJSONObject("att");
            attHome = app.getString("home");
            attAway = app.getString("away");

            app = jsonObject.getJSONObject("def");
            defHome = app.getString("home");
            defAway = app.getString("away");

            app = jsonObject.getJSONObject("fish_law");
            fishLawHome = app.getString("home");
            fishLawAway = app.getString("away");

            app = jsonObject.getJSONObject("h2h");
            h2hHome = app.getString("home");
            h2hAway = app.getString("away");

            app = jsonObject.getJSONObject("goals_h2h");
            goalsH2hHome = app.getString("home");
            goalsH2hAway = app.getString("away");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "PredictionStatisticComparison = {" +
                "formeHome='" + formeHome + '\'' +
                ", formeAway='" + formeAway + '\'' +
                ", attHome='" + attHome + '\'' +
                ", attAway='" + attAway + '\'' +
                ", defHome='" + defHome + '\'' +
                ", defAway='" + defAway + '\'' +
                ", fishLawHome='" + fishLawHome + '\'' +
                ", fishLawAway='" + fishLawAway + '\'' +
                ", h2hHome='" + h2hHome + '\'' +
                ", h2hAway='" + h2hAway + '\'' +
                ", goalsH2hHome='" + goalsH2hHome + '\'' +
                ", goalsH2hAway='" + goalsH2hAway + '\'' +
                '}';
    }

    public String getFormeHome() {
        return formeHome;
    }

    public String getFormeAway() {
        return formeAway;
    }

    public String getAttHome() {
        return attHome;
    }

    public String getAttAway() {
        return attAway;
    }

    public String getDefHome() {
        return defHome;
    }

    public String getDefAway() {
        return defAway;
    }

    public String getFishLawHome() {
        return fishLawHome;
    }

    public String getFishLawAway() {
        return fishLawAway;
    }

    public String getH2hHome() {
        return h2hHome;
    }

    public String getH2hAway() {
        return h2hAway;
    }

    public String getGoalsH2hHome() {
        return goalsH2hHome;
    }

    public String getGoalsH2hAway() {
        return goalsH2hAway;
    }
}
