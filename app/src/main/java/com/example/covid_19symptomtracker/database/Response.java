package com.example.covid_19symptomtracker.database;

public class Response {
    private int surveyID;
    private int questionID;
    private int optionNum;
    private String response;

    public Response(int surveyID, int questionID, int optionNum, String response) {
        this.surveyID = surveyID;
        this.questionID = questionID;
        this.optionNum = optionNum;
        this.response = response;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
