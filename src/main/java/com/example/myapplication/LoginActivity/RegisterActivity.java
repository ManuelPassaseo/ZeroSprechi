package com.example.myapplication.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.model.User;

import java.time.LocalDate;


public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        EditText tfName = findViewById(R.id.tfRegisterName);
        EditText tfSurname = findViewById(R.id.tfRegisterSurname);
        EditText tfAddress = findViewById(R.id.tfRegisterAddress);
        EditText tfBirthday = findViewById(R.id.tfRegisterBirthday);
        EditText tfEmail = findViewById(R.id.tfRegisterEmail);
        EditText tfPassword = findViewById(R.id.tfRegisterPassword);
        TextView lbBack = findViewById(R.id.tvBack);
        Button btnRegister = findViewById(R.id.buttonRegister);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        UserRepository userRepository = new UserRepository(databaseHelper);


        btnRegister.setOnClickListener(v -> {
            String name = tfName.getText().toString();
            String surname = tfSurname.getText().toString();
            String address = tfAddress.getText().toString();
            String birthday = tfBirthday.getText().toString();
            String email = tfEmail.getText().toString();
            String password = tfPassword.getText().toString();

            if (name.isEmpty() || surname.isEmpty() || address.isEmpty() || birthday.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Riempi i campi vuoti", Toast.LENGTH_SHORT).show();
            } else {
                boolean validUserEmail = userRepository.checkEmail(email); //email univoca
                boolean validEmail = UserRepository.validEmail(email);  //formato email
                boolean validPassword = UserRepository.validPassword(password); //formato password
                boolean validBirthday = UserRepository.validBirthday(birthday); //formato data di nascita

                if (!validEmail) {
                    Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
                } else {
                    if (!validPassword) {
                        Toast.makeText(getApplicationContext(), "Password non valida", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!validBirthday) {
                            Toast.makeText(getApplicationContext(), "Data di nascita non valida", Toast.LENGTH_SHORT).show();
                        } else {
                            if (validUserEmail) {
                                Toast.makeText(getApplicationContext(), "Email già in uso", Toast.LENGTH_SHORT).show();
                            } else {
                                User user = new User(name, surname, address, LocalDate.parse(birthday), email, password);
                                userRepository.insert(user);
                                Toast.makeText(getApplicationContext(), "Registrazione fatta con successo", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                }
            }
        });

        lbBack.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}