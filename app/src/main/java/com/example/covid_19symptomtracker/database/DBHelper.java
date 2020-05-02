package com.example.covid_19symptomtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Logcat tag
    private static final String LOG = "DBHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SymptomTrackerDB";

    // Table Names
    private static final String TABLE_SURVEY = "survey";
    private static final String TABLE_QUESTION = "question";
    private static final String TABLE_OPTION = "option";
    private static final String TABLE_RESPONSE = "response";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Survey Table - column names
    private static final String KEY_SID = "survey_id";
    private static final String KEY_DATE = "date";

    // Question Table - column names
    private static final String KEY_QID = "question_id";
    private static final String KEY_QUESTION = "question";

    // Option Table - column names
    private static final String KEY_QUESTION_ID= "question_id";
    private static final String KEY_OPTION_NUM = "option_num";
    private static final String KEY_OPTION_SCORE = "option_score";
    private static final String KEY_OPTION = "option";

    // Response Table - column names
    private static final String KEY_SURVEY_ID = "survey_id";
    private static final String KEY_RESPONSE = "response";

    // Table Create Statements
    private static final String CREATE_TABLE_SURVEY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SURVEY + "(" + KEY_SID + " INTEGER PRIMARY KEY," + KEY_DATE
            + " TEXT" + ")";

    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_QUESTION + "(" + KEY_QID + " INTEGER PRIMARY KEY," + KEY_QUESTION + " TEXT" + ")";

    private static final String CREATE_TABLE_OPTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_OPTION + "(" + KEY_QUESTION_ID + " INTEGER NOT NULL," + KEY_OPTION_NUM +
            " INTEGER NOT NULL," + KEY_OPTION + " TEXT," + " PRIMARY KEY (" + KEY_QUESTION_ID
            + ", " + KEY_OPTION_NUM + "))";

    private static final String CREATE_TABLE_RESPONSE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_RESPONSE + "(" + KEY_SURVEY_ID + " INTEGER NOT NULL," + KEY_QUESTION_ID +
            " INTEGER NOT NULL," + KEY_OPTION_NUM + " INTEGER NOT NULL,"+ KEY_RESPONSE + " TEXT,"
            + " PRIMARY KEY (" + KEY_SURVEY_ID + ", " + KEY_QUESTION_ID + ", " + KEY_OPTION_NUM + "))";

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SURVEY);
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_OPTION);
        db.execSQL(CREATE_TABLE_RESPONSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSE);

        // create new tables
        onCreate(db);
    }

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_NAME;
        db.execSQL(clearDBQuery);
        db.close();
    }


    public Survey createSurvey(String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);

        long surveyID = db.insert(TABLE_SURVEY, null, values);

        db.close();

        Survey survey = new Survey((int) surveyID, date);

        return survey;
    }

    public ArrayList<Survey> getSurveys() {
        ArrayList<Survey> surveys = new ArrayList<>();

        String selectQuery = "SELECT * FROM survey ORDER BY survey_id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        int idIndex = c.getColumnIndex(KEY_SID);
        int dateIndex = c.getColumnIndex(KEY_DATE);

        if(c.moveToFirst()) {
            do {
                int surveyID = c.getInt(idIndex);
                String dateText = c.getString(dateIndex);
                Survey survey = new Survey(surveyID, dateText);
                surveys.add(survey);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return surveys;
    }

    public void removeSurveyWithID(int surveyID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SURVEY, KEY_SID + "=" + surveyID, null);
    }

    public int createQuestion(String question, ArrayList<String> options, ArrayList<Integer> symptomScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question);

        long questionID = db.insert(TABLE_QUESTION, null, values);

        for(int i = 0; i < options.size(); i++) {
            int optionNum = i + 1;
            createOption(questionID, optionNum, options.get(i), symptomScore.get(i));
        }

        db.close();
        return (int) questionID;
    }

    public void createOption(long questionID, int optionNum, String option, int optionScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, questionID);
        values.put(KEY_OPTION_NUM, optionNum);
        values.put(KEY_OPTION_SCORE, optionScore);
        values.put(KEY_OPTION, option);

        db.insert(TABLE_OPTION, null, values);

        db.close();
    }

    public ArrayList<QuestionOption> getAllQuestions() {
        ArrayList<QuestionOption> questions = new ArrayList<>();

        String selectQuery = "SELECT * FROM question";
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        int questionIDIndex = c.getColumnIndex(KEY_QID);
        int questionIndex = c.getColumnIndex(KEY_QUESTION);

        if(c.moveToFirst()) {
            do {
                int questionID = c.getInt(questionIDIndex);
                String questionText = c.getString(questionIndex);
                Question question = new Question(questionID, questionText);
                ArrayList<Option> options = getAllOptionsForQuestion(questionID);
                questions.add(new QuestionOption(question, options));
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return questions;
    }

    public ArrayList<Option> getAllOptionsForQuestion(int questionID) {
        ArrayList<Option> options = new ArrayList<>();

        String selectQuery = String.format("SELECT * FROM option WHERE question_id LIKE '%d' ORDER BY option_num ASC", questionID);
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        int optionNumIndex = c.getColumnIndex(KEY_OPTION_NUM);
        int optionScoreIndex = c.getColumnIndex(KEY_OPTION_SCORE);
        int optionIndex = c.getColumnIndex(KEY_OPTION);

        if(c.moveToFirst()) {
            do {
                int optionNum = c.getInt(optionNumIndex);
                int optionScore = c.getInt(optionScoreIndex);
                String optionText = c.getString(optionIndex);
                Option option = new Option(questionID, optionNum, optionText, optionScore);
                options.add(option);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return options;
    }

    public void saveResults(ArrayList<Result> results) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(Result result : results) {
            ArrayList<Response> responses = result.getResponses();
            for(Response response : responses) {
                ContentValues values = new ContentValues();
                values.put(KEY_SURVEY_ID, response.getSurveyID());
                values.put(KEY_QUESTION_ID, response.getQuestionID());
                values.put(KEY_OPTION_NUM, response.getOptionNum());
                values.put(KEY_RESPONSE, response.getResponse());

                long id = db.insert(TABLE_RESPONSE, null, values);
                if(id == -1) {
                    Log.d("Insert Failed", "Failure");
                }
            }
        }

        db.close();
    }

    public ArrayList<Result> getResultsForSurvey(Survey survey, int numQuestions) {
        ArrayList<Result> results = new ArrayList<>();

        for(int i = 0; i < numQuestions; i++) {
            Question question = getQuestionForId(i+1);
            ArrayList<Response> responses = new ArrayList<>();

            String selectQuery = String.format("SELECT * FROM response WHERE survey_id LIKE '%d' AND question_id " +
                                                "LIKE '%d' ORDER BY option_num ASC", survey.getId(), question.getId());
            Log.e(LOG, selectQuery);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

//            int surveyIDIndex = c.getColumnIndex(KEY_SURVEY_ID);
//            int questionIDIndex = c.getColumnIndex(KEY_QUESTION_ID);
            int optionNumIndex = c.getColumnIndex(KEY_OPTION_NUM);
            int optionScoreIndex = c.getColumnIndex(KEY_OPTION_SCORE);
            int responseIndex = c.getColumnIndex(KEY_RESPONSE);

            if(c.moveToFirst()) {
                do {
                    int optionNum = c.getInt(optionNumIndex);
                    int optionScore = c.getInt(optionScoreIndex);
                    String responseText = c.getString(responseIndex);
                    Response response = new Response(survey.getId(), question.getId(), optionNum, responseText, optionScore);
                    responses.add(response);
                } while(c.moveToNext());
            }

            Result result = new Result(question, responses);
            results.add(result);

            c.close();
            db.close();
        }

        return results;
    }

    public Question getQuestionForId(int questionID) {
        String selectQuery = String.format("SELECT * FROM question WHERE question_id LIKE '%d'", questionID);
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        int questionIDIndex = c.getColumnIndex(KEY_QUESTION_ID);
        int questionIndex = c.getColumnIndex(KEY_QUESTION);

        if(c != null) {
            c.moveToFirst();
        }

        int id = c.getInt(questionIDIndex);
        String questionText = c.getString(questionIndex);

        Question question = new Question(id, questionText);

        c.close();
        db.close();
        return question;
    }
}
