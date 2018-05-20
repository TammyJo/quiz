package com.example.android.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import static java.lang.String.*;

public class MainActivity extends AppCompatActivity {

    // Instance variables
    final int[] scorecard = new int[8];
    RadioButton q1CorrectAnswer;
    RadioButton q2CorrectAnswer;
    RadioButton q3CorrectAnswer;
    RadioButton q4CorrectAnswer;
    RadioButton q5CorrectAnswer;
    CheckBox q6CorrectAnswer1;
    CheckBox q6IncorrectAnswer1;
    CheckBox q6CorrectAnswer2;
    CheckBox q6CorrectAnswer3;
    CheckBox q7IncorrectAnswer1;
    CheckBox q7CorrectAnswer1;
    CheckBox q7CorrectAnswer2;
    EditText responseQ8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    /**
     * Initialize the views when the app loads
     */
    public void initializeViews() {
        q1CorrectAnswer = (RadioButton) findViewById(R.id.radio_q1_opt2);
        q2CorrectAnswer = (RadioButton) findViewById(R.id.radio_q2_opt3);
        q3CorrectAnswer = (RadioButton) findViewById(R.id.radio_q3_opt1);
        q4CorrectAnswer = (RadioButton) findViewById(R.id.radio_q4_opt2);
        q5CorrectAnswer = (RadioButton) findViewById(R.id.radio_q5_opt3);
        q6CorrectAnswer1 = (CheckBox) findViewById(R.id.checkbox_q6_opt1);
        q6IncorrectAnswer1 = (CheckBox) findViewById(R.id.checkbox_q6_opt2);
        q6CorrectAnswer2 = (CheckBox) findViewById(R.id.checkbox_q6_opt3);
        q6CorrectAnswer3 = (CheckBox) findViewById(R.id.checkbox_q6_opt4);
        q7IncorrectAnswer1 = (CheckBox) findViewById(R.id.checkbox_q7_opt1);
        q7CorrectAnswer1 = (CheckBox) findViewById(R.id.checkbox_q7_opt2);
        q7CorrectAnswer2 = (CheckBox) findViewById(R.id.checkbox_q7_opt3);
        responseQ8 = (EditText) findViewById(R.id.input_q8);
    }

    /**
     * Submit button is clicked
     *
     * @param view
     */
    public void submitQuiz(View view) {
        boolean pass;
        int score = calculateScore();
        if (score >= 8) {
            pass = true;
        } else {
            pass = false;
        }
        toastGrade(score, pass);
        sendEmail(createResultsSummary());
    }

    /**
     * Calculate score
     *
     * @return int score
     */
    public int calculateScore() {
        // Store 0 for incorrect / 1 for correct for each question
        int score = 0;
        // Question 1
        if (q1CorrectAnswer.isChecked()) {
            scorecard[0] = 1;
            score++;
        }
        // Question 2
        if (q2CorrectAnswer.isChecked()) {
            scorecard[1] = 1;
            score++;
        }
        // Question 3
        if (q3CorrectAnswer.isChecked()) {
            scorecard[2] = 1;
            score++;
        }
        // Question 4
        if (q4CorrectAnswer.isChecked()) {
            scorecard[3] = 1;
            score++;
        }
        // Question 5
        if (q5CorrectAnswer.isChecked()) {
            scorecard[4] = 1;
            score++;
        }
        // Question 6
        if (q6CorrectAnswer1.isChecked() && q6CorrectAnswer2.isChecked() && q6CorrectAnswer3.isChecked() && !q6IncorrectAnswer1.isChecked()) {
            scorecard[5] = 1;
            score++;
        }
        // Question 7
        if (q7CorrectAnswer1.isChecked() && q7CorrectAnswer2.isChecked() && !q7IncorrectAnswer1.isChecked()) {
            scorecard[6] = 1;
            score++;
        }
        // Question 8
        String response = responseQ8.getText().toString().trim();
        if (response.equalsIgnoreCase(getString(R.string.question8_response1)) || response.equalsIgnoreCase(getString(R.string.question8_response2))) {
            scorecard[7] = 1;
            score++;
        }
        return score;
    }

    /**
     * Create a formatted summary of the results
     *
     * @return String summary
     */
    private String createResultsSummary() {
        String resultsSummary = "";
        for (int i = 0; i < scorecard.length; i++) {
            int quesNumber = i + 1;
            String answerResult = getString(R.string.wrong); // default the answer to incorrect
            if (scorecard[i] == 1) {
                answerResult = getString(R.string.correct);
            }
            Resources res = getResources();
            @SuppressLint("StringFormatMatches") String summary = String.format(res.getString(R.string.summary_item), answerResult, quesNumber);
            //@SuppressLint("StringFormatMatches") String summaryItem = format(getString(R.string.summary_item), quesNumber, answerResult);
            resultsSummary += summary + "\n";
        }
        return resultsSummary;
    }

    /**
     * Display a toast with the quiz grade
     *
     * @parama int score
     */
    private void toastGrade(int score, boolean pass) {
        String finalMessage;
        @SuppressLint("StringFormatMatches") String msg = format(getString(R.string.result_msg), scorecard.length, score);
        if (pass) {
            String passMsg = format(getString(R.string.pass_msg));
            finalMessage = passMsg + ' ' + msg;
        } else {
            String failMsg = format(getString(R.string.fail_msg));
            finalMessage = failMsg + ' ' + msg;
        }
        Toast.makeText(this, finalMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Send email with results
     *
     * @param String messageBody
     */
    private void sendEmail(String messageBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, messageBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
