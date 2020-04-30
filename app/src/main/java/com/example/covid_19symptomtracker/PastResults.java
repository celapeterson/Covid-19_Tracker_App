package com.example.covid_19symptomtracker;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19symptomtracker.database.Survey;

import java.util.ArrayList;

public class PastResults extends AppCompatActivity {
    public static ArrayList<Survey> surveys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_results_screen);

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("SymptomTrackerActivity.class",
                Context.MODE_PRIVATE,null);
    }

    public void backButtonOnClick(View view) {
        Intent intent = new Intent(this, StartingScreenActivity.class);
        startActivity(intent);
    }
}
