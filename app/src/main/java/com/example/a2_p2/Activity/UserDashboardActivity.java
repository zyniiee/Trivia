package com.example.a2_p2.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Adapter.UserTournamnetAdapter;
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

public class UserDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserTournamnetAdapter adapter;
    private List<TournamentModel> tournamentList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        recyclerView = findViewById(R.id.rv_user_trivia_list_layout_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserTournamnetAdapter(tournamentList, this);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://a2-p2-7408a-default-rtdb.firebaseio.com");
        databaseReference = database.getReference("tournaments");

        fetchTournaments();
    }

    private void fetchTournaments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tournamentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TournamentModel tournament = snapshot.getValue(TournamentModel.class);
                    if (tournament != null) {
                        tournament.setId(snapshot.getKey());
                        updateTournamentStatus(tournament);
                        tournamentList.add(tournament);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserDashboardActivity.this, "Failed to fetch tournaments", Toast.LENGTH_SHORT).show();
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
