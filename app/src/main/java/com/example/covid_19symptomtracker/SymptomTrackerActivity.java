package com.example.covid_19symptomtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19symptomtracker.R;
import com.example.covid_19symptomtracker.StartingScreenActivity;
import com.example.covid_19symptomtracker.SurveyFinishedActivity;
import com.example.covid_19symptomtracker.database.DBHelper;
import com.example.covid_19symptomtracker.database.Option;
import com.example.covid_19symptomtracker.database.Question;
import com.example.covid_19symptomtracker.database.QuestionOption;
import com.example.covid_19symptomtracker.database.Response;
import com.example.covid_19symptomtracker.database.Result;
import com.example.covid_19symptomtracker.database.Survey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SymptomTrackerActivity extends AppCompatActivity {
    DBHelper db;
    Survey survey;
    ArrayList<QuestionOption> questionOptionList;
    QuestionOption currentQuestion;
    int questionIndex = 0;
    ArrayList<Result> resultList = new ArrayList<>();
    TextView questionTextView;
    LinearLayout optionGroup;
    ArrayList<CheckBox> currentCheckBoxes = new ArrayList<>();
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);

        db = DBHelper.getInstance(this);

//        clearAllTables();
//        insertQuestions();

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String date = dateFormat.format(new Date());
        survey = db.createSurvey(date);
        Log.d("Survey Created", "surveyID: " + survey.getId() + " Date: " + survey.getDate());

        questionOptionList = db.getAllQuestions();
        currentQuestion = questionOptionList.get(questionIndex);

        questionTextView = findViewById(R.id.textViewQuestion);
        optionGroup = findViewById(R.id.optionLayout);
        nextButton = findViewById(R.id.nextButton);

        setQuestionView();
        currentCheckBoxes = setOptionGroup();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Option> selectedOptions = getSelectedOptions();

                if(!selectedOptions.isEmpty()) {
                    ArrayList<Response> responses = new ArrayList<>();

                    for(Option option : selectedOptions) {
                        int surveyID = survey.getId();
                        int questionID = currentQuestion.getQuestion().getId();
                        int optionNum = option.getOptionNum();
                        int optionScore = option.getScore();
                        String response = option.getOptionText();
                        responses.add(new Response(surveyID, questionID, optionNum, response, optionScore));
                    }

                    Question question = currentQuestion.getQuestion();
                    resultList.add(new Result(question, responses));

                    optionGroup.removeAllViews();
                    questionIndex++;

                    if(questionIndex < questionOptionList.size()) {
                        currentQuestion = questionOptionList.get(questionIndex);
                        setQuestionView();
                        currentCheckBoxes = setOptionGroup();
                    } else {
                        db.saveResults(resultList);
                        goToSurveyFinished();
                    }
                }
            }
        });
    }

    private void goToSurveyFinished() {
        int score = 0;
        // add score from results
        Intent intent = new Intent(SymptomTrackerActivity.this, SurveyFinishedActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        db.removeSurveyWithID(survey.getId());
        Intent intent = new Intent(this, StartingScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public void setQuestionView() {
        questionTextView.setText(currentQuestion.getQuestion().getQuestionText());
    }

    public ArrayList<CheckBox> setOptionGroup() {
        ArrayList<Option> options = currentQuestion.getOptions();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();

        for(Option option : options) {
            String optionText = option.getOptionText();
            CheckBox newOption = new CheckBox(this);
            newOption.setId(option.getOptionNum());
            newOption.setText(optionText);
            newOption.setGravity(Gravity.LEFT);
            optionGroup.addView(newOption);
            checkBoxes.add(newOption);
        }

        return checkBoxes;
    }

    public ArrayList<Option> getSelectedOptions() {
        ArrayList<Option> selectedOptions = new ArrayList<>();

        for(CheckBox box : currentCheckBoxes) {
            if(box.isChecked()) {
                int questionID = currentQuestion.getQuestion().getId();
                int optionNum = box.getId();
                String optionText = box.getText().toString();
                ArrayList<Option> options = db.getAllOptionsForQuestion(questionID);
                int optionScore = 0;
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getOptionNum() == optionNum) {
                        optionScore = options.get(i).getScore();
                    }
                }
                selectedOptions.add(new Option(questionID, optionNum, optionText, optionScore));
                box.setChecked(false);
            }
        }

        return selectedOptions;
    }

    public void insertQuestions() {
        String emergencyQuestion = "Are you experiencing any of theses emergency warning signs for COVID-19? (Select any/all that apply)";
        ArrayList<String> emergencySymptoms = new ArrayList<>();
        ArrayList<Integer> symptomScore = new ArrayList<>();
        emergencySymptoms.add("Trouble breathing");
        symptomScore.add(10);
        emergencySymptoms.add("Persistent pain or pressure in the chest");
        symptomScore.add(10);
        emergencySymptoms.add("New confusion/disorientation");
        symptomScore.add(10);
        emergencySymptoms.add("Inability to wake after sleeping");
        symptomScore.add(10);
        emergencySymptoms.add("Bluish lips or face");
        symptomScore.add(10);
        emergencySymptoms.add("Any other symptoms that are severe that concern you");
        symptomScore.add(10);
        emergencySymptoms.add("None of the above");
        symptomScore.add(10);
        int questionID = db.createQuestion(emergencyQuestion, emergencySymptoms, symptomScore);
        Log.d("Emergency symptoms question created", "questionID: " + questionID + " question: " + emergencyQuestion);

        String commonQuestion = "Are you experiencing any of these common symptoms of COVID-19? (Select any/all that apply)";
        ArrayList<String> commonSymptoms = new ArrayList<>();
        symptomScore.clear();
        commonSymptoms.add("Fever");
        symptomScore.add(1);
        commonSymptoms.add("Cough");
        symptomScore.add(1);
        commonSymptoms.add("Shortness of breath");
        symptomScore.add(1);
        commonSymptoms.add("Chills");
        symptomScore.add(1);
        commonSymptoms.add("Repeated shaking with chills");
        symptomScore.add(1);
        commonSymptoms.add("Muscle pain");
        symptomScore.add(1);
        commonSymptoms.add("Headache");
        symptomScore.add(1);
        commonSymptoms.add("Sore throat");
        symptomScore.add(1);
        commonSymptoms.add("New loss of smell or taste");
        symptomScore.add(1);
        commonSymptoms.add("None of the above");
        symptomScore.add(1);
        long questionID1 = db.createQuestion(commonQuestion, commonSymptoms, symptomScore);
        Log.d("Common symptoms question created", "question_id: " + questionID1);
    }

    public void clearAllTables() {
        db.clearDatabase("survey");
        db.clearDatabase("question");
        db.clearDatabase("option");
        db.clearDatabase("response");
    }
}