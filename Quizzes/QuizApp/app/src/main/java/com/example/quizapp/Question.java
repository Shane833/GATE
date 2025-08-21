/*
package com.example.quizapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String text;
    private ArrayList<String> options;  // Empty for NAT
    private String answer;
    private String explanation;
    private String type; // MCQ, MSQ, NAT

    public Question(String text, ArrayList<String> options, String answer, String explanation, String type) {
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
        this.type = type;
    }

    public String getText() { return text; }
    public ArrayList<String> getOptions() { return options; }
    public String getAnswer() { return answer; }
    public String getExplanation() { return explanation; }
    public String getType() { return type; }
}
*/

// Update 4
package com.example.quizapp;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class Question implements Parcelable {
    private String text;
    private ArrayList<String> options;
    private String answer;
    private String explanation;
    private String type;
    private String imageUriString;
    private String explanationImageUriString;

    public Question(String text, ArrayList<String> options, String answer, String explanation, String type, String imageUriString, String explanationImageUriString) {
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
        this.type = type;
        this.imageUriString = imageUriString;
        this.explanationImageUriString = explanationImageUriString;
    }

    // Getters
    public String getText() { return text; }
    public ArrayList<String> getOptions() { return options; }
    public String getAnswer() { return answer; }
    public String getExplanation() { return explanation; }
    public String getType() { return type; }
    public String getImageUriString() { return imageUriString; }
    public String getExplanationImageUriString() { return explanationImageUriString; }

    // --- Parcelable Implementation ---
    protected Question(Parcel in) {
        text = in.readString();
        options = in.createStringArrayList();
        answer = in.readString();
        explanation = in.readString();
        type = in.readString();
        imageUriString = in.readString();
        explanationImageUriString = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeStringList(options);
        dest.writeString(answer);
        dest.writeString(explanation);
        dest.writeString(type);
        dest.writeString(imageUriString);
        dest.writeString(explanationImageUriString);
    }
}