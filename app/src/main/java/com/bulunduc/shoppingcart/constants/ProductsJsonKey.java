package com.bulunduc.shoppingcart.constants;

public enum ProductsJsonKey {
    PRODUCT_FILE("all_products.json"), PRODUCTS("products"), CATEGORY("category"), IMAGE("image_res"),
    TITLE("title"), COUNT("count"), UNIT("unit"), PRICE("price");

    private final String description;
    ProductsJsonKey(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
