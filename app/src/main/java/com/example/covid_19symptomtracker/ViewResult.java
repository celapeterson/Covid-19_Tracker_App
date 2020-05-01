package com.example.covid_19symptomtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19symptomtracker.database.QuestionOption;
import com.example.covid_19symptomtracker.database.Result;
import com.example.covid_19symptomtracker.database.Survey;

import java.util.ArrayList;

public class ViewResult extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_result_activity);

        textView = findViewById(R.id.textViewQuestion);
        final Intent intent = getIntent();
        int survey_id = Integer.parseInt(intent.getStringExtra("survey_id"));

        Survey survey = PastResults.surveys.get(survey_id);
        ArrayList<QuestionOption> questions = PastResults.db.getAllQuestions();
        // numQuestion to be changed to the total number on questions
        ArrayList<Result> results = PastResults.db.getResultsForSurvey(survey, 1);

        String resultsView = "";
        for (int i = 0; i < questions.size(); i++) {
            resultsView = resultsView.concat(questions.get(i).getQuestion().getQuestionText());
            resultsView = resultsView.concat("\n");
            for (int j = 0; j < results.get(i).getResponses().size(); j++) {
                resultsView = resultsView.concat(results.get(i).getResponses().get(j).getResponse());
                resultsView = resultsView.concat("\n");
            }
            resultsView = resultsView.concat("\n");
        }

        textView.setText(resultsView);
    }

    public void backToPastResults(View view) {
        Intent intent = new Intent(this, PastResults.class);
        startActivity(intent);
    }
}