package com.example.covid_19symptomtracker.database;

import java.util.ArrayList;

public class QuestionOption {
    private Question question;
    private ArrayList<Option> options;

    public QuestionOption(Question question, ArrayList<Option> options) {
        this.question = question;
        this.options = options;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
}
