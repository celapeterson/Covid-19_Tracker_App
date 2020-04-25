package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
    }

    public void startTracker(View view) {
        Intent intent = new Intent(this, SymptomTrackerActivity.class);
        startActivity(intent);
    }

    public void resultsButtonOnClick(View view) {
        Intent intent = new Intent(this, PastResultsScreen.class);
        startActivity(intent);

    }

}
