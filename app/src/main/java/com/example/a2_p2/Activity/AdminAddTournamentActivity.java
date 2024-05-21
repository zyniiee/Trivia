package com.example.a2_p2.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.API.ApiClient;
import com.example.a2_p2.API.ApiResponse;
import com.example.a2_p2.API.ApiService;
import com.example.a2_p2.Adapter.CategoryAdapter;
import com.example.a2_p2.Model.CategoryModel;
import com.example.a2_p2.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddTournamentActivity extends AppCompatActivity {
    private TextInputEditText startDate, endDate;
    private ApiService apiService;
    private AutoCompleteTextView selectCategory, selectDifficulty;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRv;
    private Spinner difficultySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_tournament);

        startDate = findViewById(R.id.tournamentStartDateEt);
        endDate = findViewById(R.id.tournamentEndDateEt);
        categoryRv = findViewById(R.id.categoryRv);
        categoryRv.setLayoutManager(new LinearLayoutManager(this));
        // Disable editing
        startDate.setFocusable(false);
        startDate.setClickable(true);
        endDate.setFocusable(false);
        endDate.setClickable(true);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentDate(startDate);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentDate(endDate);
            }
        });

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        difficultySpinner = findViewById(R.id.difficulty_spinner);
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> difficultyAdapter;
        difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array,
                R.layout.spinner_difficulty_item);
        difficultyAdapter.setDropDownViewResource(R.layout.spinner_difficulty_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDifficulty = parent.getItemAtPosition(position).toString();
                Toast.makeText(AdminAddTournamentActivity.this, "Selected difficulty: " + selectedDifficulty, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });


        fetchCategoriesInRv();
        fetchCategories();
    }


    private void fetchCategoriesInRv() {
        apiService.getCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categories = response.body().getTriviaCategories();
                    Log.d("API Response", "Categories: " + categories); // Log the list
                    categoryAdapter = new CategoryAdapter(categories, AdminAddTournamentActivity.this);
                    categoryRv.setAdapter(categoryAdapter);
                } else {
                    try {
                        Log.e("API Error", "Response: " + response.errorBody().string()); // Log error details
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Failure", t.getMessage()); // Log the failure message
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

        Spinner categorySpinner = findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(AdminAddTournamentActivity.this, "Selected category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });
    }
    private void fetchCategories() {
        apiService.getCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categories = response.body().getTriviaCategories();
                    setupCategorySpinner(categories);
                } else {
                    Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AdminAddTournamentActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
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

            // Get today's date
            Calendar today = Calendar.getInstance();

            if (selectedDate.before(today)) {
                // Show a message if the selected date is earlier than today
                Toast.makeText(AdminAddTournamentActivity.this, "Start date cannot be earlier than today", Toast.LENGTH_SHORT).show();
            } else {
                // Set the selected date to the text input field
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
                Calendar start = Calendar.getInstance();
                start.setTime(simpleDateFormat.parse(startDateText));

                Calendar end = Calendar.getInstance();
                end.setTime(simpleDateFormat.parse(endDateText));


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
 }
