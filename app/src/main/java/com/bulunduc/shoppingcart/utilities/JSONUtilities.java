package com.bulunduc.shoppingcart.utilities;

import android.content.Context;
import android.util.Log;

import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.data.preference.AppPreference;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.models.Template;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


class JSONUtilities {
    private static final String TAG = "JSONUtilities";
    private static JSONUtilities jsonUtilities = null;

    static JSONUtilities getInstance() {
        if (jsonUtilities == null) {
            jsonUtilities = new JSONUtilities();
        }
        return jsonUtilities;
    }

    String loadJson(Context context, String directory, String fileName) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            if (AppPreference.getInstance(context).getBoolean(AppConstants.KEY_FIRST_RUN, true)
                    && !fileName.equals(AppConstants.CART_JSON_FILE)) {
                bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(directory + fileName)));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
            }
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (AppPreference.getInstance(context).getBoolean(AppConstants.KEY_FIRST_RUN, true)) {
            writeToJSON(context, fileName, stringBuffer.toString());
        }
        Log.d(TAG, "loadJson: " + fileName + " " + stringBuffer.toString());
        return stringBuffer.toString();
    }

    void writeToJSON(Context context, String fileName, String json) {
        Log.d(TAG, "writeToJSON: " + json);
        Writer writer = null;
        try {
            OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            AppPreference.getInstance(context).setBoolean(AppConstants.KEY_FIRST_RUN, false);
            writer = new OutputStreamWriter(outputStream);
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String parceCartItemsToJSON(ArrayList<CartItem> cartList) {
        String json = "";
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < cartList.size(); j++) {
                CartItem cartItem = cartList.get(j);
                JSONObject object = new JSONObject();
                JSONObject itemObject = new JSONObject();

                itemObject.put(AppConstants.JSON_KEY_PROD_TITLE, cartItem.getItem().getItemName());
                itemObject.put(AppConstants.JSON_KEY_PROD_COUNT, cartItem.getItem().getCount());
                itemObject.put(AppConstants.JSON_KEY_PROD_UNIT, cartItem.getItem().getCountUnit());
                itemObject.put(AppConstants.JSON_KEY_PROD_PRICE, cartItem.getItem().getPrice());

                object.put(AppConstants.JSON_KEY_CART_ITEM, itemObject);
                object.put(AppConstants.JSON_KEY_CATEGORY, cartItem.getCategory());
                object.put(AppConstants.JSON_KEY_CART_ITEM_IS_BUYED, cartItem.isBuyed());
                jsonArray.put(object);
            }
            jsonObject.put(AppConstants.JSON_KEY_CART_ITEMS, jsonArray);
            json = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    String parceAllProductsToJSON(HashMap<String, ArrayList<Item>> productList) {
        String json = "";
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (String category : productList.keySet()) {
                JSONObject categoryJsonObject = new JSONObject();
                categoryJsonObject.put(AppConstants.JSON_KEY_CATEGORY, category);
                JSONArray productsJsonArray = new JSONArray();
                for (Item item : productList.get(category)) {
                    JSONObject object = new JSONObject();
                    object.put(AppConstants.JSON_KEY_PROD_TITLE, item.getItemName());
                    object.put(AppConstants.JSON_KEY_PROD_COUNT, item.getCount());
                    object.put(AppConstants.JSON_KEY_PROD_UNIT, item.getCountUnit());
                    object.put(AppConstants.JSON_KEY_PROD_PRICE, item.getPrice());
                    productsJsonArray.put(object);
                }
                categoryJsonObject.put(AppConstants.JSON_KEY_PRODUCTS, productsJsonArray);
                jsonArray.put(categoryJsonObject);
            }
            jsonObject.put(AppConstants.JSON_KEY_ALL_PRODUCTS, jsonArray);
            json = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    String parceTemplatesToJSON(ArrayList<Template> templates) {
        String json = "";
        try{
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (Template template : templates) {
                JSONObject templateJSONObject = new JSONObject();
                templateJSONObject.put(AppConstants.JSON_KEY_CATEGORY,template.getTitle() );
                JSONArray productsJsonArray = new JSONArray();
                for (Item item : template.getItems()) {
                    JSONObject object = new JSONObject();
                    object.put(AppConstants.JSON_KEY_PROD_TITLE, item.getItemName());
                    object.put(AppConstants.JSON_KEY_PROD_COUNT, item.getCount());
                    object.put(AppConstants.JSON_KEY_PROD_UNIT, item.getCountUnit());
                    object.put(AppConstants.JSON_KEY_PROD_PRICE, item.getPrice());
                    productsJsonArray.put(object);
                }
                templateJSONObject.put(AppConstants.JSON_KEY_PRODUCTS, productsJsonArray);
                jsonArray.put(templateJSONObject);
            }
            jsonObject.put(AppConstants.JSON_KEY_TEMPLATES_ITEMS, jsonArray);
            json = jsonObject.toString();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    ArrayList<Template> parceJSONTemplates(String json) {
        ArrayList<Template> mTemplates = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_KEY_TEMPLATES_ITEMS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String title = object.getString(AppConstants.JSON_KEY_CATEGORY);
                ArrayList<Item> products = new ArrayList<>();
                JSONArray productsArray = object.getJSONArray(AppConstants.JSON_KEY_PRODUCTS);
                for (int j = 0; j < productsArray.length(); j++) {
                    JSONObject categoryProduct = productsArray.getJSONObject(j);
                    String item = categoryProduct.getString(AppConstants.JSON_KEY_PROD_TITLE);
                    Double minCount = categoryProduct.getDouble(AppConstants.JSON_KEY_PROD_COUNT);
                    String unit = categoryProduct.getString(AppConstants.JSON_KEY_PROD_UNIT);
                    Double price = categoryProduct.getDouble(AppConstants.JSON_KEY_PROD_PRICE);
                    products.add(new Item(item, minCount, unit, price));
                }
                mTemplates.add(new Template(title, products));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mTemplates;
    }

    LinkedHashMap<String, ArrayList<Item>> parceJSONAllProducts(String json) {
        LinkedHashMap<String, ArrayList<Item>> mAllProducts = new LinkedHashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_KEY_ALL_PRODUCTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String category = object.getString(AppConstants.JSON_KEY_CATEGORY);
                ArrayList<Item> categoryProducts = new ArrayList<>();
                JSONArray categoryProductsArray = object.getJSONArray(AppConstants.JSON_KEY_PRODUCTS);
                for (int j = 0; j < categoryProductsArray.length(); j++) {
                    JSONObject categoryProduct = categoryProductsArray.getJSONObject(j);
                    String item = categoryProduct.getString(AppConstants.JSON_KEY_PROD_TITLE);
                    Double minCount = categoryProduct.getDouble(AppConstants.JSON_KEY_PROD_COUNT);
                    String unit = categoryProduct.getString(AppConstants.JSON_KEY_PROD_UNIT);
                    Double price = categoryProduct.getDouble(AppConstants.JSON_KEY_PROD_PRICE);
                    categoryProducts.add(new Item(item, minCount, unit, price));
                }
                mAllProducts.put(category, categoryProducts);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAllProducts;
    }

    ArrayList<CartItem> parceJSONCartItems(String json) {
        ArrayList<CartItem> cartList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_KEY_CART_ITEMS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                JSONObject itemObject = object.getJSONObject(AppConstants.JSON_KEY_CART_ITEM);
                Item item = new Item(itemObject.getString(AppConstants.JSON_KEY_PROD_TITLE),
                        itemObject.getDouble(AppConstants.JSON_KEY_PROD_COUNT),
                        itemObject.getString(AppConstants.JSON_KEY_PROD_UNIT),
                        itemObject.getDouble(AppConstants.JSON_KEY_PROD_PRICE));
                String category = object.getString(AppConstants.JSON_KEY_CATEGORY);
                boolean isBuyed = object.getBoolean(AppConstants.JSON_KEY_CART_ITEM_IS_BUYED);
                cartList.add(new CartItem(item, category, isBuyed));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cartList;
    }
}
