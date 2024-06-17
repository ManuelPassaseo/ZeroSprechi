package com.example.myapplication.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DatabaseManager.DatabaseHelper;
import com.example.myapplication.model.User;

import org.apache.commons.validator.routines.EmailValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


public class UserRepository implements Repository<User, Long> {

    DatabaseHelper databaseHelper;

    public UserRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public User insert(User entity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("surname", entity.getSurname());
        values.put("address", entity.getAddress());
        values.put("birthday", entity.getBirthday().toString());
        values.put("email", entity.getEmail());
        values.put("password", entity.getPassword());

        db.insert("persona", null, values);

        db.close();
        return entity;

    }


    public User update(User entity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("surname", entity.getSurname());
        values.put("address", entity.getAddress());
        values.put("birthday", String.valueOf(entity.getBirthday()));
        values.put("email", entity.getEmail());
        values.put("password", entity.getPassword());
        db.update("persona", values, "id = ?", new String[]{String.valueOf(entity.getId())});
        db.close();

        return entity;
    }


    @SuppressLint("Range")
    public Optional<User> findById(Long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM persona WHERE id = ?", new String[]{id.toString()})) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex("id")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setSurname(cursor.getString(cursor.getColumnIndex("surname")));
                user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                user.setBirthday(LocalDate.parse(cursor.getString(cursor.getColumnIndex("birthday"))));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                cursor.close();
                db.close();
                return Optional.of(user);
            }
        }
        db.close();
        return Optional.empty();
    }

    @SuppressLint("Range")
    public User findByEmail(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM persona WHERE email = ?", new String[]{email})) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex("id")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setSurname(cursor.getString(cursor.getColumnIndex("surname")));
                user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                user.setBirthday(LocalDate.parse(cursor.getString(cursor.getColumnIndex("birthday"))));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));

                cursor.close();
                db.close();
                return user;
            }
        }
        db.close();
        return null;
    }

    @Override
    public User save(User entity) {
        if (Objects.isNull(entity.getId())) {
            return insert(entity);
        }

        Optional<User> user = findById(entity.getId());
        if (!user.isPresent()) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("prodottoScaduto", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public static Boolean validEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }


    public static boolean validPassword(String input) {
        // Checking lower alphabet in string
        int n = input.length();
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        Set<Character> set = new HashSet<>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*', '(', ')', '-', '+'));
        for (char i : input.toCharArray()) {
            if (Character.isLowerCase(i))
                hasLower = true;
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (set.contains(i))
                hasSpecialChar = true;
        }

        // Strength of password
        System.out.print("Strength of password:- ");
        return hasDigit && hasLower && hasUpper && hasSpecialChar && (n >= 8);
    }

    public static boolean validBirthday(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    public Boolean checkEmail(String email) {
        try (SQLiteDatabase db = databaseHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT * FROM persona WHERE email = ?", new String[]{email})) {
            return cursor.moveToFirst();
        }
    }

    public Boolean checkEmailPassword(String email, String password) {
        try (SQLiteDatabase db = databaseHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT * FROM persona WHERE email = ? AND password = ?", new String[]{email, password})) {
            return cursor.moveToFirst();
        }
    }


}