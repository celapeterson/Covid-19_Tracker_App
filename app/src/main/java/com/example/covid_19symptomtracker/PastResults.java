package com.example.covid_19symptomtracker;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19symptomtracker.database.DBHelper;
import com.example.covid_19symptomtracker.database.Survey;

import java.util.ArrayList;

public class PastResults extends AppCompatActivity {
    public static DBHelper db;
    public static ArrayList<Survey> surveys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_results_screen);

        db = DBHelper.getInstance(this);
        surveys = db.getSurveys();

        ArrayList<String> displaySurveys = new ArrayList<>();
        for (Survey survey : surveys) {
            displaySurveys.add(String.format("Symptom Results From:\n" + survey.getDate()));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displaySurveys);
        ListView listView = findViewById(R.id.surveyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewResult.class);
                Integer i = new Integer(position);
                intent.putExtra("survey_id", i.toString());
                startActivity(intent);
            }
        });
    }

    public void backToStart(View view) {
        Intent intent = new Intent(this, StartingScreenActivity.class);
        startActivity(intent);
    }
}
