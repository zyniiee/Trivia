package com.example.a2_p2.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2_p2.API.ApiClient;
import com.example.a2_p2.API.ApiService;
import com.example.a2_p2.API.CategoriesResponse;
import com.example.a2_p2.API.QuestionResponse;
import com.example.a2_p2.Model.CategoryModel;
import com.example.a2_p2.Model.QuestionModel;
import com.example.a2_p2.Model.TournamentModel;
import com.example.a2_p2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddTournamentActivity extends AppCompatActivity {
    private TextInputEditText startDate, endDate;
    private ApiService apiService;
    private Spinner difficultySpinner, categorySpinner;
    private List<CategoryModel> categoryModelList;
    private EditText tournamentName;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_tournament);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://a2-p2-7408a-default-rtdb.firebaseio.com");
        databaseReference = database.getReference("tournaments");
        tournamentName = findViewById(R.id.tournamentNameEt);
        startDate = findViewById(R.id.tournamentStartDateEt);
        endDate = findViewById(R.id.tournamentEndDateEt);

        // Disable editing for date fields
        startDate.setFocusable(false);
        startDate.setClickable(true);
        endDate.setFocusable(false);
        endDate.setClickable(true);

        startDate.setOnClickListener(v -> getCurrentDate(startDate));
        endDate.setOnClickListener(v -> getCurrentDate(endDate));

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        difficultySpinner = findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array,
                R.layout.spinner_difficulty_item);
        difficultyAdapter.setDropDownViewResource(R.layout.spinner_difficulty_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle difficulty selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        fetchCategories();
        categorySpinner = findViewById(R.id.category_spinner);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle category selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        Button addTournamentButton = findViewById(R.id.addTournamentButton);
        addTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuiz();
            }
        });

    }

    private void fetchCategories() {
        apiService.getCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryModelList = response.body().getTriviaCategories();
                    setupCategorySpinner(categoryModelList);
                } else {
                    Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategorySpinner(List<CategoryModel> categoryModelList) {
        List<String> categoryNames = new ArrayList<>();
        for (CategoryModel category : categoryModelList) {
            categoryNames.add(category.getCategoryName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_category_item, categoryNames);
        adapter.setDropDownViewResource(R.layout.spinner_category_item);
        categorySpinner.setAdapter(adapter);
    }

    private void getCurrentDate(final TextInputEditText dateEt) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AdminAddTournamentActivity.this, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year1);
            selectedDate.set(Calendar.MONTH, month1);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar today = Calendar.getInstance();
            if (selectedDate.before(today)) {
                Toast.makeText(AdminAddTournamentActivity.this, "Date cannot be earlier than today", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateEt.setText(simpleDateFormat.format(selectedDate.getTime()));
                validateEndDate();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void validateEndDate() {
        String startDateText = startDate.getText().toString();
        String endDateText = endDate.getText().toString();

        if (!startDateText.isEmpty() && !endDateText.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date start = simpleDateFormat.parse(startDateText);
                Date end = simpleDateFormat.parse(endDateText);

                if (end.before(start)) {
                    endDate.setError("End date cannot be before start date");
                } else {
                    endDate.setError(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createQuiz() {
        String quizName = tournamentName.getText().toString();
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        String selectedDifficulty = difficultySpinner.getSelectedItem().toString();
        String startDateText = startDate.getText().toString();
        String endDateText = endDate.getText().toString();

        if (quizName.isEmpty() || selectedCategory.isEmpty() || selectedDifficulty.isEmpty() || startDateText.isEmpty() || endDateText.isEmpty()) {
            Toast.makeText(AdminAddTournamentActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = -1;
        for (CategoryModel category : categoryModelList) {
            if (category.getCategoryName().equals(selectedCategory)) {
                categoryId = category.getId();
                break;
            }
        }

        if (categoryId == -1) {
            Toast.makeText(AdminAddTournamentActivity.this, "Invalid category selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<QuestionResponse> call = apiService.getQuestions(10, categoryId, selectedDifficulty.toLowerCase(Locale.ROOT));
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuestionModel> questions = response.body().getResults();

                    // Log the raw API response for debugging
                    Log.d("API_RESPONSE", "Raw response: " + response.raw().toString());

                    // Log questions for debugging
                    for (QuestionModel question : questions) {
                        Log.d("QuestionModel", "Question: " + question.getQuestion());
                        Log.d("QuestionModel", "Correct Answer: " + question.getCorrect_answer());
                        Log.d("QuestionModel", "Incorrect Answers: " + question.getIncorrect_answers());
                    }

                    // Save to Firebase
                    saveToFirebase(quizName, selectedCategory, selectedDifficulty, startDateText, endDateText, questions);
                } else {
                    Log.e("API_RESPONSE", "Failed to fetch questions. Response: " + response.toString());
                    Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Log.e("API_CALL", "API call failed", t);
                Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToFirebase(String name, String category, String difficulty, String startDate, String endDate, List<QuestionModel> questions) {
        TournamentModel tournament = new TournamentModel();
        tournament.setName(name);
        tournament.setCategory(category);
        tournament.setDifficulty(difficulty);
        tournament.setStartDate(startDate);
        tournament.setEndDate(endDate);
        tournament.setQuestions(questions);

        databaseReference.push().setValue(tournament)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FIREBASE_SAVE", "Quiz Created successfully");
                    Toast.makeText(AdminAddTournamentActivity.this, "Quiz Created", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE_SAVE", "Failed to create quiz", e);
                    Toast.makeText(AdminAddTournamentActivity.this, "Failed to create quiz", Toast.LENGTH_SHORT).show();
                });
    }
}
