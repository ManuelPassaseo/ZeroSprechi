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
import com.example.myapplication.Repository.ProductExpiredRepository;
import com.example.myapplication.Repository.ProductRepository;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.Product;

import java.util.Optional;

public class ExpiredActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductExpiredRepository productExpiredRepository;
    LinearLayout llTrash;
    int numberOfLines = 0;

    public void add_Line(Optional<Product> product) {
        Button btnRemoveTrash = new Button(this);

        // add edittext
        EditText et = new EditText(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);


        et.setBackgroundResource(R.drawable.custom_input);
        et.setTextColor(Color.BLACK);
        et.setPadding(50, 50, 50, 50);

        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        et.append("PRODOTTO SCADUTO \n");
        et.append("Scaduto il: " + product.get().getDateExpired() + "\n");
        et.append(" " + product.get().getName() + "   " + product.get().getPrice() + "€   " + "x" + product.get().getQuantity() + '\n');

        et.setId(numberOfLines + 1);


        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnRemoveTrash.setLayoutParams(buttonParams);
        btnRemoveTrash.setText("ELIMINA PRODOTTO ( SCADUTO ) ");
        btnRemoveTrash.setTextColor(Color.BLACK);
        btnRemoveTrash.setBackgroundResource(R.drawable.red_button);
        btnRemoveTrash.setTextSize(15);
        btnRemoveTrash.setTypeface(null, Typeface.BOLD_ITALIC);

        llTrash.addView(btnRemoveTrash);
        //   et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.trash, 0);

        et.setKeyListener(null); // Imposta il KeyListener a null per disabilitare l'input da tastiera
        et.setFocusable(false); // Imposta il focusabile a false per impedire il focus sull'EditText

        llTrash.addView(et);
        numberOfLines++;


        btnRemoveTrash.setOnClickListener(v -> {
            productExpiredRepository.save(product.get());

            llTrash.removeView(et);
            llTrash.removeView(btnRemoveTrash);
            numberOfLines--;
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        userRepository = new UserRepository(databaseHelper);
        productRepository = new ProductRepository(databaseHelper);
        productExpiredRepository = new ProductExpiredRepository(databaseHelper);
        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);
        llTrash = findViewById(R.id.linearLayoutDecisions);

        Long userID = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail()).getId();

        Cursor cursor_expired = productExpiredRepository.getExpired(userID);


        //  etTrash.append(cursor.getCount() + " prodotti scaduti\n");
        while (cursor_expired != null && cursor_expired.moveToNext()) {
            Optional<Product> product = productRepository.findById(cursor_expired.getLong(0));
            if (product.isPresent()) {
                add_Line(product);
            }
        }
        if (cursor_expired != null) {
            cursor_expired.close();
        }
        backImage.setOnClickListener(v -> finish());

        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Prodotti Scaduti");
            helpDialog.setHeaderText("Questa è la pagina dei prodotti scaduti. \nPer eliminarli, clicca su 'Elimina Prodotto (Scaduto)'.");
            helpDialog.show();
        });
    }

}
