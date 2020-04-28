package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covid_19symptomtracker.database.DBHelper;
import com.example.covid_19symptomtracker.database.Result;
import com.example.covid_19symptomtracker.database.Survey;

import java.util.ArrayList;

public class SurveyFinishedActivity extends AppCompatActivity {
    private DBHelper db;
    ArrayList<Result> results = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_finished);

        Bundle bundle = getIntent().getExtras();
        int surveyID = bundle.getInt("surveyID");
        String date = bundle.getString("date");
        int numQuestions = bundle.getInt("numQuestions");

        Survey finishedSurvey = new Survey(surveyID, date);


        db = DBHelper.getInstance(this);
        results = db.getResultsForSurvey(finishedSurvey, numQuestions);
    }
}
