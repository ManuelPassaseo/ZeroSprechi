package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.GraphManager.GraphActivity;
import com.example.myapplication.ManageProfile.ManageProfileActivity;
import com.example.myapplication.ManageShopping.ShoppingActivity;
import com.example.myapplication.TrashManager.TrashActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);


        CardView manageGrafico = findViewById(R.id.cardGrafico);
        manageGrafico.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, GraphActivity.class)));


        CardView manageScadenza = findViewById(R.id.cardScadenza);
        manageScadenza.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, TrashActivity.class)));


        CardView manageShopping = findViewById(R.id.cardSpesa);
        manageShopping.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ShoppingActivity.class)));


        CardView contacUs = findViewById(R.id.cardContactUs);
        contacUs.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"zerosprechi@gmail.com"});
            intent1.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent1, "Send Email"));
        });


        CardView manageProfile = findViewById(R.id.cardManageProfile);
        manageProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ManageProfileActivity.class)));

        CardView logout = findViewById(R.id.cardLogout);
        logout.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            finishAndRemoveTask();
        });
        backImage.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            finishAndRemoveTask();
        });
        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Homepage");
            helpDialog.setHeaderText("Questa è la pagina di homepage. \\n Da qui puoi gestire le tue azioni.");
            helpDialog.show();
        });
    }
}