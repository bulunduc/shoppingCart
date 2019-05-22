package com.bulunduc.shoppingcart.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.ViewPagerAdapter;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.data.preference.AppPreference;
import com.bulunduc.shoppingcart.fragments.AddItemFragment;
import com.bulunduc.shoppingcart.listeners.AddItemDialogClickListener;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.ActivityUtilities;
import com.bulunduc.shoppingcart.utilities.AppUtilities;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemCategoryActivity extends BaseActivity implements AddItemDialogClickListener {
    private static final String TAG = "ItemCategoryActivity";
    private Activity mActivity;
    private Context mContext;

    @BindView(R.id.cartContainer) protected ViewPager mViewPager;
    @BindView(R.id.tabs) protected TabLayout mTabLayout;
    @BindView(R.id.search_view) protected MaterialSearchView mSearchView;
    @BindView(R.id.bottom_navigation) protected BottomNavigationView bottomNavigationView;

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
        if (savedInstanceState != null)
            savedCurrentTab = savedInstanceState.getInt(AppConstants.KEY_SAVED_TAB);
        else {
            //проверяем что время еще не прошло
            if (AppPreference.getInstance(mContext).getLong(AppConstants.KEY_SAVING_TIME) + AppConstants.KEY_TIME >
                    System.currentTimeMillis()) {
                savedCurrentTab = AppPreference.getInstance(mContext).getInt(AppConstants.KEY_SAVED_TAB);
            }
        }
        updateViewPager(mAllProducts, savedCurrentTab, "");
    }


    public void initVar() {
        mActivity = ItemCategoryActivity.this;
        mContext = mActivity.getApplicationContext();
        mAllProducts = new LinkedHashMap<>();
        mAllProducts = AppUtilities.getProductList(mContext);
    }

    public void initView() {
        setContentView(R.layout.activity_item_category_viewpager);
        ButterKnife.bind(this);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    LinkedHashMap<String, ArrayList<Item>> searchResults = new LinkedHashMap<>();
                    for (String category : mAllProducts.keySet()) {
                        for (Item item : mAllProducts.get(category)) {
                            if (item.getItemName().toLowerCase().contains(newText.toLowerCase())) {
                                if (!searchResults.containsKey(category)) {
                                    searchResults.put(category, new ArrayList<Item>());
                                }
                                searchResults.get(category).add(item);

                            }
                            updateViewPager(searchResults, AppConstants.ZERO_VALUE_IDENTIFIER, newText.toLowerCase());

                        }
                    }
                    if (searchResults.isEmpty())
                        AppUtilities.showToast(mContext, "Ничего не найдено");
                } else {
                    updateViewPager(mAllProducts, AppConstants.ZERO_VALUE_IDENTIFIER, null);
                }
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                updateViewPager(mAllProducts, AppConstants.ZERO_VALUE_IDENTIFIER, "");

            }
        });

        initToolbar(true);
        setToolbarTitle(getString(R.string.app_name));

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
            if (!mAllProducts.keySet().contains(newCategory)) {
                mAllProducts.put(newCategory, new ArrayList<Item>());
                updateViewPager(mAllProducts, mAllProducts.size(), "");
            }
            mAllProducts.get(newCategory).add(model);
        } else {
            Log.d(TAG, "updateProductLists: " + mAllProducts.get(oldCategory).get(0).getItemName());
            mAllProducts.get(oldCategory).set(position, model);
        }
    }

    public void deleteItem(String category, int position) {
        mAllProducts.get(category).remove(position);
        updateViewPager(mAllProducts, getTabPositionByCategory(category), "");
    }

    private void updateViewPager(LinkedHashMap<String, ArrayList<Item>> products, int position, String searchText) {
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), products, searchText));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);
    }

    public void addToCart(Item item) {
        String category = getCategory(item);
        //here was fixed setCount and ItemViewAdapter
        AppUtilities.addItemAndReturnCartList(mContext, new CartItem(item, category, false));
        AppUtilities.showToast(mContext, getString(R.string.product_added_to_cart));
    }

    private String getCategory(Item item) {
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView.setMenuItem(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AppUtilities.showToast(mContext, getString(R.string.will_be_soon));
            /*
            Intent intent = new Intent(ItemCategoryActivity.this, SettingsActivity.class);
            startActivity(intent);
            */
        }
        if (id == R.id.add_item) {
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
        AppUtilities.showToast(mContext, mContext.getResources().getString(R.string.added_to_list));
        updateViewPager(mAllProducts, getTabPositionByCategory(category), null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtilities.hideKeyboard(this);
        mViewPager.setCurrentItem(savedCurrentTab);
    }

    private int getTabPositionByCategory(String category) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (mTabLayout.getTabAt(i).getText().toString().equals(category)) {
                return i;
            }
        }
        return AppConstants.INVALID_VALUE_IDENTIFIER;
    }

    public void deleteCurrentCategory() {
        int position = mTabLayout.getSelectedTabPosition();
        String currentCategory = mTabLayout.getTabAt(position).getText().toString();
        mAllProducts.remove(currentCategory);
        updateViewPager(mAllProducts, position, null);
        try {
            mViewPager.setCurrentItem(position);
        } catch (Exception e) {
            mViewPager.setCurrentItem(position - 1);
        }
        AppUtilities.showLongToast(mContext, String.format(getString(R.string.deleted_category), currentCategory));
    }
}
