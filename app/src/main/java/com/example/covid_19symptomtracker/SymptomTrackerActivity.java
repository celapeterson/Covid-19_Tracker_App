package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private DBHelper db;
    int questionIndex = 0;
    ArrayList<QuestionOption> questionOptionList;
    ArrayList<Result> resultList;
    ArrayList<Response> responses;
    QuestionOption currentQuestion;
    TextView questionTextView;
    LinearLayout optionGroup;
    ArrayList<CheckBox> currentCheckBoxes;
    Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);

        db = DBHelper.getInstance(this);

        db.clearDatabase("survey");
        db.clearDatabase("question");
        db.clearDatabase("option");
        db.clearDatabase("response");

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());
        final Survey survey = db.createSurvey(date);
        Log.d("Survey Created", "surveyID: " + survey.getId() + " Date: " + survey.getDate());

        String testQuestion = "This question is a test.";
        ArrayList<String> testOptions = new ArrayList<>();
        testOptions.add("Option 1");
        testOptions.add("Option 2");
        testOptions.add("Option 3");
        int questionID = db.createQuestion(testQuestion, testOptions);
        Log.d("Question Created", "questionID: " + questionID + " question: " + testQuestion);

        String testQuestion1 = "This a second test question.";
        ArrayList<String> testOptions1 = new ArrayList<>();
        testOptions1.add("Option 1");
        testOptions1.add("Option 2");
        testOptions1.add("Option 3");
        testOptions1.add("Option 4");
        long questionID1 = db.createQuestion(testQuestion1, testOptions1);
        Log.d("question_ID", "question_id: " + questionID1);

        questionOptionList = db.getAllQuestions();
        resultList = new ArrayList<>();
        currentQuestion = questionOptionList.get(questionIndex);
        questionTextView = (TextView) findViewById(R.id.textViewQuestion);
        optionGroup = (LinearLayout) findViewById(R.id.optionLayout);
        nextButton = (Button) findViewById(R.id.nextButton);
        setQuestionView();
        currentCheckBoxes = setOptionGroup();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Option> selectedOptions = getSelectedOptions();
                responses = new ArrayList<>();
                for(Option option : selectedOptions) {
                    int surveyID = survey.getId();
                    int questionID = currentQuestion.getQuestion().getId();
                    int optionNum = option.getOptionNum();
                    String response = option.getOptionText();
                    responses.add(new Response(surveyID, questionID, optionNum, response));
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
                    Bundle bundle = new Bundle();
                    bundle.putInt("surveyID", survey.getId());
                    bundle.putString("date", survey.getDate());
                    bundle.putInt("numQuestions", questionIndex);
                    Intent intent = new Intent(SymptomTrackerActivity.this, SurveyFinishedActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
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
            newOption.setGravity(Gravity.CENTER);
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
                selectedOptions.add(new Option(questionID, optionNum, optionText));
                box.setChecked(false);
            }
        }

        return selectedOptions;
    }
}