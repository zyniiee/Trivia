package com.example.a2_p2.API;

import com.example.a2_p2.Model.QuestionModel;
import java.util.List;

public class QuestionResponse {
    private int response_code;
    private List<QuestionModel> results;

    public int getResponse_code() {
        return response_code;
    }

    public List<QuestionModel> getResults() {
        return results;
    }
}
