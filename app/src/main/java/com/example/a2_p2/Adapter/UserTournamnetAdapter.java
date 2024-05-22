package com.example.a2_p2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Activity.QuestionViewActivity;
import com.example.a2_p2.Model.TournamentModel;
import com.example.a2_p2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

public class UserTournamnetAdapter extends RecyclerView.Adapter<UserTournamnetAdapter.TournamentViewHolder> {
    private List<TournamentModel> tournaments;
    private Context context;
    private android.graphics.Color Color;

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
        holder.likesTextView.setText(String.valueOf(tournament.getLikes()));
        holder.tvTournamentStatus.setText("Status: " + tournament.getStatus());

        // Disable join button for "upcoming" or "past" tournaments
        if ("Upcoming".equals(tournament.getStatus()) || "Past".equals(tournament.getStatus())) {
            holder.joinButton.setEnabled(false);
            holder.joinButton.setBackgroundColor(Color.parseColor("#282828"));
            holder.joinButton.setTextColor(Color.parseColor("#606060"));

        } else {
            holder.joinButton.setEnabled(true);
        }

        // Handle Join button click
        holder.joinButton.setOnClickListener(v -> {
            if (holder.joinButton.isEnabled()) {
                // Handle join button click logic
                Intent intent = new Intent(context, QuestionViewActivity.class);
                intent.putExtra("tournament", tournament);
                context.startActivity(intent);
            }
        });

        // Handle Like button click
        holder.likeButton.setOnClickListener(v -> {
            DatabaseReference likesRef = FirebaseDatabase.getInstance()
                    .getReference("tournaments")
                    .child(tournament.getId())
                    .child("likes");

            likesRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Integer currentLikes = mutableData.getValue(Integer.class);
                    if (currentLikes == null) {
                        mutableData.setValue(1);
                    } else {
                        if (holder.likeButton.isSelected()) {
                            mutableData.setValue(currentLikes - 1);
                        } else {
                            mutableData.setValue(currentLikes + 1);
                        }
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                    if (committed) {
                        holder.likeButton.setSelected(!holder.likeButton.isSelected());
                        int newLikesCount = dataSnapshot.getValue(Integer.class);
                        holder.likesTextView.setText(String.valueOf(newLikesCount));
                    }
                }
            });
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
