package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private LinearLayout resultsLayout;
    private TextView txtScore;
    private ArrayList<Question> wrongQuestions;
    private ArrayList<String> userResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsLayout = findViewById(R.id.resultsLayout);
        txtScore = findViewById(R.id.txtScore);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        wrongQuestions = (ArrayList<Question>) getIntent().getSerializableExtra("wrongQuestions");
        userResponses = (ArrayList<String>) getIntent().getSerializableExtra("userResponses");

        txtScore.setText("Score: " + score + "/" + total);

        for (int i = 0; i < wrongQuestions.size(); i++) {
            Question q = wrongQuestions.get(i);
            String userAns = userResponses.get(i);

            TextView qView = new TextView(this);
            qView.setText("Q: " + q.getText());
            qView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            resultsLayout.addView(qView);

            TextView userView = new TextView(this);
            userView.setText("Your Answer: " + userAns);
            userView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            resultsLayout.addView(userView);

            TextView correctView = new TextView(this);
            correctView.setText("Correct Answer: " + q.getAnswer());
            correctView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            resultsLayout.addView(correctView);

            TextView expView = new TextView(this);
            expView.setText("Explanation:\n" + q.getExplanation());
            expView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            resultsLayout.addView(expView);
        }
    }
}
