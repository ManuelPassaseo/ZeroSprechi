package com.example.myapplication.ManageProfile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.R;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.User;

import java.time.LocalDate;

public class ModifyProfileActivity extends AppCompatActivity {


    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        EditText etModifyNamePerson = findViewById(R.id.editTextModifyName);
        EditText etModifySurnamePerson = findViewById(R.id.editTextModifySurnamePerson);
        EditText etModifyAddressPerson = findViewById(R.id.editTextModifyAddressPerson);
        EditText etModifyBirthdayPerson = findViewById(R.id.editTextModifyBirthdayPerson);
        EditText etModifyEmailPerson = findViewById(R.id.editTextModifyEmailPerson);
        EditText etModifyPasswordPerson = findViewById(R.id.editTextModifyPasswordPerson);

        Button btnModifyNamePerson = findViewById(R.id.buttonModifyNamePerson);
        Button btnModifySurnamePerson = findViewById(R.id.ModifySurnamePerson);
        Button btnModifyAddressPerson = findViewById(R.id.buttonModifyAddressPerson);
        Button btnModifyBirthdayPerson = findViewById(R.id.buttonModifyBirthdayPerson);
        Button btnModifyEmailPerson = findViewById(R.id.buttonModifyEmailPerson);
        Button btnModifyPasswordPerson = findViewById(R.id.buttonModifyPasswordPerson);
        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);


        userRepository = new UserRepository(new DatabaseHelper(getApplicationContext()));
        User user = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail());


        btnModifyNamePerson.setOnClickListener(view -> {
            String name = etModifyNamePerson.getText().toString();
            if (!name.isEmpty()) {
                user.setName(name);
                userRepository.update(user);
            } else {
                Toast.makeText(getApplicationContext(), "Inserisci il nome", Toast.LENGTH_SHORT).show();
            }
        });

        btnModifySurnamePerson.setOnClickListener(view -> {
            String surname = etModifySurnamePerson.getText().toString();

            if (!surname.isEmpty()) {
                user.setSurname(surname);
                userRepository.update(user);
            } else {
                Toast.makeText(getApplicationContext(), "Inserisci il cognome", Toast.LENGTH_SHORT).show();
            }
        });

        btnModifyAddressPerson.setOnClickListener(view -> {
            String address = etModifyAddressPerson.getText().toString();

            if (!address.isEmpty()) {
                user.setAddress(address);
                userRepository.update(user);
            } else {
                Toast.makeText(getApplicationContext(), "Inserisci l'indirizzo", Toast.LENGTH_SHORT).show();
            }
        });

        btnModifyBirthdayPerson.setOnClickListener(view -> {
            String birthday = etModifyBirthdayPerson.getText().toString();

            if (!birthday.isEmpty()) {
                if (UserRepository.validBirthday(birthday)) {
                    user.setBirthday(LocalDate.parse(birthday));
                    userRepository.update(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Inserisci una corretta data di nascita\n(YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Inserisci una data di nascita\n(YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            }
        });

        btnModifyEmailPerson.setOnClickListener(view -> {
            String email = etModifyEmailPerson.getText().toString();

            if (!email.isEmpty()) {
                if (!userRepository.checkEmail(email) && UserRepository.validEmail(email)) {
                    user.setEmail(email);
                    userRepository.update(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Inserisci una mail corretta", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Inserisci una mail", Toast.LENGTH_SHORT).show();
            }
        });

        btnModifyPasswordPerson.setOnClickListener(view -> {
            String password = etModifyPasswordPerson.getText().toString();

            if (!(password.isEmpty())) {
                if (UserRepository.validPassword(password)) {
                    user.setPassword(password);
                    userRepository.update(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Inserisci una password forte", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Inserisci una password", Toast.LENGTH_SHORT).show();
            }
        });
        backImage.setOnClickListener(v -> finish());
        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Modifica Profilo");
            helpDialog.setHeaderText("Modifica il profilo. Ricorda: inserisci una password valida(almeno 8 caratteri, almeno una lettera maiuscola, almeno un numero ed almeno un carattere speciale), una data di nascita valida(YYYY-MM-DD) e un indirizzo email valido.");
            helpDialog.show();
        });
    }
}