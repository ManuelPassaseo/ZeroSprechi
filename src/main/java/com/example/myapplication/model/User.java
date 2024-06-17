package com.example.myapplication.model;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private Long id;
    private String name;
    private String surname;
    private String address;
    private LocalDate birthday;
    private String email;
    private String password;
    //REGISTER NON FUNZIONA E CAMBIA BIRTHDAY MANAGE PROFILE

    public User() {
    }

    public User(String name, String surname, String address, LocalDate birthday, String email, String password) {
        this(null, name, surname, address, birthday, email, password);
    }

    public User(Long id, String name, String surname, String address, LocalDate birthday, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(address, user.address) && Objects.equals(birthday, user.birthday) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, address, birthday, email, password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
