package com.example.myapplication.TrashManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.HelpDialog;
import com.example.myapplication.R;

public class TrashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);
        Button buttonExpired = findViewById(R.id.buttonExpiredPage);
        Button buttonCloseExpired = findViewById(R.id.buttonCloseExpirationPage);
        buttonExpired.setOnClickListener(view -> {
            Intent intent = new Intent(TrashActivity.this, ExpiredActivity.class);
            startActivity(intent);
        });
        buttonCloseExpired.setOnClickListener(view -> {
            Intent intent = new Intent(TrashActivity.this, CloseExpirationActivity.class);
            startActivity(intent);
        });
        backImage.setOnClickListener(v -> finish());
        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Gestione Scadenze");
            helpDialog.setHeaderText("Visualizza i prodotti già scaduti, o quelli che sono in scadenza.");
            helpDialog.show();
        });
    }
}