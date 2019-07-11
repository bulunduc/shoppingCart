package com.bulunduc.shoppingcart.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.fragments.CategoryPageFragment;
import com.bulunduc.shoppingcart.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> categories;
    private LinkedHashMap<String, ArrayList<Item>> allProducts;
    private String highlightText;

    public ViewPagerAdapter(FragmentManager fm, LinkedHashMap<String, ArrayList<Item>> products, String highlight) {
        super(fm);
        categories = new ArrayList<>();
        categories.addAll(products.keySet());
        Collections.sort(categories);
        allProducts = products;
        highlightText = highlight != null ? highlight : "";
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.ARG_PAGE, position + 1);
        args.putStringArrayList(AppConstants.CATEGORIES, categories );
        args.putParcelableArrayList(AppConstants.PRODUCTS, allProducts.get(categories.get(position)));
        args.putString(AppConstants.HIGHLIGHT, highlightText);
        CategoryPageFragment fragment = new CategoryPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position);
    }
}
