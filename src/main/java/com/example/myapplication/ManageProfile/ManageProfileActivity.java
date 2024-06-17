package com.example.myapplication.ManageProfile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.HomeActivity;
import com.example.myapplication.R;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.User;

public class ManageProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        TextView tvNameSurname = findViewById(R.id.textViewNameSurname);
        TextView tvEmail = findViewById(R.id.textViewEmail);
        Button etPersonalInfo = findViewById(R.id.editPersonalInfo);
        Button etModifyProfile = findViewById(R.id.editModifyProfile);
        Button btnNotify = findViewById(R.id.buttonNotify);
        ImageView helpImage = findViewById(R.id.imageView);
        ImageView backImage = findViewById(R.id.backImage);

        UserRepository userRepository = new UserRepository(new DatabaseHelper(getApplicationContext()));
        User user = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail());

        tvNameSurname.setText(String.format("%s %s", user.getName(), user.getSurname()));
        tvEmail.setText(user.getEmail());

        btnNotify.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            startActivity(intent);
        });

        etPersonalInfo.setOnClickListener(v ->
                startActivity(new Intent(ManageProfileActivity.this, PersonalInfoActivity.class)));

        etModifyProfile.setOnClickListener(v ->
                startActivity(new Intent(ManageProfileActivity.this, ModifyProfileActivity.class)));


        backImage.setOnClickListener(v -> {
            Intent intent = new Intent(ManageProfileActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Gestione Profilo");
            helpDialog.setHeaderText("Questa è la pagina di gestione del profilo.\nDa qui puoi gestire le il tuo profilo.");
            helpDialog.show();
        });
    }
}
