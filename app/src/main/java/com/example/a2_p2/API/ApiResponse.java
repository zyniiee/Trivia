package com.example.a2_p2.API;

import com.example.a2_p2.Model.CategoryModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("trivia_categories")
    private List<CategoryModel> triviaCategories;

    public List<CategoryModel> getTriviaCategories() {
        return triviaCategories;
    }

    public void setTriviaCategories(List<CategoryModel> triviaCategories) {
        this.triviaCategories = triviaCategories;
    }
}
