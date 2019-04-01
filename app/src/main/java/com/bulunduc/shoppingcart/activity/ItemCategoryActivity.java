package com.bulunduc.shoppingcart.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.ViewPagerAdapter;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.data.preference.AppPreference;
import com.bulunduc.shoppingcart.fragments.AddItemFragment;
import com.bulunduc.shoppingcart.fragments.ItemAddBottomSheetDialogFragment;
import com.bulunduc.shoppingcart.listeners.AddItemDialogClickListener;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.ActivityUtilities;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ItemCategoryActivity extends BaseActivity implements AddItemDialogClickListener {
    private static final String TAG = "ItemCategoryActivity";
    private Activity mActivity;
    private Context mContext;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private static int savedCurrentTab = AppConstants.ZERO_VALUE_IDENTIFIER;

    private LinkedHashMap<String, ArrayList<Item>> mAllProducts;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstants.KEY_SAVED_TAB, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initVar();
        if (savedInstanceState!=null) savedCurrentTab = savedInstanceState.getInt(AppConstants.KEY_SAVED_TAB);
        else {
            //проверяем что время еще не прошло
            if (AppPreference.getInstance(mContext).getLong(AppConstants.KEY_SAVING_TIME) + AppConstants.KEY_TIME >
                    System.currentTimeMillis()) {
                savedCurrentTab = AppPreference.getInstance(mContext).getInt(AppConstants.KEY_SAVED_TAB);
            }
        }
        updateViewPager(savedCurrentTab);
    }


    public void initVar() {
        mActivity = ItemCategoryActivity.this;
        mContext = mActivity.getApplicationContext();
        mAllProducts = new LinkedHashMap<>();
        mAllProducts = AppUtilities.getProductList(mContext);
        }

    public void initView() {
        setContentView(R.layout.activity_item_category_viewpager);
        mViewPager = findViewById(R.id.cartContainer);
        mTabLayout = findViewById(R.id.tabs);

        initToolbar(true);
        setToolbarTitle(getString(R.string.app_name));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_lists:
                        break;
                    case R.id.action_templates:
                        Intent intent1 = new Intent(ItemCategoryActivity.this, TemplatesActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.action_cart:
                        Intent intent2 = new Intent(ItemCategoryActivity.this, CartActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
    }

    public void updateProductLists(Item model, String newCategory, String oldCategory, int position) {
        if (!newCategory.equals(oldCategory)) {
            mAllProducts.get(oldCategory).remove(position);
            if (!mAllProducts.keySet().contains(newCategory))
            {
                mAllProducts.put(newCategory, new ArrayList<Item>());
                updateViewPager(mAllProducts.size());
            }
            mAllProducts.get(newCategory).add(model);
        } else {
            mAllProducts.get(oldCategory).set(position, model);
        }
    }

    public void deleteItem(String category, int position){
        mAllProducts.get(category).remove(position);
        updateViewPager(getTabPositionByCategory(category));
    }

    private void updateViewPager(int position) {
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mAllProducts));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);
    }

    public void addToCart(Item item) {
        String category = getCategory(item);
        //here was fixed setCount and ItemViewAdapter
        AppUtilities.addItemAndReturnCartList(mContext, new CartItem(item, category, false));
        AppUtilities.showToast(mContext, "Товар добавлен в корзину");
    }

    private String getCategory(Item item){
        for (String s : mAllProducts.keySet()) {
            for (Item model : mAllProducts.get(s)) {
                if (model.equals(item)) {
                    return s;
                }
            }
        }
        return "";
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppPreference.getInstance(mContext).setInt(AppConstants.KEY_SAVED_TAB, mTabLayout.getSelectedTabPosition());
        AppPreference.getInstance(mContext).setLong(AppConstants.KEY_SAVING_TIME, System.currentTimeMillis());
        AppUtilities.saveProductList(mContext, mAllProducts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ItemCategoryActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.add_item){
            try {
                ArrayList<String> categories = AppUtilities.getCategories(mContext);
                AddItemFragment addItemFragment = AddItemFragment.newInstance(this, categories, 1);
                addItemFragment.show(getFragmentManager(), addItemFragment.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemAddClick(Item item, String category) {
        if (!mAllProducts.containsKey(category)) {
            mAllProducts.put(category, new ArrayList<Item>());
        }
        mAllProducts.get(category).add(item);
        updateViewPager(getTabPositionByCategory(category));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtilities.hideKeyboard(this);
        mViewPager.setCurrentItem(savedCurrentTab);
    }

    private int getTabPositionByCategory(String category){
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (mTabLayout.getTabAt(i).getText().toString().equals(category)) {
                return i;
            }
        }
        return AppConstants.INVALID_VALUE_IDENTIFIER;
    }
    public void deleteCurrentCategory() {
        int position =  mTabLayout.getSelectedTabPosition();
        String currentCategory = mTabLayout.getTabAt(position).getText().toString();
        mAllProducts.remove(currentCategory);
        updateViewPager(position);
        try {
            mViewPager.setCurrentItem(position);
        } catch (Exception e){
            mViewPager.setCurrentItem(position - 1);
        }
        AppUtilities.showLongToast(mContext, String.format(getString(R.string.deleted_category), currentCategory));
        }
}
