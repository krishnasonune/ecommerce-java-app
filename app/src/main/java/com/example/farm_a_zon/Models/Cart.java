package com.example.farm_a_zon.Models;

public class Cart {

    private String pid, prodname, quantity, price, discount;

    public  Cart(){

    }

    public Cart(String pid, String prodname, String quantity, String price, String discount) {
        this.pid = pid;
        this.prodname = prodname;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
