package com.example.a2_p2.API;

import com.example.a2_p2.Model.QuestionModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionResponse {
    @SerializedName("results")
    private List<QuestionModel> results;

    public List<QuestionModel> getResults() {
        return results;
    }

    public void setResults(List<QuestionModel> results) {
        this.results = results;
    }
}
