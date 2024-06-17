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

public class ProductRepository implements Repository<Product, Long> {
    DatabaseHelper databaseHelper;

    public ProductRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @SuppressLint("Range")
    @Override
    public Optional<Product> findById(Long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM prodotto WHERE id = ?", new String[]{id.toString()})) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                Product product = new Product();
                product.setId(cursor.getLong(cursor.getColumnIndex("id")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                product.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                product.setDateExpired(LocalDate.parse(cursor.getString(cursor.getColumnIndex("dateExpired"))));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
                db.close();
                cursor.close();
                return Optional.of(product);
            }
        }
        db.close();
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

        db.insert("prodotto", null, values);

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

        db.update("prodotto", values, "id = ?", new String[]{String.valueOf(entity.getId())});
        db.close();

        return entity;
    }


    @Override
    public void deleteById(Long id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("prodotto", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public Cursor getMaxID() {
        return databaseHelper.getWritableDatabase().rawQuery("SELECT MAX(id) FROM prodotto", null);
    }
}