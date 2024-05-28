package com.example.a2_p2.Model;

import android.util.Log;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionModel implements Serializable {
    private String question;

    @PropertyName("correct_answer")
    private String correctAnswer;

    @PropertyName("incorrect_answers")
    private List<String> incorrectAnswers;

    private String type;
    private String difficulty;
    private String category;

    // Default constructor for Firebase
    public QuestionModel() {
        this.incorrectAnswers = new ArrayList<>();
    }

    public QuestionModel(String question, String correctAnswer, List<String> incorrectAnswers, String type, String difficulty, String category) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers != null ? incorrectAnswers : new ArrayList<>();
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @PropertyName("correct_answer")
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @PropertyName("correct_answer")
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @PropertyName("incorrect_answers")
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    @PropertyName("incorrect_answers")
    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers != null ? incorrectAnswers : new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void logContents() {
        Log.d("QuestionModel", "Question: " + getQuestion());
        Log.d("QuestionModel", "Correct Answer: " + getCorrectAnswer());
        if (getIncorrectAnswers() != null) {
            for (String incorrectAnswer : getIncorrectAnswers()) {
                Log.d("QuestionModel", "Incorrect Answer: " + incorrectAnswer);
            }
        } else {
            Log.d("QuestionModel", "Incorrect Answers: null");
        }
        Log.d("QuestionModel", "Type: " + getType());
        Log.d("QuestionModel", "Difficulty: " + getDifficulty());
        Log.d("QuestionModel", "Category: " + getCategory());
    }
}
