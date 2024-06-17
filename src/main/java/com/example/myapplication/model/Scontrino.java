package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scontrino {

    Long id;
    List<Product> productList;
    String description;
    double price;


    public Scontrino() {
        this(null, "", 0);
    }

    public Scontrino(Long id, String description, double price) {
        productList = new ArrayList<>();
        this.id = id;
        this.description = description;
        this.price = 0;
    }


    @Override
    public String toString() {
        return "Scontrino{" +
                "id=" + id +
                ", productList=" + productList +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scontrino scontrino = (Scontrino) o;
        return Double.compare(price, scontrino.price) == 0 && Objects.equals(id, scontrino.id) && Objects.equals(productList, scontrino.productList) && Objects.equals(description, scontrino.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productList, description, price);
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Product product : productList) {
            totalPrice = totalPrice + (product.getPrice() * product.getQuantity());
        }
        return totalPrice;
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public List<Product> getProductList() {
        return productList;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
