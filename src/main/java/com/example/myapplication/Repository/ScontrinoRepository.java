package com.example.myapplication.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.model.Scontrino;

import java.util.Objects;
import java.util.Optional;

public class ScontrinoRepository implements Repository<Scontrino, Long> {
    DatabaseHelper databaseHelper;

    public ScontrinoRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @SuppressLint("Range")
    @Override
    public Optional<Scontrino> findById(Long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM prodotto WHERE id = ?", new String[]{id.toString()})) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                Scontrino scontrino = new Scontrino();
                scontrino.setId(cursor.getLong(cursor.getColumnIndex("id")));
                scontrino.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                scontrino.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                db.close();
                cursor.close();
                return Optional.of(scontrino);
            }
        }
        db.close();
        return Optional.empty();
    }


    @Override
    public Scontrino save(Scontrino entity) {
        if (Objects.isNull(entity.getId())) {
            return insert(entity);
        }

        Optional<Scontrino> scontrino = findById(entity.getId());
        if (!scontrino.isPresent()) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("scontrino", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    private Scontrino update(Scontrino entity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", entity.getProductList().toString());
        values.put("price", entity.getTotalPrice());

        db.update("scontrino", values, "id = ?", new String[]{String.valueOf(entity.getId())});
        db.close();

        return entity;
    }



    public Scontrino insert(Scontrino entity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", entity.getProductList().toString());
        values.put("price", entity.getTotalPrice());

        db.insert("scontrino", null, values);

        db.close();
        return entity;
    }

    public Long getMaxID() {
        try (SQLiteDatabase db = databaseHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT MAX(id) FROM scontrino", null)) {
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            } else {
                return -1L;
            }
        }
    }

    public Cursor getAllProducts(Long userID, long currentReceiptId) {
        return databaseHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM prodotto as p " +
                        "JOIN riga_scontrino as rs ON p.id = rs.id_prodotto " +
                        "JOIN persona as per ON per.id = rs.id_persona " +
                        "JOIN scontrino as sc ON sc.id = rs.id_scontrino " +
                        "WHERE rs.id_persona = ? AND rs.id_scontrino = ? ORDER BY sc.id ",
                new String[]{String.valueOf(userID), String.valueOf(currentReceiptId)});
    }

    public Cursor getAllProducts(Long userID) {
        return databaseHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM prodotto as p " +
                        "JOIN riga_scontrino as rs ON p.id = rs.id_prodotto " +
                        "JOIN persona as per ON per.id = rs.id_persona " +
                        "JOIN scontrino as sc ON sc.id = rs.id_scontrino " +
                        "WHERE rs.id_persona = ? ORDER BY sc.id ",
                new String[]{String.valueOf(userID)}
        );
    }
}
