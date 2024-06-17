package com.example.myapplication.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.model.Product;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class ProductExpiredRepository implements Repository<Product, Long> {

    DatabaseHelper databaseHelper;

    public ProductExpiredRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    @SuppressLint("Range")
    @Override
    public Optional<Product> findById(Long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM prodottoScaduto WHERE id = ? ", new String[]{id.toString()});
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            Product product = new Product();
            product.setId(cursor.getLong(cursor.getColumnIndex("id")));
            product.setName(cursor.getString(cursor.getColumnIndex("name")));
            product.setDescription(cursor.getString(cursor.getColumnIndex("category")));
            product.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            product.setDateExpired(LocalDate.parse(cursor.getString(cursor.getColumnIndex("dateExpired"))));
            product.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
            cursor.close();
            return Optional.of(product);
        }
        cursor.close();
        return Optional.empty();
    }


    public Product insert(Product entity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("description", entity.getDescription());
        values.put("category", entity.getCategory());
        values.put("price", entity.getPrice());
        values.put("dateExpired", String.valueOf(entity.getDateExpired()));
        values.put("quantity", entity.getQuantity());
        values.put("id_prodotto", entity.getId());
        db.insert("prodottoScaduto", null, values);

        db.close();
        return entity;

    }

    @Override
    public Product save(Product entity) {
        if (Objects.isNull(entity.getId())) {
            return insert(entity);
        }

        Optional<Product> product = findById(entity.getId());
        if (!product.isPresent()) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    public Product update(Product entity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", entity.getName());
        values.put("category", entity.getCategory());
        values.put("price", entity.getPrice());
        values.put("dateExpired", String.valueOf(entity.getDateExpired()));
        values.put("quantity", entity.getQuantity());
        values.put("id_prodotto", entity.getId());

        db.update("prodottoScaduto", values, "id = ?", new String[]{String.valueOf(entity.getId())});
        db.close();

        return entity;
    }

    @Override
    public void deleteById(Long id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("prodottoScaduto", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }



    public Cursor findByYear(Long userID) {
        return databaseHelper.getWritableDatabase().rawQuery(
                "SELECT SUBSTR(ps.dateExpired, 1, 4) AS Anno, COUNT(*) AS NumeroProdotti " +
                        "FROM prodottoScaduto AS ps " +
                        "JOIN prodotto AS p ON p.id = ps.id_prodotto " +
                        "JOIN riga_scontrino AS rs ON p.id = rs.id_prodotto " +
                        "JOIN persona AS per ON per.id = rs.id_persona " +
                        "JOIN scontrino AS sc ON sc.id = rs.id_scontrino " +
                        "WHERE rs.id_persona = ? " +
                        "AND ps.id NOT IN ( " +
                        "SELECT id_prodotto " +
                        "FROM prodottoVicinoScadenza) " +
                        "GROUP BY SUBSTR(ps.dateExpired, 1, 4) ", new String[]{String.valueOf(userID)});
    }

    public Cursor findByCategory(Long userID) {
        return databaseHelper.getWritableDatabase().rawQuery(
                "SELECT ps.category , COUNT(*) AS NumeroProdotti " +
                        "FROM prodottoScaduto AS ps " +
                        "JOIN prodotto AS p ON p.id = ps.id_prodotto " +
                        "JOIN riga_scontrino AS rs ON p.id = rs.id_prodotto " +
                        "JOIN persona AS per ON per.id = rs.id_persona " +
                        "JOIN scontrino AS sc ON sc.id = rs.id_scontrino " +
                        "WHERE rs.id_persona = ? " +
                        "AND ps.id NOT IN ( " +
                        "SELECT id_prodotto " +
                        "FROM prodottoVicinoScadenza) " +
                        "GROUP BY ps.category ", new String[]{String.valueOf(userID)});
    }

    public Cursor getExpired(Long userID) {
        return databaseHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM prodotto AS p " +
                        "JOIN riga_scontrino AS rs ON p.id = rs.id_prodotto " +
                        "JOIN persona AS per ON per.id = rs.id_persona " +
                        "JOIN scontrino AS sc ON sc.id = rs.id_scontrino " +
                        "WHERE rs.id_persona = ? " +
                        "AND p.dateExpired < DATE('now') " +
                        "AND NOT EXISTS ( " +
                        "SELECT 1 FROM prodottoScaduto AS ps WHERE p.id = ps.id_prodotto) " +
                        "ORDER BY sc.id ",
                new String[]{String.valueOf(userID)}
        );
    }
}