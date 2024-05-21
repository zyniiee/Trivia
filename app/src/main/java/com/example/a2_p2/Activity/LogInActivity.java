package com.example.a2_p2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a2_p2.R;
import com.google.android.material.textfield.TextInputEditText;

public class LogInActivity extends AppCompatActivity {
    EditText emailText, passwordText;
    Button toDashboard;
    boolean isAllFieldChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        emailText = findViewById(R.id.emailTextInput);
        passwordText= findViewById(R.id.passwordTextInput);
        toDashboard = findViewById(R.id.toDashboard);
        toDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllFieldChecked = CheckAllFields();
                if (isAllFieldChecked){
                    String email = emailText.getText().toString().trim();
                    String password = passwordText.getText().toString().trim();
                    if (email.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")){
                        Intent intent = new Intent(LogInActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                    } else if (email.equalsIgnoreCase("user") && password.equalsIgnoreCase("user")) {
                        Intent intent = new Intent(LogInActivity.this, UserDashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LogInActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean CheckAllFields(){
        if(emailText.length() == 0){
            emailText.setError("This field is required");
            return false;
        }
        if(passwordText.length() == 0){
            passwordText.setError("This field is required");
            return false;
        }
        return true;
    }
}