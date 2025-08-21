/*
package com.example.quizapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private Button btnSubmit;
    private TextView txtQuizName, txtQuestionCount;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtQuizName = findViewById(R.id.txtQuizName);
        txtQuestionCount = findViewById(R.id.txtQuestionCount);

        String quizName = getIntent().getStringExtra("quizName");
        questions = (List<Question>) getIntent().getSerializableExtra("questions");

        txtQuizName.setText(quizName);
        txtQuestionCount.setText("Q1 / " + questions.size());

        adapter = new QuizAdapter(questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisible = layoutManager.findFirstVisibleItemPosition() + 1;
                txtQuestionCount.setText("Q" + firstVisible + " / " + questions.size());
            }
        });

        btnSubmit.setOnClickListener(v -> evaluateQuiz());
    }

    private void evaluateQuiz() {
        Map<Integer, Object> userAnswers = adapter.getUserAnswers();
        int score = 0;
        StringBuilder explanationText = new StringBuilder();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Object userAns = userAnswers.get(i);

            boolean correct = false;
            if (q.getType().equalsIgnoreCase("MCQ")) {
                correct = (userAns != null && userAns.equals(q.getAnswer()));
            } else if (q.getType().equalsIgnoreCase("MSQ")) {
                if (userAns instanceof ArrayList) {
                    ArrayList<String> selected = (ArrayList<String>) userAns;
                    correct = selected.containsAll(List.of(q.getAnswer().split(",")))
                            && List.of(q.getAnswer().split(",")).containsAll(selected);
                }
            } else if (q.getType().equalsIgnoreCase("NAT")) {
                correct = (userAns != null && userAns.equals(q.getAnswer()));
            }

            if (correct) {
                score++;
            } else {
                explanationText.append("\nQ").append(i + 1).append(": ").append(q.getText())
                        .append("\nYour Answer: ").append(userAns)
                        .append("\nCorrect Answer: ").append(q.getAnswer())
                        .append("\nExplanation: ").append(q.getExplanation()).append("\n");
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Results");
        builder.setMessage("Score: " + score + "/" + questions.size() + "\n" + explanationText.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
Original Code
*/

/*
package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private Button btnSubmit;
    private TextView txtQuizName, txtQuestionCount;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtQuizName = findViewById(R.id.txtQuizName);
        txtQuestionCount = findViewById(R.id.txtQuestionCount);

        String quizName = getIntent().getStringExtra("quizName");
        questions = (List<Question>) getIntent().getSerializableExtra("questions");

        txtQuizName.setText(quizName);
        txtQuestionCount.setText("Q1 / " + questions.size());

        adapter = new QuizAdapter(questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisible = layoutManager.findFirstVisibleItemPosition() + 1;
                txtQuestionCount.setText("Q" + firstVisible + " / " + questions.size());
            }
        });

        // UPDATED: The listener now calls the new evaluateQuiz method
        btnSubmit.setOnClickListener(v -> evaluateQuiz(adapter.getUserAnswers()));
    }

    // UPDATED: This method now launches the ResultsActivity instead of an AlertDialog
    private void evaluateQuiz(Map<Integer, Object> userAnswers) {
        int score = 0;
        ArrayList<Question> wrongQuestions = new ArrayList<>();
        ArrayList<String> userResponsesForWrong = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Object userAns = userAnswers.getOrDefault(i, ""); // Use getOrDefault for safety

            boolean correct = false;
            if (q.getType().equalsIgnoreCase("MCQ") || q.getType().equalsIgnoreCase("NAT")) {
                correct = userAns.toString().equalsIgnoreCase(q.getAnswer());
            } else if (q.getType().equalsIgnoreCase("MSQ")) {
                if (userAns instanceof ArrayList) {
                    // Robust check for list equality, ignoring order
                    ArrayList<String> selected = (ArrayList<String>) userAns;
                    ArrayList<String> correctAnswers = new ArrayList<>(List.of(q.getAnswer().split(",")));
                    Collections.sort(selected);
                    Collections.sort(correctAnswers);
                    correct = selected.equals(correctAnswers);
                }
            }

            if (correct) {
                score++;
            } else {
                wrongQuestions.add(q);
                userResponsesForWrong.add(userAns.toString());
            }
        }

        // Launch the Results Activity with all the necessary data
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());
        intent.putExtra("wrongQuestions", wrongQuestions);
        intent.putExtra("userResponses", userResponsesForWrong);
        startActivity(intent);
        finish(); // Finish this activity so the user can't go back to the quiz
    }
}
 */

// Update 4
package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private Button btnSubmit;
    private TextView txtQuizName, txtQuestionCount;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtQuizName = findViewById(R.id.txtQuizName);
        txtQuestionCount = findViewById(R.id.txtQuestionCount);

        String quizName = getIntent().getStringExtra("quizName");
        questions = getIntent().getParcelableArrayListExtra("questions");

        txtQuizName.setText(quizName);
        txtQuestionCount.setText("Q1 / " + questions.size());

        adapter = new QuizAdapter(questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisible = layoutManager.findFirstVisibleItemPosition() + 1;
                txtQuestionCount.setText("Q" + firstVisible + " / " + questions.size());
            }
        });

        btnSubmit.setOnClickListener(v -> evaluateQuiz(adapter.getUserAnswers()));
    }

    private void evaluateQuiz(Map<Integer, Object> userAnswers) {
        int score = 0;
        ArrayList<Question> wrongQuestions = new ArrayList<>();
        ArrayList<String> userResponsesForWrong = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Object userAns = userAnswers.getOrDefault(i, "");

            boolean correct = false;
            if (q.getType().equalsIgnoreCase("MCQ") || q.getType().equalsIgnoreCase("NAT")) {
                correct = userAns.toString().equalsIgnoreCase(q.getAnswer());
            } else if (q.getType().equalsIgnoreCase("MSQ")) {
                if (userAns instanceof ArrayList) {
                    ArrayList<String> selected = (ArrayList<String>) userAns;
                    ArrayList<String> correctAnswers = new ArrayList<>(List.of(q.getAnswer().split(",")));
                    Collections.sort(selected);
                    Collections.sort(correctAnswers);
                    correct = selected.equals(correctAnswers);
                }
            }

            if (correct) {
                score++;
            } else {
                wrongQuestions.add(q);
                userResponsesForWrong.add(userAns.toString());
            }
        }

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());
        intent.putParcelableArrayListExtra("wrongQuestions", wrongQuestions);
        intent.putStringArrayListExtra("userResponses", userResponsesForWrong);
        startActivity(intent);
        finish();
    }
}