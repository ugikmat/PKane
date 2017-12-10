package com.example.mat.pkane.Model;

/**
 * Created by Mat on 11/26/17.
 */

public class Therapis {
    private String Nama,Phone,Address,Email,Gender;

    public Therapis() {
    }

    public Therapis(String nama, String phone, String address, String email, String gender) {
        Nama = nama;
        Phone = phone;
        Address = address;
        Email = email;
        Gender = gender;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String Nama) {
        this.Nama = Nama;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
