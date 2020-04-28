package com.example.covid_19symptomtracker.database;

import java.util.ArrayList;

public class Result {
    private Question question;
    private ArrayList<Response> responses;

    public Result(Question question, ArrayList<Response> responses) {
        this.question = question;
        this.responses = responses;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<Response> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<Response> responses) {
        this.responses = responses;
    }
}
