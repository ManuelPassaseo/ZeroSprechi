package com.example.myapplication.TrashManager;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.R;
import com.example.myapplication.Repository.ProductNearExpiredRepository;
import com.example.myapplication.Repository.ProductRepository;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.Product;

import java.util.Optional;

public class CloseExpirationActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductNearExpiredRepository productNearExpiredRepository;
    LinearLayout llSave;
    int numberOfLines = 0;

    public void add_Line_NearExpired(Optional<Product> product) {
        Button btnRemoveTrash = new Button(this);

        // add edittext
        EditText et = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        et.setBackgroundResource(R.drawable.custom_input);
        et.setTextColor(Color.BLACK);
        et.setPadding(50, 50, 50, 50);


        et.append("PRODOTTO IN SCADENZA\n");
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        et.append("Scadrà il: " + product.get().getDateExpired() + "\n");
        et.append(" " + product.get().getName() + "   " + product.get().getPrice() + "€   " + product.get().getQuantity() + "x" + '\n');

        et.setId(numberOfLines + 1);


        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnRemoveTrash.setLayoutParams(buttonParams);
        btnRemoveTrash.setText("PRODOTTO CONSUMATO");
        btnRemoveTrash.setTextColor(Color.rgb(255, 165, 0));
        btnRemoveTrash.setBackgroundColor(Color.LTGRAY);

        btnRemoveTrash.setTextSize(15);
        btnRemoveTrash.setBackgroundResource(R.drawable.yellow_button);
        btnRemoveTrash.setTypeface(null, Typeface.BOLD_ITALIC);


        llSave.addView(btnRemoveTrash);
        //   et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.trash, 0);

        et.setKeyListener(null); // Imposta il KeyListener a null per disabilitare l'input da tastiera
        et.setFocusable(false); // Imposta il focusabile a false per impedire il focus sull'EditText

        llSave.addView(et);
        numberOfLines++;


        btnRemoveTrash.setOnClickListener(v -> {
            productNearExpiredRepository.save(product.get());


            llSave.removeView(et);
            llSave.removeView(btnRemoveTrash);
            numberOfLines--;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_expiration);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        userRepository = new UserRepository(databaseHelper);
        productRepository = new ProductRepository(databaseHelper);
        productNearExpiredRepository = new ProductNearExpiredRepository(databaseHelper);
        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);

        Long userID = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail()).getId();

        llSave = findViewById(R.id.linearLayoutToSave);
        Cursor cursor_nearExpired = productNearExpiredRepository.getNearExpired(userID);

        //  etTrash.append(cursor.getCount() + " prodotti scaduti\n");
        while (cursor_nearExpired != null && cursor_nearExpired.moveToNext()) {

            Optional<Product> product = productRepository.findById(cursor_nearExpired.getLong(0));

            if (product.isPresent()) {
                add_Line_NearExpired(product);
            }
        }
        if(cursor_nearExpired != null) {
            cursor_nearExpired.close();
        }
        backImage.setOnClickListener(v -> finish());

        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Prodotti in Scadenza");
            helpDialog.setHeaderText("Questa è la pagina dei prodotti in scadenza. \nSe li hai già consumati, eliminali cliccando su 'Used Product'.");
            helpDialog.show();
        });
    }

}
