package com.example.covid_19symptomtracker.database;

public class Option {
    private int questionID;
    private int optionNum;
    private String option;

    public Option(int questionID, int optionNum, String option) {
        this.questionID = questionID;
        this.optionNum = optionNum;
        this.option = option;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(int optionNum) {
        this.optionNum = optionNum;
    }

    public String getOptionText() {
        return option;
    }

    public void setOptionText(String option) {
        this.option = option;
    }
}
