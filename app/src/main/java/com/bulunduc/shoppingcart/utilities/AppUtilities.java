package com.bulunduc.shoppingcart.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.models.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AppUtilities {
    private static final String TAG = "AppUtilities";
    private static long backPressed = 0;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void tapPromtToExit(Activity activity) {
        if (backPressed + 2500 > System.currentTimeMillis()) {
            activity.finish();
        } else {
            showToast(activity.getApplicationContext(), "");
        }
        backPressed = System.currentTimeMillis();
    }
    public static ArrayList<Template> getTemplatesList(Context context) {
        String jsonTemplatesList = JSONUtilities.getInstance().loadJson(context, AppConstants.DEFAULT_PRODUCT_DIRECTORY, AppConstants.TEMPLATES_JSON_FILE);
        Log.d(TAG, jsonTemplatesList);
        return JSONUtilities.getInstance().parceJSONTemplates(jsonTemplatesList);
    }

    public static void saveTemplatesList(Context context, ArrayList<Template> templates) {
        JSONUtilities.getInstance().writeToJSON(context, AppConstants.TEMPLATES_JSON_FILE, JSONUtilities.getInstance().parceTemplatesToJSON(templates));
    }

    public static LinkedHashMap<String, ArrayList<Item>> getProductList(Context context) {
        String jsonAllProductsList = JSONUtilities.getInstance().loadJson(context, AppConstants.DEFAULT_PRODUCT_DIRECTORY, AppConstants.ALL_PRODUCT_JSON_FILE);
        return JSONUtilities.getInstance().parceJSONAllProducts(jsonAllProductsList);
    }

    public static void saveProductList(Context context, HashMap<String, ArrayList<Item>> products) {
        JSONUtilities.getInstance().writeToJSON(context, AppConstants.ALL_PRODUCT_JSON_FILE, JSONUtilities.getInstance().parceAllProductsToJSON(products));
    }

    public static void addItemToProductList(Context context, String category, Item item) {
        HashMap<String, ArrayList<Item>> products = getProductList(context);
        if (!products.keySet().contains(category)) {
            products.put(category, new ArrayList<Item>());
        }
        products.get(category).add(item);
        saveProductList(context, products);
        showToast(context, context.getResources().getString(R.string.added_to_list));

    }

    public static ArrayList<CartItem> addItemAndReturnCartList(Context context, CartItem cartItem) {
        ArrayList<CartItem> mCartItemList = getCartList(context);
        boolean isUpdated = false;
        for (CartItem item : mCartItemList) {
            if (item.getCategory().equals(cartItem.getCategory()) &&
                    item.getItem().getItemName().equals(cartItem.getItem().getItemName()) &&
                    item.getItem().getPrice() == cartItem.getItem().getPrice()) {
                item.getItem().addCount(cartItem.getItem().getCount(), cartItem.getItem().getCountUnit());
                isUpdated = true;
                break;
            }
        }
        if (!isUpdated) mCartItemList.add(cartItem);
        saveCartList(context, mCartItemList);
        return mCartItemList;
    }

    public static ArrayList<String> getCategories(Context context) {
        ArrayList<String> categories = new ArrayList<>();
        categories.addAll(getProductList(context).keySet());
        return categories;
    }

    public static ArrayList<CartItem> getCartList(Context context) {
        String jsonCartItems = JSONUtilities.getInstance().loadJson(context, "", AppConstants.CART_JSON_FILE);
        return JSONUtilities.getInstance().parceJSONCartItems(jsonCartItems);
    }

    public static void saveCartList(Context context, ArrayList<CartItem> cartList) {
        JSONUtilities.getInstance().writeToJSON(context, AppConstants.CART_JSON_FILE, JSONUtilities.getInstance().parceCartItemsToJSON(cartList));
    }
}
