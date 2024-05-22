package com.example.a2_p2.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api_category.php")
    Call<CategoriesResponse> getCategories();

    @GET("/api.php")
    Call<QuestionResponse> getQuestions (@Query("amount") int amount,
                                        @Query("category") int category,
                                        @Query("difficulty") String difficulty);
}
