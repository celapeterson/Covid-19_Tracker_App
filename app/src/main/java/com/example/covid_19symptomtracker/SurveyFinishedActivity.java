package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.covid_19symptomtracker.database.DBHelper;
import com.example.covid_19symptomtracker.database.Result;

import java.util.ArrayList;

public class SurveyFinishedActivity extends AppCompatActivity {
    DBHelper db;
    ArrayList<Result> results = new ArrayList<>();
    TextView recommendation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_finished);

        recommendation = findViewById(R.id.recommendationTextView);

        final int liteRisk = 2;
        final int highRisk = 10;

        Bundle bundle = this.getIntent().getExtras();
        int score = bundle.getInt("score");

        if (score < 2) {
            recommendation.setText("Stay Home");
        } else if (2 <= score && score < 10) {
            recommendation.setText("Stay Home and Call Doctor for Advice");
        } else if (score >= 10) {
            recommendation.setText("Call Doctor");
        }
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
