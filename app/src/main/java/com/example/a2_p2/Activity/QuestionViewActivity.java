package com.example.a2_p2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a2_p2.Adapter.QuestionsAdapter;
import com.example.a2_p2.Model.QuestionModel;
import com.example.a2_p2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionViewActivity extends AppCompatActivity implements QuestionsAdapter.OnNextButtonClickListener {

    private static final String TAG = "QuestionViewActivity";
    private static final String PREFS_NAME = "TournamentPrefs";
    private static final String KEY_HAS_COMPLETED = "hasCompletedTournament";

    private ViewPager2 viewPager;
    private QuestionsAdapter adapter;
    private List<QuestionModel> questionList;
    private ProgressBar progressBar;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String tournamentId;
    private boolean hasCompletedTournament = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);

        // Get the tournament ID from the intent
        tournamentId = getIntent().getStringExtra("tournament_id");

        // Retrieve the flag from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        hasCompletedTournament = preferences.getBoolean(KEY_HAS_COMPLETED + tournamentId, false);

        // Check if the user has already completed the tournament
        if (hasCompletedTournament) {
            Toast.makeText(this, "You have already completed this tournament.", Toast.LENGTH_SHORT).show();
            finish(); // Prevent the user from starting the tournament again
            return;
        }

        viewPager = findViewById(R.id.view_pager);
        progressBar = findViewById(R.id.progressBar);
        questionList = new ArrayList<>();

        fetchQuestionsFromDatabase();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentQuestionIndex = position;
                updateProgressBar();
            }
        });
    }

    private void fetchQuestionsFromDatabase() {
        DatabaseReference questionsRef = FirebaseDatabase.getInstance("https://a2-p2-7408a-default-rtdb.firebaseio.com")
                .getReference("tournaments")
                .child(tournamentId)
                .child("questions");
        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionList.clear();
                Log.d(TAG, "Snapshot: " + snapshot.toString());  // Log the entire snapshot

                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    Log.d(TAG, "Question Snapshot: " + questionSnapshot.toString());  // Log each question snapshot

                    QuestionModel question = questionSnapshot.getValue(QuestionModel.class);
                    if (question != null) {
                        // Check and handle missing fields
                        if (question.getCorrect_answer() == null) {
                            Log.d(TAG, "Missing correct_answer for question: " + question.getQuestion());
                            question.setCorrect_answer("Unknown");  // Provide a default value or handle as needed
                        }
                        if (question.getIncorrect_answers() == null || question.getIncorrect_answers().isEmpty()) {
                            Log.d(TAG, "Missing incorrect_answers for question: " + question.getQuestion());
                            question.setIncorrect_answers(new ArrayList<>());  // Provide default values or handle as needed
                        }

                        questionList.add(question);
                    } else {
                        Log.d(TAG, "Question is null");
                    }
                }
                if (questionList.isEmpty()) {
                    Log.d(TAG, "No questions found in database");
                }
                adapter = new QuestionsAdapter(questionList, QuestionViewActivity.this, QuestionViewActivity.this);
                viewPager.setAdapter(adapter);
                Log.d(TAG, "Adapter set with item count: " + adapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionViewActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to load questions: " + error.getMessage());
            }
        });
    }

    private void updateProgressBar() {
        int progress = (int) (((double)(currentQuestionIndex + 1) / questionList.size()) * 100);
        progressBar.setProgress(progress);
    }

    @Override
    public void onNextButtonClick(boolean isCorrect) {
        if (isCorrect) {
            score++;
        }
        if (currentQuestionIndex < questionList.size() - 1) {
            viewPager.setCurrentItem(currentQuestionIndex + 1);
        } else {
            showFinalScore();
        }
    }

    private void showFinalScore() {
        // Save the completion status in SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_HAS_COMPLETED + tournamentId, true);
        editor.apply();

        Intent intent = new Intent(QuestionViewActivity.this, TournamentResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questionList.size());
        startActivity(intent);
        finish();
    }
}
