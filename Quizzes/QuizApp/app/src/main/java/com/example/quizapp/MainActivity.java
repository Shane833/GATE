/*
package com.example.quizapp;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questionList = new ArrayList<>();
    private Button btnLoadQuiz;

    // File chooser launcher
    private final ActivityResultLauncher<String> filePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    loadQuizFromUri(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadQuiz = findViewById(R.id.btnLoadQuiz);
        btnLoadQuiz.setOnClickListener(v -> {
            filePicker.launch("text/plain"); // Pick .txt file
        });
    }

    private void loadQuizFromUri(Uri uri) {
        try {
            String quizName = getFileName(uri);
            InputStream is = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            parseQuiz(content.toString(), quizName);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = "Quiz";
        try (android.database.Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            }
        }
        return result.replace(".txt", "");
    }

    private void parseQuiz(String content, String quizName) {
        questionList.clear();
        String[] blocks = content.trim().split("\\n\\n");

        for (String block : blocks) {
            String[] lines = block.trim().split("\\n");
            if (lines.length < 2) continue;

            String typeLine = lines[0];
            String qType = typeLine.substring(0, typeLine.indexOf(":")).trim();
            String qText = typeLine.substring(typeLine.indexOf(":") + 1).trim();

            ArrayList<String> options = new ArrayList<>();
            String answer = "";
            StringBuilder explanation = new StringBuilder();

            if (qType.equalsIgnoreCase("MCQ") || qType.equalsIgnoreCase("MSQ")) {
                for (int i = 1; i <= 4; i++) {
                    options.add(lines[i].substring(3).trim());
                }
                answer = lines[5].split(":")[1].trim();
                for (int i = 6; i < lines.length; i++) explanation.append(lines[i]).append("\n");
            } else if (qType.equalsIgnoreCase("NAT")) {
                answer = lines[1].split(":")[1].trim();
                for (int i = 2; i < lines.length; i++) explanation.append(lines[i]).append("\n");
            }

            questionList.add(new Question(qText, options, answer, explanation.toString().trim(), qType));
        }

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(questionList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Number of Questions (Total: " + questionList.size() + ")");
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(1);
        picker.setMaxValue(questionList.size());
        picker.setValue(questionList.size());
        builder.setView(picker);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int numQuestions = picker.getValue();
            Collections.shuffle(questionList);
            ArrayList<Question> selectedQuestions = new ArrayList<>(questionList.subList(0, numQuestions));

            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("quizName", quizName + " (" + numQuestions + " Questions)");
            intent.putExtra("questions", selectedQuestions);
            startActivity(intent);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
Original Code
 */

/*
package com.example.quizapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern; // UPDATED: Import for regular expressions

public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questionList = new ArrayList<>();
    private Button btnLoadQuiz;

    // File chooser launcher
    private final ActivityResultLauncher<String> filePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    loadQuizFromUri(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadQuiz = findViewById(R.id.btnLoadQuiz);
        btnLoadQuiz.setOnClickListener(v -> {
            filePicker.launch("text/plain"); // Pick .txt file
        });
    }

    private void loadQuizFromUri(Uri uri) {
        try {
            String quizName = getFileName(uri);
            InputStream is = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            parseQuiz(content.toString(), quizName);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = "Quiz";
        try (android.database.Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            }
        }
        return result.replace(".txt", "");
    }

    private void parseQuiz(String content, String quizName) {
        questionList.clear();
        String[] blocks = content.trim().split("\\n\\n");

        // UPDATED: Regex to find and remove any image tags like [image: ... ]
        Pattern imagePattern = Pattern.compile("\\[image:.*?\\]");

        for (String block : blocks) {
            String[] lines = block.trim().split("\\n");
            if (lines.length < 2) continue;

            String typeLine = lines[0];
            String qType = typeLine.substring(0, typeLine.indexOf(":")).trim();

            // UPDATED: Get the raw question text and strip out any image tags
            String qTextWithTags = typeLine.substring(typeLine.indexOf(":") + 1).trim();
            String qText = imagePattern.matcher(qTextWithTags).replaceAll("").trim();


            ArrayList<String> options = new ArrayList<>();
            String answer = "";
            StringBuilder explanationWithTags = new StringBuilder();

            if (qType.equalsIgnoreCase("MCQ") || qType.equalsIgnoreCase("MSQ")) {
                for (int i = 1; i <= 4; i++) {
                    options.add(lines[i].substring(3).trim());
                }
                answer = lines[5].split(":")[1].trim();
                for (int i = 6; i < lines.length; i++) explanationWithTags.append(lines[i]).append("\n");
            } else if (qType.equalsIgnoreCase("NAT")) {
                answer = lines[1].split(":")[1].trim();
                for (int i = 2; i < lines.length; i++) explanationWithTags.append(lines[i]).append("\n");
            }

            // UPDATED: Strip image tags from the explanation as well
            String explanationText = explanationWithTags.toString().replace("Explanation:", "").trim();
            String explanation = imagePattern.matcher(explanationText).replaceAll("").trim();

            questionList.add(new Question(qText, options, answer, explanation, qType));
        }

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(questionList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Number of Questions (Total: " + questionList.size() + ")");
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(1);
        picker.setMaxValue(questionList.size());
        picker.setValue(questionList.size());
        builder.setView(picker);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int numQuestions = picker.getValue();
            ArrayList<Question> selectedQuestions = new ArrayList<>(questionList.subList(0, numQuestions));

            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("quizName", quizName + " (" + numQuestions + " Questions)");
            intent.putExtra("questions", selectedQuestions);
            startActivity(intent);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
 */

/*
package com.example.quizapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questionList = new ArrayList<>();
    private Button btnLoadQuiz;

    // File chooser launcher
    private final ActivityResultLauncher<String> filePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    loadQuizFromUri(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadQuiz = findViewById(R.id.btnLoadQuiz);
        btnLoadQuiz.setOnClickListener(v -> {
            filePicker.launch("text/plain"); // Pick .txt file
        });
    }

    private void loadQuizFromUri(Uri uri) {
        try {
            String quizName = getFileName(uri);
            InputStream is = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            parseQuiz(content.toString(), quizName);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = "Quiz";
        try (android.database.Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            }
        }
        return result.replace(".txt", "");
    }

    // UPDATED: This method is now modified to handle multiline questions
    private void parseQuiz(String content, String quizName) {
        questionList.clear();
        String[] blocks = content.trim().split("\\n\\n");

        for (String block : blocks) {
            String[] lines = block.trim().split("\\n");
            if (lines.length < 2) continue;

            String typeLine = lines[0];
            String qType = typeLine.substring(0, typeLine.indexOf(":")).trim();

            // --- Logic to find where the question text ends ---
            ArrayList<String> questionTextLines = new ArrayList<>();
            questionTextLines.add(typeLine.substring(typeLine.indexOf(":") + 1).trim());

            int contentStartIndex = 1;
            for (int i = 1; i < lines.length; i++) {
                String currentLine = lines[i].trim();
                boolean isOption = qType.matches("MCQ|MSQ") && currentLine.matches("^[A-D]\\).+");
                boolean isAnswer = currentLine.startsWith("Answer:");

                if (isOption || isAnswer) {
                    contentStartIndex = i;
                    break;
                }
                questionTextLines.add(lines[i]);
            }
            String qText = String.join("\n", questionTextLines);


            // --- Parse Options, Answer, and Explanation from the remaining lines ---
            ArrayList<String> options = new ArrayList<>();
            String answer = "";
            StringBuilder explanation = new StringBuilder();
            int explanationIndex;

            if (qType.equalsIgnoreCase("MCQ") || qType.equalsIgnoreCase("MSQ")) {
                for (int i = 0; i < 4; i++) {
                    options.add(lines[contentStartIndex + i].substring(3).trim());
                }
                answer = lines[contentStartIndex + 4].split(":")[1].trim();
                explanationIndex = contentStartIndex + 5;
            } else if (qType.equalsIgnoreCase("NAT")) {
                answer = lines[contentStartIndex].split(":")[1].trim();
                explanationIndex = contentStartIndex + 1;
            } else {
                continue; // Skip unknown question types
            }

            for (int i = explanationIndex; i < lines.length; i++) {
                explanation.append(lines[i]).append("\n");
            }

            String finalExplanation = explanation.toString().replace("Explanation:", "").trim();
            questionList.add(new Question(qText, options, answer, finalExplanation, qType));
        }

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(questionList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Number of Questions (Total: " + questionList.size() + ")");
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(1);
        picker.setMaxValue(questionList.size());
        picker.setValue(questionList.size());
        builder.setView(picker);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int numQuestions = picker.getValue();
            ArrayList<Question> selectedQuestions = new ArrayList<>(questionList.subList(0, numQuestions));

            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("quizName", quizName + " (" + numQuestions + " Questions)");
            intent.putExtra("questions", selectedQuestions);
            startActivity(intent);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
 */

// Update 4
package com.example.quizapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questionList = new ArrayList<>();
    private Button btnLoadQuiz;

    private final ActivityResultLauncher<Uri> directoryPicker =
            registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
                if (uri != null) {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    loadQuizFromDirectory(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadQuiz = findViewById(R.id.btnLoadQuiz);
        btnLoadQuiz.setOnClickListener(v -> {
            Toast.makeText(this, "Please select the FOLDER containing your quiz.txt and images.", Toast.LENGTH_LONG).show();
            directoryPicker.launch(null);
        });
    }

    private void loadQuizFromDirectory(Uri dirUri) {
        DocumentFile dir = DocumentFile.fromTreeUri(this, dirUri);
        if (dir == null || !dir.isDirectory()) {
            Toast.makeText(this, "Failed to open directory.", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentFile quizFile = null;
        for (DocumentFile file : dir.listFiles()) {
            if (file.getName() != null && file.getName().endsWith(".txt")) {
                quizFile = file;
                break;
            }
        }

        if (quizFile == null) {
            Toast.makeText(this, "No .txt quiz file found in the selected directory.", Toast.LENGTH_SHORT).show();
            return;
        }

        try (InputStream is = getContentResolver().openInputStream(quizFile.getUri());
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            parseQuiz(content.toString(), quizFile.getName(), dir);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    private void parseQuiz(String content, String quizName, DocumentFile dir) {
        questionList.clear();
        String[] blocks = content.trim().split("\\n\\n");
        Pattern imagePattern = Pattern.compile("\\[image:\\s*(.*?)\\]");

        HashMap<String, Uri> fileUriMap = new HashMap<>();
        for (DocumentFile file : dir.listFiles()) {
            if (file.getName() != null) {
                fileUriMap.put(file.getName(), file.getUri());
            }
        }

        for (String block : blocks) {
            String[] lines = block.trim().split("\\n");
            if (lines.length < 2) continue;

            String typeLine = lines[0];
            String qType = typeLine.substring(0, typeLine.indexOf(":")).trim();
            String qTextRaw = typeLine.substring(typeLine.indexOf(":") + 1).trim();
            Matcher qMatcher = imagePattern.matcher(qTextRaw);
            String qText = qMatcher.replaceAll("").trim();
            String qImageUri = qMatcher.find() ? fileUriMap.get(qMatcher.group(1).trim()).toString() : null;

            ArrayList<String> options = new ArrayList<>();
            String answer = "";
            String rawExplanation = "";

            if (qType.equalsIgnoreCase("MCQ") || qType.equalsIgnoreCase("MSQ")) {
                for (int i = 1; i <= 4; i++) options.add(lines[i].substring(3).trim());
                answer = lines[5].split(":")[1].trim();
                for (int i = 6; i < lines.length; i++) rawExplanation += lines[i] + "\n";
            } else if (qType.equalsIgnoreCase("NAT")) {
                answer = lines[1].split(":")[1].trim();
                for (int i = 2; i < lines.length; i++) rawExplanation += lines[i] + "\n";
            }

            rawExplanation = rawExplanation.replace("Explanation:", "").trim();
            Matcher eMatcher = imagePattern.matcher(rawExplanation);
            String explanationText = eMatcher.replaceAll("").trim();
            String eImageUri = eMatcher.find() ? fileUriMap.get(eMatcher.group(1).trim()).toString() : null;

            questionList.add(new Question(qText, options, answer, explanationText, qType, qImageUri, eImageUri));
        }

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(questionList);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Number of Questions (Total: " + questionList.size() + ")");
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(1);
        picker.setMaxValue(questionList.size());
        picker.setValue(questionList.size());
        builder.setView(picker);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int numQuestions = picker.getValue();
            ArrayList<Question> selectedQuestions = new ArrayList<>(questionList.subList(0, numQuestions));
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("quizName", quizName.replace(".txt", "") + " (" + numQuestions + " Questions)");
            intent.putParcelableArrayListExtra("questions", selectedQuestions);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

     */

    private void parseQuiz(String content, String quizName, DocumentFile dir) {
        questionList.clear();
        String[] blocks = content.trim().split("\\n\\n");
        Pattern imagePattern = Pattern.compile("\\[image:\\s*(.*?)\\]");

        HashMap<String, Uri> fileUriMap = new HashMap<>();
        for (DocumentFile file : dir.listFiles()) {
            if (file.getName() != null) {
                fileUriMap.put(file.getName(), file.getUri());
            }
        }

        for (String block : blocks) {
            String[] lines = block.trim().split("\\n");
            if (lines.length < 2) continue;

            String typeLine = lines[0];
            String qType = typeLine.substring(0, typeLine.indexOf(":")).trim();
            String qTextRaw = typeLine.substring(typeLine.indexOf(":") + 1).trim();

            // --- CORRECTED LOGIC ---
            Matcher qMatcher = imagePattern.matcher(qTextRaw);
            String qImageUri = null;
            if (qMatcher.find()) {
                // Find the filename first...
                String filename = qMatcher.group(1).trim();
                if (fileUriMap.containsKey(filename)) {
                    qImageUri = fileUriMap.get(filename).toString();
                }
            }
            // ...then remove the tag from the text.
            String qText = qMatcher.replaceAll("").trim();


            ArrayList<String> options = new ArrayList<>();
            String answer = "";
            String rawExplanation = "";

            if (qType.equalsIgnoreCase("MCQ") || qType.equalsIgnoreCase("MSQ")) {
                for (int i = 1; i <= 4; i++) options.add(lines[i].substring(3).trim());
                answer = lines[5].split(":")[1].trim();
                for (int i = 6; i < lines.length; i++) rawExplanation += lines[i] + "\n";
            } else if (qType.equalsIgnoreCase("NAT")) {
                answer = lines[1].split(":")[1].trim();
                for (int i = 2; i < lines.length; i++) rawExplanation += lines[i] + "\n";
            }

            rawExplanation = rawExplanation.replace("Explanation:", "").trim();

            // --- CORRECTED LOGIC for Explanation ---
            Matcher eMatcher = imagePattern.matcher(rawExplanation);
            String eImageUri = null;
            if (eMatcher.find()) {
                String filename = eMatcher.group(1).trim();
                if (fileUriMap.containsKey(filename)) {
                    eImageUri = fileUriMap.get(filename).toString();
                }
            }
            String explanationText = eMatcher.replaceAll("").trim();

            questionList.add(new Question(qText, options, answer, explanationText, qType, qImageUri, eImageUri));
        }

        // ...The rest of the method (NumberPicker dialog) remains the same...
        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(questionList);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Number of Questions (Total: " + questionList.size() + ")");
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(1);
        picker.setMaxValue(questionList.size());
        picker.setValue(questionList.size());
        builder.setView(picker);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int numQuestions = picker.getValue();
            ArrayList<Question> selectedQuestions = new ArrayList<>(questionList.subList(0, numQuestions));
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("quizName", quizName.replace(".txt", "") + " (" + numQuestions + " Questions)");
            intent.putParcelableArrayListExtra("questions", selectedQuestions);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}