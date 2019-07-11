package com.bulunduc.shoppingcart.models;

import java.util.ArrayList;

public class Template {
    private String title;
    private ArrayList<Item> items;

    public Template(String title, ArrayList<Item> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
