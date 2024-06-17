package com.example.myapplication.ManageShopping;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.R;
import com.example.myapplication.Repository.ProductRepository;
import com.example.myapplication.Repository.ScontrinoRepository;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Optional;

public class ShoppingActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private int numberOfLines = 0;
    private double prezzo_totale;
    private LinearLayout ll;

    private LinearLayout add_Line(Optional<Product> product, EditText et, boolean first, long currentReceiptId) {

        ll = findViewById(R.id.linearLayoutShopping);
        // add edittext


        if (first) {
            et.setId(numberOfLines + 1);

            ImageView imageView = new ImageView(this);

            imageView.setImageResource(R.drawable.lente_ingradimento);
            imageView.setOnClickListener(v -> showSuccessDialog(currentReceiptId));
            ll.addView(imageView);
        }
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        et.setLayoutParams(p);
        et.setBackgroundResource(R.drawable.custom_input);
        et.setTextColor(getResources().getColor(R.color.black));
        et.setPadding(50, 50, 50, 50);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);


// Aggiungi il resto del testo con lo stile SANS_SERIF NORMAL

        et.append(product.get().getName() + " ");
        et.append(product.get().getPrice() + "€ ");
        et.append("x" + product.get().getQuantity() + "\n");

// Imposta l'allineamento del testo
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

// Incrementa il conteggio delle linee
        numberOfLines++;
        return ll;
    }

    private void showRemoveDialog() {
        RelativeLayout constraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_remove, constraintLayout, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        ImageView imageView1 = view.findViewById(R.id.imageRemoveOk);
        EditText etRemoveID = view.findViewById(R.id.editTextRemoveID);

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        imageView1.setOnClickListener(v -> {

            String idScontrino = etRemoveID.getText().toString();
            if (!idScontrino.isEmpty()) {
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.delete("scontrino", "id=?", new String[]{String.valueOf(etRemoveID.getText())});
                db.delete("riga_scontrino", "id_scontrino=?", new String[]{String.valueOf(etRemoveID.getText())});
                db.close();
                alertDialog.dismiss();
                Intent intent = new Intent(ShoppingActivity.this, ShoppingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void showSuccessDialog(long currentReceiptId) {
        RelativeLayout constraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_info, constraintLayout, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView successTitle = view.findViewById(R.id.successTitle);
        successTitle.setText("SCONTRINO: " + currentReceiptId);

        Long userID = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail()).getId();
        Cursor cursor = new ScontrinoRepository(databaseHelper).getAllProducts(userID, currentReceiptId);


        TextView successDesc = view.findViewById(R.id.successDesc);
        while (cursor != null && cursor.moveToNext()) {
            Optional<Product> product = productRepository.findById(cursor.getLong(0));
            product.ifPresent(value -> successDesc.append(value.getName() + " " + value.getDescription() + " " + value.getCategory() + " " + value.getDateExpired() + " " + value.getPrice() + "€ x" + value.getQuantity() + "\n\n"));
        }


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        FloatingActionButton btnAddShopping = findViewById(R.id.buttonAddShopping);
        FloatingActionButton btnRemoveShopping = findViewById(R.id.buttonRemove);
        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        userRepository = new UserRepository(databaseHelper);
        productRepository = new ProductRepository(databaseHelper);
        EditText et = new EditText(this);

        Long userID = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail()).getId();
        Cursor cursor = new ScontrinoRepository(databaseHelper).getAllProducts(userID);

        long previousReceiptId = -1; // ID dello scontrino precedente
        boolean first;
        ll = new LinearLayout(this);
        while (cursor != null && cursor.moveToNext()) {
            @SuppressLint("Range") long currentReceiptId = cursor.getLong(cursor.getColumnIndex("id_scontrino")); // ID dello scontrino corrente
            if (currentReceiptId != previousReceiptId) {
                et.append("PREZZO TOTALE: " + prezzo_totale + "\n");
                et.setKeyListener(null); // Imposta il KeyListener a null per disabilitare l'input da tastiera
                et.setFocusable(false); // Imposta il focusabile a false per impedire il focus sull'EditText
                ll.addView(et);
                prezzo_totale = 0;
                et = new EditText(this);
                previousReceiptId = currentReceiptId; // Aggiorna l'ID dello scontrino precedente


                SpannableString spannableId = new SpannableString("ID Scontrino: " + currentReceiptId + "\n\n");
                spannableId.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, spannableId.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Aggiungi il testo formattato all'EditText
                et.append(spannableId);


                first = true;
            } else {
                first = false;
            }


            Optional<Product> product = productRepository.findById(cursor.getLong(0));
            if (product.isPresent()) {
                ll = add_Line(product, et, first, currentReceiptId);
                prezzo_totale += product.get().getPrice() * product.get().getQuantity();
            }

        }
        et.append("PREZZO TOTALE: " + prezzo_totale + "\n");
        et.setKeyListener(null); // Imposta il KeyListener a null per disabilitare l'input da tastiera
        et.setFocusable(false); // Imposta il focusabile a false per impedire il focus sull'EditText

        ll.addView(et);

        btnAddShopping.setOnClickListener(v -> startActivity(new Intent(ShoppingActivity.this, AddShoppingActivity.class)));

        btnRemoveShopping.setOnClickListener(v -> showRemoveDialog());

        backImage.setOnClickListener(v -> finish());

        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Gestione Spesa");
            helpDialog.setHeaderText("Inserisci o rimuovi uno scontrino.\nVisualizza i dettagli di uno scontrino cliccando sulla lente.");
            helpDialog.show();
        });
    }
}