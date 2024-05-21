package com.example.a2_p2.API;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/api_category.php")
    Call<ApiResponse> getCategories();
}

