package com.bulunduc.shoppingcart.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

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

    public static void shareApp(Activity activity) {
        try {
            final String appPackageName = activity.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Поделиться https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static LinkedHashMap<String, ArrayList<Item>> getProductList(Context context) {
        String jsonAllProductsList = JSONUtilities.getInstance().loadJson(context, AppConstants.DEFAULT_PRODUCT_DIRECTORY, AppConstants.ALL_PRODUCT_JSON_FILE);
        return JSONUtilities.getInstance().parceJSONAllProducts(jsonAllProductsList);
    }

    public static void saveProductList(Context context, HashMap<String, ArrayList<Item>> products) {
        JSONUtilities.getInstance().writeToJSON(context, AppConstants.ALL_PRODUCT_JSON_FILE, JSONUtilities.getInstance().parceAllProductsToJSON(products));
    }

    public static void addItemToProductList(Context context, String category, Item item) {
        //todo setting(сделать галку - сохранять из корзины в списки или нет)
        HashMap<String, ArrayList<Item>> products = getProductList(context);
        if (!products.keySet().contains(category)) {
            products.put(category, new ArrayList<Item>());
        }
        products.get(category).add(item);
        saveProductList(context, products);
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
        String jsonAllProductsList = JSONUtilities.getInstance().loadJson(context, AppConstants.DEFAULT_PRODUCT_DIRECTORY, AppConstants.ALL_PRODUCT_JSON_FILE);
        Set<String> categories = JSONUtilities.getInstance().parceJSONAllProducts(jsonAllProductsList).keySet();
        return new ArrayList<>(categories);
    }

    public static ArrayList<CartItem> getCartList(Context context) {
        String jsonCartItems = JSONUtilities.getInstance().loadJson(context, "", AppConstants.CART_JSON_FILE);
        return JSONUtilities.getInstance().parceJSONCartItems(jsonCartItems);
    }

    public static void saveCartList(Context context, ArrayList<CartItem> cartList) {
        JSONUtilities.getInstance().writeToJSON(context, AppConstants.CART_JSON_FILE, JSONUtilities.getInstance().parceCartItemsToJSON(cartList));
    }


    public static void rateThisApp(Activity activity) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
