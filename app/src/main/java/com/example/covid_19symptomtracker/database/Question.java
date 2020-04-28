package com.example.covid_19symptomtracker.database;

public class Question {
    private int id;
    private String question;

    public Question(int id, String question) {
        this.id = id;
        this.question = question;
    }

    public Question(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return question;
    }

    public void setQuestionText(String question) {
        this.question = question;
    }
}
