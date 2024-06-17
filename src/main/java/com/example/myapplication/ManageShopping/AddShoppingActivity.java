package com.example.myapplication.ManageShopping;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.R;
import com.example.myapplication.Repository.ProductExpiredRepository;
import com.example.myapplication.Repository.ProductRepository;
import com.example.myapplication.Repository.ScontrinoRepository;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.example.myapplication.model.Scontrino;
import com.example.myapplication.model.Product;

import java.time.LocalDate;

public class AddShoppingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping);

        EditText etNameProduct = findViewById(R.id.editTextNameProduct);
        EditText etDescriptionProduct = findViewById(R.id.editTextDescriptionProduct);
        EditText etDateProduct = findViewById(R.id.editTextDateProduct);
        EditText etPriceProduct = findViewById(R.id.editTextPriceProduct);
        EditText etQuantityProduct = findViewById(R.id.editTextQuantityProduct);
        EditText etShoppingView = findViewById(R.id.editTextShoppingView);
        Spinner spCategoryProduct = findViewById(R.id.spinnerCategoryProduct);
        Button btnAddProduct = findViewById(R.id.buttonAddProduct);
        Button btnOkShopping = findViewById(R.id.buttonOkShopping);
        Button btnCancelShopping = findViewById(R.id.buttonCancelShopping);
        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ProductRepository productRepository = new ProductRepository(databaseHelper);
        ProductExpiredRepository productExpiredRepository = new ProductExpiredRepository(databaseHelper);
        UserRepository userRepository = new UserRepository(databaseHelper);
        ScontrinoRepository scontrinoRepository = new ScontrinoRepository(databaseHelper);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spCategoryProduct.setAdapter(adapter);
        spCategoryProduct.setSelection(0);

        Scontrino scontrino = new Scontrino();


        btnAddProduct.setOnClickListener(v -> {
            if (etNameProduct.getText().toString().isEmpty() || etDescriptionProduct.getText().toString().isEmpty() || spCategoryProduct.getSelectedItem().toString().isEmpty() || etPriceProduct.getText().toString().isEmpty() || etDateProduct.getText().toString().isEmpty() || etQuantityProduct.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                String name = etNameProduct.getText().toString();
                String description = etDescriptionProduct.getText().toString();
                String category = spCategoryProduct.getSelectedItem().toString();
                double price = Double.parseDouble(etPriceProduct.getText().toString());
                LocalDate date = LocalDate.parse(etDateProduct.getText().toString());
                int quantity = Integer.parseInt(etQuantityProduct.getText().toString());
                Product product = new Product(name, description, category, price, date, quantity);
                scontrino.addProduct(product);
                etShoppingView.append(product + "\n");
            }
        });


        btnOkShopping.setOnClickListener(v -> {
            if (etShoppingView.getText().toString().isEmpty()) {
                Toast.makeText(this, "Inserisci almeno un prodotto", Toast.LENGTH_SHORT).show();
            } else {

                String date = (etDateProduct.getText().toString());
                if (!UserRepository.validBirthday(date)) {
                    Toast.makeText(this, "Inserisci una data di scadenza valida (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                } else {
                    //sql -> aggiungi questo scontrino con i prodotti inseriti.

                    //CREO n PRODOTTI e SALVO L'ID
                    int index = 0;

                    for (Product product : scontrino.getProductList()) {
                        productRepository.save(product);
                        if (product.getDateExpired().isBefore(LocalDate.now())) {
                            productExpiredRepository.save(product);
                        }

                        Cursor c_prodotto = productRepository.getMaxID();
                        if (c_prodotto.moveToFirst()) {
                            scontrino.getProductList().set(index, product).setId(c_prodotto.getLong(0));
                            index++;
                        }
                    }


                    //CREO UN NUOVO SCONTRINO E SALVO DENTRO l'id
                    scontrinoRepository.insert(scontrino);
                    scontrino.setId(scontrinoRepository.getMaxID());


                    //PRENDO ID USER
                    Long userID = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail()).getId();

                    //INSERISCO NELLA TABELLA RIGA_SCONTRINO
                    for (int i = 0; i < scontrino.getProductList().size(); i++) {
                        databaseHelper.getWritableDatabase().execSQL("INSERT INTO riga_scontrino (id_scontrino, id_prodotto, id_persona) VALUES (?, ?,?)", new Object[]{scontrino.getId(), scontrino.getProductList().get(i).getId(), userID});
                    }
                    Intent intent = new Intent(this, ShoppingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    finishAndRemoveTask();
                }
            }
        });

        btnCancelShopping.setOnClickListener(v -> finishAndRemoveTask());
        backImage.setOnClickListener(v -> finish());
        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Aggiungi Scontrino");
            helpDialog.setHeaderText("Aggiungi uno scontrino.\nInserisci le informazioni dei prodotti e salvali cliccando su 'Add'\nUna volta finito, salca lo scontrino cliccando su 'OK'.");
            helpDialog.show();
        });
    }
}
