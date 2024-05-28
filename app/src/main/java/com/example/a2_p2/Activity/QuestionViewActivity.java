package com.example.a2_p2.Activity;

import android.content.Intent;
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

public class QuestionViewActivity extends AppCompatActivity {

    private static final String TAG = "QuestionViewActivity";

    private ViewPager2 viewPager;
    private QuestionsAdapter adapter;
    private List<QuestionModel> questionList;
    private ProgressBar progressBar;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String tournamentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);

        viewPager = findViewById(R.id.view_pager);
        progressBar = findViewById(R.id.progressBar);
        questionList = new ArrayList<>();

        // Get the tournament ID from the intent
        tournamentId = getIntent().getStringExtra("tournament_id");

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
                        if (question.getCorrectAnswer() == null) {
                            Log.d(TAG, "Missing correct_answer for question: " + question.getQuestion());
                            question.setCorrectAnswer("");  // Provide a default value or handle as needed
                        }
                        if (question.getIncorrectAnswers() == null || question.getIncorrectAnswers().isEmpty()) {
                            Log.d(TAG, "Missing incorrect_answers for question: " + question.getQuestion());
                            question.setIncorrectAnswers(new ArrayList<>());  // Provide default values or handle as needed
                        }

                        question.logContents();  // Log the contents of the question model
                        questionList.add(question);
                    } else {
                        Log.d(TAG, "Question is null");
                    }
                }
                if (questionList.isEmpty()) {
                    Log.d(TAG, "No questions found in database");
                }
                adapter = new QuestionsAdapter(questionList, QuestionViewActivity.this);
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
        int progress = (int) (((double) currentQuestionIndex / questionList.size()) * 100);
        progressBar.setProgress(progress);
    }

    private void showFinalScore() {
        Intent intent = new Intent(QuestionViewActivity.this, TournamentResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questionList.size());
        startActivity(intent);
        finish();
    }
}
