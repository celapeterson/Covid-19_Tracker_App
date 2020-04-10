package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void symptomCheckButton(View view) {
        Intent intent = new Intent(this, symptomcheck.class);
        startActivity(intent);
    }

    public void symptomHistoryButton(View view) {
        Intent intent = new Intent(this, symptomHistory.class);
        startActivity(intent);
    }

    public void nearMeButton(View view) {
        Intent intent = new Intent(this, nearMe.class);
        startActivity(intent);
    }

    public void helpAvailableButton(View view) {
        Intent intent = new Intent(this, helpAvailable.class);
        startActivity(intent);
    }
}
