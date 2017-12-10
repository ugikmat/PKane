package com.example.mat.pkane.Model;

/**
 * Created by Mat on 11/12/17.
 */

public class Order {
    private String ProductID;
    private String ProductName;
    private String Price;
    private String Discount;

    public Order() {
    }

    public Order(String productID, String productName, String price, String discount) {
        ProductID = productID;
        ProductName = productName;
        Price = price;
        Discount = discount;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
