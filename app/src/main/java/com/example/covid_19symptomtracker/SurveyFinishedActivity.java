package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.covid_19symptomtracker.database.DBHelper;
import com.example.covid_19symptomtracker.database.Result;
import com.example.covid_19symptomtracker.database.Survey;

import java.util.ArrayList;

public class SurveyFinishedActivity extends AppCompatActivity {
    DBHelper db;
    ArrayList<Result> results = new ArrayList<>();
    TextView recommendation;
    final int liteRisk = 3;
    final int highRisk = 10;

    final String lowRiskMessage = "Based on your responses, it is recommended that you continue to monitor your symptoms and practice social distancing\n\n" +
            "You should avoid groups of people and stay six feet apart from anyone who's not part of your household. Especially avoid those showing symptoms.\n\n" +
            "In addition, you should wear a face mask when leaving the house. You should also clean your hands often and avoid touching your face.\n\n" +
            "As of now, your answers suggest that you do not need to get tested for COVID-19. If anything changes, take the questionnaire again.\n\n" +
            "See the CDC's website for more details:\n\n" + "https://www.cdc.gov/coronavirus/2019-ncov/index.html";
    final String mediumRiskMessage = "Based on your reported symptoms, you should self-isolate.\n\nYou should limit your contact with others including those in your home. You should stay" +
            " away from others for at least 7 days from when your symptoms first appeared. Your isolation can end if your symptoms improve significantly.\n\n" +
            "Continue to monitor your symptoms. If they get significantly worse, contact a doctor or a medical professional\n\n" +
            "See the CDC's website for more details:\n\n" + "https://www.cdc.gov/coronavirus/2019-ncov/index.html";
    final String highRiskMessage = "Based on your reported symptoms, you should seek care immediately.\n\nSee the CDC's website for more details:\n\n"
            + "https://www.cdc.gov/coronavirus/2019-ncov/index.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_finished);

        recommendation = findViewById(R.id.recommendationTextView);

        Bundle bundle = this.getIntent().getExtras();
        int score = bundle.getInt("score");
        int surveyID = bundle.getInt("surveyID");

        db = DBHelper.getInstance(this);

        if (score <= liteRisk) {
            recommendation.setText(lowRiskMessage);
            db.saveRecommendationForSurvey(surveyID, lowRiskMessage);
        } else if (score > liteRisk  && score < highRisk) {
            recommendation.setText(mediumRiskMessage);
            db.saveRecommendationForSurvey(surveyID, mediumRiskMessage);
        } else if (score >= highRisk) {
            recommendation.setText(highRiskMessage);
            db.saveRecommendationForSurvey(surveyID, highRiskMessage);
        }

        recommendation.setTextColor(Color.BLACK);
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
