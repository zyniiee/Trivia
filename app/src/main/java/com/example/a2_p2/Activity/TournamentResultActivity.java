package com.example.a2_p2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2_p2.R;

public class TournamentResultActivity extends AppCompatActivity {
    private TextView scoreTextView;
    private Button likeButton;
    private Button dislikeButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tournamnet_result);

        scoreTextView = findViewById(R.id.score_text_view);
        likeButton = findViewById(R.id.like_button);
        dislikeButton = findViewById(R.id.dislike_button);
        backButton = findViewById(R.id.back_button);

        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        scoreTextView.setText("Your got: " + score + " / " + totalQuestions);

        likeButton.setOnClickListener(v -> {
            Toast.makeText(this, "Glad you liked the quiz!", Toast.LENGTH_SHORT).show();
        });

        dislikeButton.setOnClickListener(v -> {
            Toast.makeText(this, "Sorry to hear that you didn't like the quiz.", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TournamentResultActivity.this, UserDashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
