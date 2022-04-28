package com.example.farm_a_zon.Models;

public class Admins {
    private String name, phone, password, address;

    public Admins() {
    }

    public Admins(String name, String phone, String password, String address) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
