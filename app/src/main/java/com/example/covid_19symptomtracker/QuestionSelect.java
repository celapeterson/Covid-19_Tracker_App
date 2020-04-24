package com.example.covid_19symptomtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class QuestionSelect {
    public static Question[] questions;
    public static String[] answers;
    public String date;

    public QuestionSelect() {
        Question Question0 = new Question("This is Question 0", 0);
        Question Question1 = new Question("This is Question 1", 1);
        Question Question2 = new Question("This is Question 2", 2);
        Question Question3 = new Question("This is Question 3", 3);
        Question Question4 = new Question("This is Question 4", 4);
        Question Question5 = new Question("This is Question 5", 5);
        questions = new Question[]{Question0, Question1, Question2, Question3, Question4, Question5};
    }

}
