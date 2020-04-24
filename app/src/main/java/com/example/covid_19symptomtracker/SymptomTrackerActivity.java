package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SymptomTrackerActivity extends AppCompatActivity {

    int question_id = 0;
    TextView question_text = (TextView) findViewById(R.id.textViewQuestion);
    QuestionSelect questionSelect = new QuestionSelect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);


        questionTracker();
    }

    public void questionTracker() {
        if (question_id == -1 ) {
            Intent start = new Intent(SymptomTrackerActivity.this, StartingScreenActivity.class);
            startActivity(start);
        } else if (question_id == QuestionSelect.questions.length) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            questionSelect.date = dateFormat.format(new Date());
            Intent results = new Intent(SymptomTrackerActivity.this, PastResultsScreen.class);
            startActivity(results);
        } else {
            Question question = QuestionSelect.questions[question_id];
            String questionContent = question.getContent();
            question_text.setText(questionContent);
        }
    }

    public void prevQuestion(View view) {
        question_id--;
        questionTracker();
    }

    public void nextQuestion(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextAnswer);
        QuestionSelect.answers[question_id] = editText.getText().toString();
        question_id++;
        questionTracker();
    }
}
