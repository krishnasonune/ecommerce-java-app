package com.example.farm_a_zon.Models;

public class Orders {
    private String  name, address, city, phone, date, status, time,
            total_amount;

    public Orders() {
    }

    public Orders(String name, String address, String city, String phone, String date, String status, String time, String total_amount) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.date = date;
        this.status = status;
        this.time = time;
        this.total_amount = total_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}
