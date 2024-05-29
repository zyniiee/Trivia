package com.example.a2_p2.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Activity.AdminUpdateTournamentActivity;
import com.example.a2_p2.Model.TournamentModel;
import com.example.a2_p2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {
    private List<TournamentModel> tournamentList;
    private Context context;
    private static final String PREFS_NAME = "TournamentPrefs";
    private static final String KEY_HAS_COMPLETED = "hasCompletedTournament";

    public TournamentAdapter(List<TournamentModel> tournamentList, Context context) {
        this.tournamentList = tournamentList;
        this.context = context;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_admin_list_layout_item, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        TournamentModel tournament = tournamentList.get(position);
        holder.tvTournamentName.setText(tournament.getName());
        holder.tvTournamentStatus.setText("Status: " + tournament.getStatus());
        holder.tvTournamentLikes.setText(String.valueOf(tournament.getLikes()));
        holder.tvTournamentCategory.setText("Category: " + tournament.getCategory());
        holder.tvTournamentDifficulty.setText("Difficulty: " + tournament.getDifficulty());
        holder.tvTournamentFromDate.setText("From: " + tournament.getStartDate());
        holder.tvTournamentToDate.setText("To: " + tournament.getEndDate());


        holder.btnUpdate.setOnClickListener(v -> {
            if (tournament.getId() != null) {
                Intent intent = new Intent(context, AdminUpdateTournamentActivity.class);
                intent.putExtra("tournamentId", tournament.getId());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Invalid tournament ID", Toast.LENGTH_SHORT).show();
            }
        });


        holder.btnDelete.setOnClickListener(v -> {
            if (tournament.getId() != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Tournament")
                        .setMessage("Are you sure you want to delete this tournament?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://a2-p2-7408a-default-rtdb.firebaseio.com").getReference("tournaments").child(tournament.getId());
                            databaseReference.removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Tournament Deleted", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete tournament", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(context, "Invalid tournament ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    public static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTournamentName, tvTournamentStatus, tvTournamentLikes, tvTournamentCategory, tvTournamentDifficulty, tvTournamentFromDate, tvTournamentToDate;
        Button btnUpdate, btnDelete;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTournamentName = itemView.findViewById(R.id.trivia_layout_name);
            tvTournamentStatus = itemView.findViewById(R.id.trivia_layout_status);
            tvTournamentLikes = itemView.findViewById(R.id.trivia_layout_like);
            tvTournamentCategory = itemView.findViewById(R.id.trivia_layout_category);
            tvTournamentDifficulty = itemView.findViewById(R.id.trivia_layout_difficulty);
            tvTournamentFromDate = itemView.findViewById(R.id.trivia_layout_fromDate);
            tvTournamentToDate = itemView.findViewById(R.id.trivia_layout_toDate);
            btnUpdate = itemView.findViewById(R.id.update_button);
            btnDelete = itemView.findViewById(R.id.delete_button);
        }
    }
}
