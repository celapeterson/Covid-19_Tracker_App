package com.example.covid_19symptomtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19symptomtracker.database.QuestionOption;
import com.example.covid_19symptomtracker.database.Result;
import com.example.covid_19symptomtracker.database.Survey;

import java.util.ArrayList;

public class ViewResult extends AppCompatActivity {
    TextView textView;
    LinearLayout resultsLayout;
    TextView resultsForTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_result_activity);

//        textView = findViewById(R.id.textQuestionView);
        resultsLayout = findViewById(R.id.resultsLayout);
        resultsForTextView = findViewById(R.id.resultsForTextView);

        final Intent intent = getIntent();
        int survey_id = Integer.parseInt(intent.getStringExtra("survey_id"));
        Survey survey = PastResults.surveys.get(survey_id);
        String date = survey.getDate();
        resultsForTextView.setText("Results for: " + date);

        ArrayList<QuestionOption> questions = PastResults.db.getAllQuestions();
        // numQuestion to be changed to the total number on questions
        ArrayList<Result> results = PastResults.db.getResultsForSurvey(survey, 5);

        String resultsView = "";
        for (int i = 0; i < questions.size(); i++) {
            resultsView = resultsView.concat(i+1 + ") " + questions.get(i).getQuestion().getQuestionText());
            resultsView = resultsView.concat("\n");
            if(results.get(i).getResponses().isEmpty()) {
                resultsView = resultsView.concat("- No options selected\n");
            } else {
                for (int j = 0; j < results.get(i).getResponses().size(); j++) {
                    resultsView = resultsView.concat("- " + results.get(i).getResponses().get(j).getResponse());
                    resultsView = resultsView.concat("\n");
                }
            }

            resultsView = resultsView.concat("\n");
        }

        textView = new TextView(this);
        textView.setText(resultsView);
        textView.setTextColor(Color.BLACK);
        resultsLayout.addView(textView);
    }

    public void backToPastResults(View view) {
        Intent intent = new Intent(this, PastResults.class);
        startActivity(intent);
    }
}