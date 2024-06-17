package com.example.myapplication.model;

import java.time.LocalDate;
import java.util.Objects;

public class Product {
    Long id;
    String name;
    String description;
    String category;
    double price;
    LocalDate dateExpired;
    int quantity;

    public Product() {
    }

    public Product(Long id, String name, String description, String category, double price, LocalDate dateExpired, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.dateExpired = dateExpired;
        this.quantity = quantity;
    }

    public Product(String name, String description, String category, double price, LocalDate dateExpired, int quantity) {
        this(null, name, description, category, price, dateExpired, quantity);
    }

    public boolean isExpired() {
        LocalDate start = LocalDate.now();
        return dateExpired.isAfter(start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && quantity == product.quantity && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(category, product.category) && Objects.equals(dateExpired, product.dateExpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, category, price, dateExpired, quantity);
    }

    @Override
    public String toString() {
        return "Name Product:'" + name + '\'' +
                ", Description:'" + description + '\'' +
                ", Category:'" + category + '\'' +
                ", Price:" + price +
                ", Date Expired:" + dateExpired +
                ", Quantity:" + quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(LocalDate dateExpired) {
        this.dateExpired = dateExpired;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
