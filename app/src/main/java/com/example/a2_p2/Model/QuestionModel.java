package com.example.a2_p2.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionModel implements Serializable {
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private String type;
    private String difficulty;
    private String category;

    public QuestionModel() {
        // Default constructor
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

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

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
}
