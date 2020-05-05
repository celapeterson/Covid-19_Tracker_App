package com.example.covid_19symptomtracker.database;

public class Survey {
    private int id;
    private String date;
    private String recommendation;

    public Survey(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public Survey(int id, String date, String recommendation) {
        this.id = id;
        this.date = date;
        this.recommendation = recommendation;
    }

    public Survey(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
