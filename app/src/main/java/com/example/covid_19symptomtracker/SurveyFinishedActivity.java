package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.covid_19symptomtracker.database.DBHelper;
import com.example.covid_19symptomtracker.database.Result;

import java.util.ArrayList;

public class SurveyFinishedActivity extends AppCompatActivity {
    DBHelper db;
    ArrayList<Result> results = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_finished);

//        Bundle bundle = getIntent().getExtras();
//        int surveyID = bundle.getInt("surveyID");
//        String date = bundle.getString("date");
//        int numQuestions = bundle.getInt("numQuestions");
//
//        Survey finishedSurvey = new Survey(surveyID, date);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, StartingScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public void finishedOnClick(View view) {
        Intent intent = new Intent(this, StartingScreenActivity.class);
        startActivity(intent);
    }

    public void pastResultsOnClick(View view) {
        Intent intent = new Intent(this, PastResults.class);
        startActivity(intent);
    }
}
