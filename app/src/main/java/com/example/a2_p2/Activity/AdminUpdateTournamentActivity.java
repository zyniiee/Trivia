package com.example.a2_p2.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2_p2.Model.TournamentModel;
import com.example.a2_p2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdminUpdateTournamentActivity extends AppCompatActivity {
    private EditText tournamentNameEt;
    private TextInputEditText startDate, endDate;
    private Button updateTournamentButton;
    private DatabaseReference databaseReference;
    private String tournamentId;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_tournament);

        tournamentId = getIntent().getStringExtra("tournamentId");

        if (tournamentId == null || tournamentId.isEmpty()) {
            Toast.makeText(this, "Invalid Tournament ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://a2-p2-7408a-default-rtdb.firebaseio.com").getReference("tournaments").child(tournamentId);

        tournamentNameEt = findViewById(R.id.tournamentNameEt);
        startDate = findViewById(R.id.tournamentStartDateEt);
        endDate = findViewById(R.id.tournamentEndDateEt);
        updateTournamentButton = findViewById(R.id.update_button);

        // Disable editing for date fields
        startDate.setFocusable(false);
        startDate.setClickable(true);
        endDate.setFocusable(false);
        endDate.setClickable(true);

        startDate.setOnClickListener(v -> getCurrentDate(startDate));
        endDate.setOnClickListener(v -> getCurrentDate(endDate));

        fetchTournamentDetails();

        updateTournamentButton.setOnClickListener(v -> updateTournament());
    }

    private void fetchTournamentDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TournamentModel tournament = dataSnapshot.getValue(TournamentModel.class);
                if (tournament != null) {
                    tournamentNameEt.setText(tournament.getName());
                    startDate.setText(tournament.getStartDate());
                    endDate.setText(tournament.getEndDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminUpdateTournamentActivity.this, "Failed to fetch tournament details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentDate(final TextInputEditText dateEt) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AdminUpdateTournamentActivity.this, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year1);
            selectedDate.set(Calendar.MONTH, month1);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar today = Calendar.getInstance();
            if (selectedDate.before(today)) {
                Toast.makeText(AdminUpdateTournamentActivity.this, "Date cannot be earlier than today", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
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

    private void updateTournament() {
        String name = tournamentNameEt.getText().toString();
        String updatedStartDate = startDate.getText().toString();
        String updatedEndDate = endDate.getText().toString();

        if (name.isEmpty() || updatedStartDate.isEmpty() || updatedEndDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch existing tournament details to retain other fields
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TournamentModel tournament = dataSnapshot.getValue(TournamentModel.class);
                if (tournament != null) {
                    // Update the fields that need to be updated
                    tournament.setName(name);
                    tournament.setStartDate(updatedStartDate);
                    tournament.setEndDate(updatedEndDate);

                    // Save the updated tournament
                    databaseReference.setValue(tournament)
                            .addOnSuccessListener(aVoid -> Toast.makeText(AdminUpdateTournamentActivity.this, "Tournament updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(AdminUpdateTournamentActivity.this, "Failed to update tournament", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminUpdateTournamentActivity.this, "Failed to fetch tournament details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
