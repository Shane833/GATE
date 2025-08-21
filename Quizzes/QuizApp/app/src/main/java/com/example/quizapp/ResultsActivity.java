/*
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
*/

// Update 4
package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private LinearLayout resultsLayout;
    private TextView txtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsLayout = findViewById(R.id.resultsLayout);
        txtScore = findViewById(R.id.txtScore);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        ArrayList<Question> wrongQuestions = getIntent().getParcelableArrayListExtra("wrongQuestions");
        ArrayList<String> userResponses = getIntent().getStringArrayListExtra("userResponses");

        txtScore.setText("Score: " + score + "/" + total);

        if (wrongQuestions == null || userResponses == null) return;

        for (int i = 0; i < wrongQuestions.size(); i++) {
            Question q = wrongQuestions.get(i);
            String userAns = userResponses.get(i);

            // Question Text
            TextView qView = new TextView(this);
            qView.setText("Q: " + q.getText());
            qView.setTextColor(getResources().getColor(android.R.color.black));
            qView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            resultsLayout.addView(qView);

            // Question Image
            if (q.getImageUriString() != null) {
                addImageView(resultsLayout, q.getImageUriString());
            }

            // User Answer
            TextView userView = new TextView(this);
            userView.setText("Your Answer: " + userAns);
            userView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            resultsLayout.addView(userView);

            // Correct Answer
            TextView correctView = new TextView(this);
            correctView.setText("Correct Answer: " + q.getAnswer());
            correctView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            resultsLayout.addView(correctView);

            // Explanation Text
            TextView expView = new TextView(this);
            expView.setText("Explanation: " + q.getExplanation());
            expView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            resultsLayout.addView(expView);

            // Explanation Image
            if (q.getExplanationImageUriString() != null) {
                addImageView(resultsLayout, q.getExplanationImageUriString());
            }

            // Add a separator
            View separator = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
            params.setMargins(0, 24, 0, 24);
            separator.setLayoutParams(params);
            separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            resultsLayout.addView(separator);
        }
    }

    private void addImageView(LinearLayout layout, String uriString) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        imageView.setLayoutParams(params);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        layout.addView(imageView);

        Glide.with(this)
                .load(Uri.parse(uriString))
                .into(imageView);
    }
}