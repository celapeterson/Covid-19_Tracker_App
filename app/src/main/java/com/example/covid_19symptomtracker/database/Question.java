package com.example.covid_19symptomtracker.database;

public class Question {
    private int id;
    private String question;
    private int type;

    public Question(int id, String question, int type) {
        this.id = id;
        this.question = question;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
