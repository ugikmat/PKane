package com.example.mat.pkane.Model;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mat on 11/20/17.
 */

public class Request {
    private String phone;
    private String name;
    private String gender;
    private String date;
    private String time;
    private String total;
    private List<Order> pijat;

    public Request() {
    }

    public Request(String phone, String name, String gender, String date, String time, String total, List<Order> pijat) {
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.date = date;
        this.time = time;
        this.total = total;
        this.pijat = pijat;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getPijat() {
        return pijat;
    }

    public void setPijat(List<Order> pijat) {
        this.pijat = pijat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void updatePrice(){
        Locale locale = new Locale("en","US");
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);
        int price=0;
        for(Order order:pijat){
            price+=(Integer.parseInt(order.getPrice()))-((Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getDiscount())));
        }
        setTotal(fmt.format(price));
    }
}
