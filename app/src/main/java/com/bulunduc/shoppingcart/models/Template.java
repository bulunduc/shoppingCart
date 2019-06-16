package com.bulunduc.shoppingcart.models;

import java.util.ArrayList;

public class Template {
    private String title;
    private int imageId;
    private ArrayList<CartItem> items;

    public Template(String title, int imageId, ArrayList<CartItem> items) {
        this.title = title;
        this.imageId = imageId;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}
