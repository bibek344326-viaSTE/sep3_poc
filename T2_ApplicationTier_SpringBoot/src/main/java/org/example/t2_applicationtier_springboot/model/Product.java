package org.example.t2_applicationtier_springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("ProductName")
    private String productName;
    @JsonProperty("Price")
    private double price;

    // Constructors
    public Product() {}

    public Product(String productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
