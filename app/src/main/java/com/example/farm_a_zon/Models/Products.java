package com.example.farm_a_zon.Models;

public class Products {
    private String prodname, description, price, Image, category, pid,
            time, date, productstatus;

    public Products() {

    }

    public Products(String prodname, String description, String price, String image, String category, String pid, String time, String date, String productstatus) {
        this.prodname = prodname;
        this.description = description;
        this.price = price;
        Image = image;
        this.category = category;
        this.pid = pid;
        this.time = time;
        this.date = date;
        this.productstatus = productstatus;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(String productstatus) {
        this.productstatus = productstatus;
    }
}
