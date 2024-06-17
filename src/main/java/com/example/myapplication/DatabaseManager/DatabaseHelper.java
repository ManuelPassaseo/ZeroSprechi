package com.example.myapplication.DatabaseManager;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "progetto.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE persona (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), surname VARCHAR(50), address VARCHAR(50), birthday VARCHAR(50),email VARCHAR(50), password VARCHAR(50))");
        db.execSQL("CREATE TABLE prodotto (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), description VARCHAR(50),category VARCHAR(50), price DOUBLE, dateExpired VARCHAR(50), quantity INTEGER )");
        db.execSQL("CREATE TABLE prodottoScaduto (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), description VARCHAR(50),category VARCHAR(50), price DOUBLE, dateExpired VARCHAR(50), quantity INTEGER,id_prodotto INTEGER, FOREIGN KEY (id_prodotto) REFERENCES prodotto(id))");
        db.execSQL("CREATE TABLE prodottoVicinoScadenza (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), description VARCHAR(50),category VARCHAR(50), price DOUBLE, dateExpired VARCHAR(50), quantity INTEGER,id_prodotto INTEGER, FOREIGN KEY (id_prodotto) REFERENCES prodotto(id))");
        db.execSQL("CREATE TABLE scontrino (id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR(50),  price DOUBLE )");
        db.execSQL("CREATE TABLE riga_scontrino (id INTEGER PRIMARY KEY AUTOINCREMENT, id_scontrino INTEGER, id_prodotto INTEGER, id_persona INTEGER, FOREIGN KEY (id_scontrino) REFERENCES scontrino(id),FOREIGN KEY (id_prodotto) REFERENCES prodotto(id), FOREIGN KEY (id_persona) REFERENCES persona(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists persona");
        db.execSQL("drop Table if exists scontrino");
        db.execSQL("drop Table if exists fattura");
        db.execSQL("drop Table if exists riga_scontrino");
        onCreate(db);
    }

}
