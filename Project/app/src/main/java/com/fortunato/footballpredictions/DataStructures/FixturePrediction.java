package com.fortunato.footballpredictions.DataStructures;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class FixturePrediction extends BaseType{
    private String matchWinner;
    private String underOver;
    private String goalsHome;
    private String goalsAway;
    private String advice;
    private String winPercHome = null;
    private String winPercAway = null;
    private String winPercDraws = null;
    private PredictionStatisticTeam home = null;
    private PredictionStatisticTeam away = null;
    private PredictionStatisticComparison cmp = null;

    public FixturePrediction(JSONObject jsonObject) {
        JSONObject app;
        try {
            matchWinner = jsonObject.getString("match_winner").replace("N", "").replace(" ", "");
            underOver = jsonObject.getString("under_over").replace("-", "under ").replace("+", "over ");
            goalsHome = jsonObject.getString("goals_home").replace("-", "under ").replace("+", "over ");
            goalsAway = jsonObject.getString("goals_away").replace("-", "under ").replace("+", "over ");
            advice = jsonObject.getString("advice");

            app = jsonObject.getJSONObject("winning_percent");
            winPercHome = app.getString("home");
            winPercDraws = app.getString("draws");
            winPercAway = app.getString("away");

            JSONObject parent = jsonObject.getJSONObject("teams");
            home = new PredictionStatisticTeam(parent.getJSONObject("home"));
            away = new PredictionStatisticTeam(parent.getJSONObject("away"));

            cmp = new PredictionStatisticComparison(jsonObject.getJSONObject("comparison"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(){
        return matchWinner.isEmpty() && underOver.isEmpty() && goalsHome.isEmpty()
                && goalsAway.isEmpty() && advice.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixturePrediction)) return false;
        FixturePrediction that = (FixturePrediction) o;
        return matchWinner.equals(that.matchWinner) &&
                underOver.equals(that.underOver) &&
                goalsHome.equals(that.goalsHome) &&
                goalsAway.equals(that.goalsAway) &&
                advice.equals(that.advice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchWinner, underOver, goalsHome, goalsAway, advice);
    }

    @NonNull
    @Override
    public String toString() {
        return "FixturePrediction = {" +
                "matchWinner='" + matchWinner + '\'' +
                ", underOver='" + underOver + '\'' +
                ", goalsHome='" + goalsHome + '\'' +
                ", goalsAway='" + goalsAway + '\'' +
                ", advice='" + advice + '\'' +
                ", WinPercHome='" + winPercHome + '\'' +
                ", WinPercAway='" + winPercAway + '\'' +
                ", WinPercDraws='" + winPercDraws + '\'' +
                ", home=" + home +
                ", away=" + away +
                ", hToh=" + cmp +
                '}';
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public String getUnderOver() {
        return underOver;
    }

    public String getGoalsHome() {
        return goalsHome;
    }

    public String getGoalsAway() {
        return goalsAway;
    }

    public String getAdvice() {
        return advice;
    }

    public String getWinPercHome() {
        return winPercHome;
    }

    public String getWinPercAway() {
        return winPercAway;
    }

    public String getWinPercDraws() {
        return winPercDraws;
    }

    public PredictionStatisticTeam getHome() {
        return home;
    }

    public PredictionStatisticTeam getAway() {
        return away;
    }

    public PredictionStatisticComparison gethToh() {
        return cmp;
    }
}
