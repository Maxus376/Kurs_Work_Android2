package com.maxus376.kostromafastfooddelivery;

public class ListDataOrder {
    String id, where, price, time;

    public ListDataOrder(String id, String where, String price, String time) {
        this.id = id;
        this.where = where;
        this.price = price;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getWhere() {
        return where;
    }


    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}