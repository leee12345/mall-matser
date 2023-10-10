package com.fo.pp.pojo.vo;

public class CartList {
    private Lists[] lists;
    private double totalPrice;

    public void setLists(Lists[] lists) {
        this.lists = lists;
    }

    public Lists[] getLists() {
        return lists;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
