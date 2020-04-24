package com.example.covid_19symptomtracker;

public class Question {
    private static String content;
    private int num;

    public Question(String content, int num) {
        this.content = content;
        this.num = num;
    }

    public static String getContent() {return content;}

    public int getNum() {return num;}
}
