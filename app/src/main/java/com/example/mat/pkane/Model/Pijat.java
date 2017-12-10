package com.example.mat.pkane.Model;

/**
 * Created by Mat on 11/11/17.
 */

public class Pijat {
    private String Id;
    private String Name;
    private String Image;
    private String Desc;
    private String Price;
    private String Discount;

    public Pijat() {
    }

    public Pijat(String name, String image, String desc, String price) {
        Name = name;
        Image = image;
        Desc = desc;
        Price = price;
    }

    public Pijat(String id, String name, String image, String desc, String price, String discount) {
        Id = id;
        Name = name;
        Image = image;
        Desc = desc;
        Price = price;
        Discount = discount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
