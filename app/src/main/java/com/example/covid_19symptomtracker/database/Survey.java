package com.example.covid_19symptomtracker.database;

public class Survey {
    private int id;
    private String date;
    private int numQuestions;

    public Survey(int id, String date) {
        this.id = id;
        this.date = date;
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
}
