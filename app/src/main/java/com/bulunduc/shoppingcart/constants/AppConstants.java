package com.bulunduc.shoppingcart.constants;

public class AppConstants {


    public static final int INVALID_VALUE_IDENTIFIER = -1;
    public static final int ZERO_VALUE_IDENTIFIER = 0;
    public static final double ZERO_DOUBLE_VALUE = 0.0;

    //AppPreference
    public static final String KEY_FIRST_RUN = "first_run";
    public static final String KEY_SAVED_TAB = "saved_tab";
    public static final String KEY_SAVING_TIME = "time";
    public static final long KEY_TIME = 3600000; // 1 hour


    // product file
    public static final String DEFAULT_PRODUCT_DIRECTORY = "json/products/";

    public static final String ALL_PRODUCT_JSON_FILE = "all_products.json";
    public static final String JSON_KEY_ALL_PRODUCTS = "all";
    public static final String JSON_KEY_PRODUCTS = "products";
    public static final String JSON_KEY_PROD_IMAGE = "image_res";
    public static final String JSON_KEY_PROD_TITLE = "title";
    public static final String JSON_KEY_PROD_COUNT = "count";
    public static final String JSON_KEY_PROD_UNIT = "unit";
    public static final String JSON_KEY_PROD_PRICE = "price";

    // cart file
    public static final String CART_JSON_FILE = "cart_list.json";
    public static final String JSON_KEY_CART_ITEMS = "cart_items";
    public static final String JSON_KEY_CART_ITEM = "item";
    public static final String JSON_KEY_CATEGORY = "category";
    public static final String JSON_KEY_CART_ITEM_IS_BUYED = "buyed";

    //templates file
    public static final String DEFAULT_TEMPLATES_DIRECTORY = "json/templates/";

    public static final String TEMPLATES_JSON_FILE = "templates.json";
    public static final String JSON_KEY_TEMPLATES_ITEMS = "templates";
    public static final String JSON_KEY_TEMPLATES_TITLE = "title";
    public static final String JSON_KEY_TEMPLATES_IMAGE = "image_res";


    //weight constants
    public static final int WEIGHT_MIN_KG_L_PIECE_STEP = 1;

    //dialog fragment edit item
    public static final String KEY_ITEM = "item";
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_ITEM_MIN_COUNT = "item_min_count";
    public static final String KEY_ITEM_UNIT = "item_unit";
    public static final String KEY_ITEM_PRICE = "item_price";
    public static final String KEY_ITEM_CATEGORY = "item_category";
    public static final String KEY_ITEM_CATEGORIES = "item_categories";
    public static final String KEY_DIALOG_FRAGMENT = "dialog_fragment";
    public static final String KEY_POSITION = "position";
    public static final int EDIT_ITEM_REQUEST_CODE = 100;

    public static final String APP_PREF_NAME = "shopping_cart_app_pref";
    public static final String KEY_ITEM_CATEGORY_POSITION = "position";
}
