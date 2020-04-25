package com.example.covid_19symptomtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SymptomTrackerActivity extends AppCompatActivity {

    int question_id;
    TextView question_text;
    EditText answer_text;
    Question[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);

        question_text = (TextView) findViewById(R.id.textViewQuestion);
        answer_text = (EditText) findViewById(R.id.editTextAnswer);

        Question question0 = new Question("This is Question 0");
        Question question1 = new Question("This is Question 1");
        Question question2 = new Question("This is Question 2");
        Question question3 = new Question("This is Question 3");
        Question question4 = new Question("This is Question 4");
        Question question5 = new Question("This is Question 5");
        questions = new Question[]{question0, question1, question2, question3, question4, question5};

        question_id = 0;

        String questionContent = questions[question_id].getContent();
        question_text.setText(questionContent);
    }

    public void questionTracker() {
        if (question_id == -1 ) {
            Intent start = new Intent(SymptomTrackerActivity.this, StartingScreenActivity.class);
            startActivity(start);
        } else if (question_id == questions.length) {
            Intent results = new Intent(SymptomTrackerActivity.this, SymptomTrackerActivity.class);
            startActivity(results);
        } else {
            String questionContent = questions[question_id].getContent();
            question_text.setText(questionContent);

            String answerContent = questions[question_id].answer;
            answer_text.setText(answerContent);
        }
    }

    public void prevQuestion(View view) {
        question_id--;
        questionTracker();
    }

    public void nextQuestion(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextAnswer);
        questions[question_id].answer = editText.getText().toString();
        question_id++;
        questionTracker();
    }
}
