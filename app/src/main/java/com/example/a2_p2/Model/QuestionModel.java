package com.example.a2_p2.Model;

import com.google.firebase.database.PropertyName;
import java.io.Serializable;
import java.util.List;

public class QuestionModel implements Serializable {
    private String question;

    private String correct_answer;

    private List<String> incorrect_answers;

    private String type;
    private String difficulty;
    private String category;

    // Default constructor for Firebase
    public QuestionModel() {
    }

    public QuestionModel(String question, String correct_answer, List<String> incorrect_answers, String type, String difficulty, String category) {
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
}
