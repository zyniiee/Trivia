package com.example.a2_p2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Adapter.TournamentAdapter;
import com.example.a2_p2.Model.TournamentModel;
import com.example.a2_p2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TournamentAdapter tournamentAdapter;
    private List<TournamentModel> tournamentList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button toAddTournament = findViewById(R.id.toAddTournament);
        toAddTournament.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminAddTournamentActivity.class);
            startActivity(intent);
        });


        recyclerView = findViewById(R.id.rv_trivia_list_layout_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tournamentList = new ArrayList<>();
        tournamentAdapter = new TournamentAdapter(tournamentList, this);
        recyclerView.setAdapter(tournamentAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://a2-p2-7408a-default-rtdb.firebaseio.com");
        databaseReference = database.getReference("tournaments");

        fetchTournaments();
    }

    private void fetchTournaments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tournamentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TournamentModel tournament = dataSnapshot.getValue(TournamentModel.class);
                    if (tournament != null) {
                        tournament.setId(dataSnapshot.getKey()); // Set the id here
                        updateTournamentStatus(tournament);
                        tournamentList.add(tournament);
                    }
                }
                tournamentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to fetch tournaments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTournamentStatus(TournamentModel tournament) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date startDate = sdf.parse(tournament.getStartDate());
            Date endDate = sdf.parse(tournament.getEndDate());
            Date currentDate = new Date();

            if (currentDate.before(startDate)) {
                tournament.setStatus("Upcoming");
            } else if (currentDate.after(endDate)) {
                tournament.setStatus("Past");
            } else {
                tournament.setStatus("Ongoing");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
