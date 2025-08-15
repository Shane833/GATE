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
