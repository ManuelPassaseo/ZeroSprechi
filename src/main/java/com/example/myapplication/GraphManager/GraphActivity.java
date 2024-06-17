package com.example.myapplication.GraphManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.HelpDialog;
import com.example.myapplication.HomeActivity;
import com.example.myapplication.R;
import com.example.myapplication.Repository.ProductExpiredRepository;
import com.example.myapplication.Repository.UserRepository;
import com.example.myapplication.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {


    private ProductExpiredRepository productExpiredRepository;
    private Cursor cursor_expired;
    private PieDataSet pieDataSet;
    private PieData pieData;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        UserRepository userRepository = new UserRepository(databaseHelper);
        productExpiredRepository = new ProductExpiredRepository(databaseHelper);

        ImageView backImage = findViewById(R.id.backImage);
        ImageView helpImage = findViewById(R.id.imageView);
        Button btnAttiva = findViewById(R.id.buttonAttiva);

        ArrayList<PieEntry> visitors = new ArrayList<>();

        Spinner spGraph = findViewById(R.id.spinnerGraph);
        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.graph_array,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spGraph.setAdapter(adapter);
        spGraph.setSelection(1);

        Long userID = userRepository.findByEmail(SessionManager.getInstance(getApplicationContext()).getEmail()).getId();

        btnAttiva.setOnClickListener(v -> {
            if (spGraph.getSelectedItem().toString().equals("Anno")) {
                visitors.clear();
                pieChart.clear();
                cursor_expired = productExpiredRepository.findByYear(userID);


                while (cursor_expired != null && cursor_expired.moveToNext()) {
                    String name = cursor_expired.getString(cursor_expired.getColumnIndex("Anno"));
                    int quantita = cursor_expired.getInt(cursor_expired.getColumnIndex("NumeroProdotti"));

                    visitors.add(new PieEntry(quantita, name));
                }
                if (cursor_expired != null) {
                    cursor_expired.close();
                }
                pieDataSet = new PieDataSet(visitors, "Data di scadenza");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Prodotti scaduti");
                pieChart.animate();

            } else if (spGraph.getSelectedItem().toString().equals("Categoria")) {

                visitors.clear();
                pieChart.clear();

                cursor_expired = productExpiredRepository.findByCategory(userID);

                while (cursor_expired != null && cursor_expired.moveToNext()) {
                    String name = cursor_expired.getString(cursor_expired.getColumnIndex("category"));
                    int quantita = cursor_expired.getInt(cursor_expired.getColumnIndex("NumeroProdotti"));
                    visitors.add(new PieEntry(quantita, name));
                }
                if (cursor_expired != null) {
                    cursor_expired.close();
                }
                pieDataSet = new PieDataSet(visitors, "Categoria Prodotti");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Prodotti scaduti");
                pieChart.animate();
            }

        });
        backImage.setOnClickListener(v -> {
            Intent intent = new Intent(GraphActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        helpImage.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.setDescriptionText("Help Graph");
            helpDialog.setHeaderText("Visualizza le statistiche dei prodotti scaduti, filtrandoli per anno o per categoria.");
            helpDialog.show();
        });
    }
}