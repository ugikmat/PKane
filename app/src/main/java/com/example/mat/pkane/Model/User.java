package com.example.mat.pkane.Model;

/**
 * Created by Mat on 10/22/17.
 */

public class User {
    private String Email;
    private String Nama;
    private String Address;
    private String Gender;
    private String Password;
    private String Phone;
    private boolean Admin;

    public User(){

    }

    public User(String email, String nama, String address, String gender, String password, String phone, boolean admin) {
        Email = email;
        Nama = nama;
        Address = address;
        Gender = gender;
        Password = password;
        Phone = phone;
        Admin = admin;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }
}
