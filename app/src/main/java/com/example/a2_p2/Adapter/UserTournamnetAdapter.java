package com.example.a2_p2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Activity.QuestionViewActivity;
import com.example.a2_p2.Model.TournamentModel;
import com.example.a2_p2.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserTournamnetAdapter extends RecyclerView.Adapter<UserTournamnetAdapter.TournamentViewHolder> {
    private List<TournamentModel> tournaments;
    private Context context;
    private static final String PREFS_NAME = "TournamentPrefs";
    private static final String KEY_HAS_COMPLETED = "hasCompletedTournament";

    public UserTournamnetAdapter(List<TournamentModel> tournaments, Context context) {
        this.tournaments = tournaments;
        this.context = context;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_user_list_layout_item, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        TournamentModel tournament = tournaments.get(position);
        holder.nameTextView.setText(tournament.getName());
        holder.categoryTextView.setText("Category: " + tournament.getCategory());
        holder.difficultyTextView.setText("Difficulty: " + tournament.getDifficulty());
        holder.fromDateTextView.setText("From: " + tournament.getStartDate());
        holder.toDateTextView.setText("To: " + tournament.getEndDate());
        holder.tvTournamentStatus.setText("Status: " + tournament.getStatus());
        holder.likesTextView.setText(String.valueOf(tournament.getLikes()));
        // Check if the tournament has been completed
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean hasCompletedTournament = preferences.getBoolean(KEY_HAS_COMPLETED + tournament.getId(), false);

        if (hasCompletedTournament) {
            holder.joinButton.setText("Unavailable");
            holder.joinButton.setEnabled(false);
        } else {
            holder.joinButton.setText("Join");
            holder.joinButton.setEnabled(true);

            holder.joinButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, QuestionViewActivity.class);
                intent.putExtra("tournament_id", tournament.getId());
                context.startActivity(intent);
            });
        }
        // Disable join button for "upcoming" or "past" tournaments
        if ("Upcoming".equals(tournament.getStatus()) || "Past".equals(tournament.getStatus())) {
            holder.joinButton.setEnabled(false);
            holder.joinButton.setText("Unavailable");
            holder.joinButton.setBackgroundColor(Color.parseColor("#282828"));
            holder.joinButton.setTextColor(Color.parseColor("#606060"));
        } else {
            holder.joinButton.setEnabled(true);
            holder.joinButton.setText("Join");
        }

        // Set default button text to "Like"
        holder.likeButton.setText("Like");
        AtomicReference<Integer> currentLikes = new AtomicReference<>(tournament.getLikes());
        holder.likeButton.setOnClickListener(v -> {
            if ("Like".equals(holder.likeButton.getText())) {
                currentLikes.getAndSet(currentLikes.get() + 1);
                holder.likesTextView.setText(String.valueOf(currentLikes));
                holder.likeButton.setText("Unlike");
            } else if ("Unlike".equals(holder.likeButton.getText())) {
                currentLikes.getAndSet(currentLikes.get() - 1);
                holder.likesTextView.setText(String.valueOf(currentLikes));
                holder.likeButton.setText("Like");
            }
        });

        // Handle Join button click
        holder.joinButton.setOnClickListener(v -> {
            if (holder.joinButton.isEnabled()) {
                // Handle join button click logic
                Intent intent = new Intent(context, QuestionViewActivity.class);
                intent.putExtra("tournament_id", tournament.getId());
                context.startActivity(intent);
            } else {
                // Show toast message when the button is disabled
                Toast.makeText(context, "Unable to join due to tournament status: " + tournament.getStatus(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return tournaments.size();
    }

    public static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, tvTournamentStatus, categoryTextView, difficultyTextView, fromDateTextView, toDateTextView, likesTextView;
        Button joinButton, likeButton;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.trivia_layout_name);
            categoryTextView = itemView.findViewById(R.id.trivia_layout_category);
            difficultyTextView = itemView.findViewById(R.id.trivia_layout_difficulty);
            tvTournamentStatus = itemView.findViewById(R.id.trivia_layout_status);
            fromDateTextView = itemView.findViewById(R.id.trivia_layout_fromDate);
            toDateTextView = itemView.findViewById(R.id.trivia_layout_toDate);
            likesTextView = itemView.findViewById(R.id.trivia_layout_like);
            joinButton = itemView.findViewById(R.id.join_button);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }
}
