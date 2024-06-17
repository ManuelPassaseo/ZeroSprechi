package com.example.myapplication.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HomeActivity;
import com.example.myapplication.R;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        UserRepository userRepository = new UserRepository(databaseHelper);

        EditText editUsername = findViewById(R.id.tfLoginUsername);
        EditText editPassword = findViewById(R.id.tfLoginPassword);
        Button btn = findViewById(R.id.buttonLogin);
        TextView labelRegister = findViewById(R.id.labelRegister);

        btn.setOnClickListener(v -> {
            String email = editUsername.getText().toString();
            String password = editPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Inserisci i campi", Toast.LENGTH_SHORT).show();
            } else {
                boolean exists = userRepository.checkEmailPassword(email, password);
                if (exists) {
                    SessionManager.getInstance(getApplicationContext()).setEmail(email);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Non sei autorizzato", Toast.LENGTH_SHORT).show();
                }
            }
        });
        labelRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}