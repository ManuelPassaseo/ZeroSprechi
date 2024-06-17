package com.example.myapplication.ManageProfile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.R;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.User;

public class PersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        UserRepository userRepository = new UserRepository(new DatabaseHelper(getApplicationContext()));
        User user = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail());

        TextView tvNamePerson = findViewById(R.id.textViewNamePerson);
        TextView tvSurnamePerson = findViewById(R.id.textViewSurnamePerson);
        TextView tvAddressPerson = findViewById(R.id.textViewAddressPerson);
        TextView tvBirthdayPerson = findViewById(R.id.textViewBirthdayPerson);
        TextView tvEmailPerson = findViewById(R.id.textViewEmailPerson);

        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);

        tvNamePerson.setText(user.getName());
        tvSurnamePerson.setText(user.getSurname());
        tvAddressPerson.setText(user.getAddress());
        tvBirthdayPerson.setText(String.valueOf(user.getBirthday()));
        tvEmailPerson.setText(user.getEmail());

        backImage.setOnClickListener(v -> finish());


        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Informazioni Profilo");
            helpDialog.setHeaderText("Queste sono le informazioni del tuo profilo.");
            helpDialog.show();
        });
    }
}