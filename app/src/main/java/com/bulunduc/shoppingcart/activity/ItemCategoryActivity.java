package com.bulunduc.shoppingcart.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ItemCategoryActivity extends BaseActivity implements AddItemDialogClickListener {
    private static final String TAG = "ItemCategoryActivity";
    private static final int REQUEST_CODE = 100;
    private Activity mActivity;
    private Context mContext;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MaterialSearchView mSearchView;
    private Observable<String> mObservable;
    private DisposableObserver<String> mObserver;
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
            //проверяем что время сохранения вкладок еще не прошло
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
        mViewPager = findViewById(R.id.cart_container);
        mTabLayout = findViewById(R.id.tabs);
        mSearchView = findViewById(R.id.search_view);

        mObservable = Observable.create(emitter -> {
            MaterialSearchView.OnQueryTextListener listener = new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (!emitter.isDisposed()) { //если еще не отписались
                        emitter.onNext(s); //отправляем текущее состояние
                        return true;
                    }
                    return false;
                }
            };
            mSearchView.setOnQueryTextListener(listener);
        });
        mObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                if (s != null && !s.isEmpty()) {
                    LinkedHashMap<String, ArrayList<Item>> searchResults = new LinkedHashMap<>();
                    for (String category : mAllProducts.keySet()) {
                        for (Item item : mAllProducts.get(category)) {
                            if (item.getItemName().toLowerCase().contains(s.toLowerCase())) {
                                if (!searchResults.containsKey(category)) {
                                    searchResults.put(category, new ArrayList<>());
                                }
                                searchResults.get(category).add(item);
                            }
                            updateViewPager(searchResults, AppConstants.ZERO_VALUE_IDENTIFIER, s.toLowerCase());
                        }
                    }
                    if (searchResults.isEmpty())
                        AppUtilities.showToast(mContext, getString(R.string.nothing_found));
                } else {
                    updateViewPager(mAllProducts, AppConstants.ZERO_VALUE_IDENTIFIER, null);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mObservable.debounce(500, TimeUnit.MILLISECONDS)
                .filter(text -> !text.isEmpty())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
        initToolbar(true);
        setToolbarTitle(getString(R.string.app_name));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });
    }

    public void updateProductLists(Item model, String newCategory, String oldCategory, int position) {
        if (!newCategory.equals(oldCategory)) {
            mAllProducts.get(oldCategory).remove(position);
            if (!mAllProducts.keySet().contains(newCategory)) {
                mAllProducts.put(newCategory, new ArrayList<>());
                updateViewPager(mAllProducts, mAllProducts.size(), "");
            }
            mAllProducts.get(newCategory).add(model);
        } else {
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
        String category = getCategoryByItem(item);
        AppUtilities.addItemAndReturnCartList(mContext, new CartItem(item, category, false));
        AppUtilities.showToast(mContext, getString(R.string.product_added_to_cart));
    }

    private String getCategoryByItem(Item item) {
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                //AppUtilities.showToast(mContext, getString(R.string.will_be_soon));
                /*
                Intent intent = new Intent(ItemCategoryActivity.this, SettingsActivity.class);
                startActivity(intent);
                */
                break;
            case R.id.action_set_category_order:
                Intent intent = new Intent(ItemCategoryActivity.this, EditCategoriesActiviry.class);
                intent.putExtra(AppConstants.CATEGORIES, AppUtilities.getCategories(mContext));
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.add_item:
                try {
                    ArrayList<String> categories = AppUtilities.getCategories(mContext);
                    Bundle args = new Bundle();
                    args.putStringArrayList(AppConstants.KEY_ITEM_CATEGORIES, categories);
                    args.putInt(AppConstants.KEY_ITEM_CATEGORY_POSITION, mTabLayout.getSelectedTabPosition());
                    AddItemFragment fragment = new AddItemFragment();
                    fragment.setArguments(args);
                    fragment.show(getFragmentManager(), fragment.getTag());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        ArrayList<String> categories = data.getStringArrayListExtra(AppConstants.CATEGORIES);
        LinkedHashMap<String, ArrayList<Item>> products = new LinkedHashMap<>();
        for (String category : categories) {
            products.put(category, mAllProducts.get(category));
        }
        mAllProducts = products;
        updateViewPager(mAllProducts, 1, null);

        AppUtilities.saveProductList(mContext, mAllProducts);
    }

    @Override
    public void onItemAddClick(Item item, String category) {
        if (!mAllProducts.containsKey(category)) {
            mAllProducts.put(category, new ArrayList<>());
        }
        int position = findSimilarProductPosition(category, item);
        if (position < 0) {
            mAllProducts.get(category).add(item);
            AppUtilities.showToast(mContext, mContext.getResources().getString(R.string.added_to_list));
            updateViewPager(mAllProducts, getTabPositionByCategory(category), null);
        } else {
            Item similarItem = mAllProducts.get(category).get(position);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.found_similar_item)
                    .setMessage(getString(R.string.foundSimilarItemMessage, similarItem.getItemName(),
                            String.valueOf(similarItem.getCount()), similarItem.getCountUnit(),
                            String.valueOf(similarItem.getPrice())))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.yes), (dialog, whichButton) -> {
                        mAllProducts.get(category).add(item);
                        AppUtilities.showToast(mContext, mContext.getResources().getString(R.string.added_to_list));
                        updateViewPager(mAllProducts, getTabPositionByCategory(category), null);
                    })
                    .setNeutralButton(getString(R.string.update_current), (dialog, whichButton) -> {
                        mAllProducts.get(category).set(position, item);
                        AppUtilities.showToast(mContext, getString(R.string.updated_item));
                        updateViewPager(mAllProducts, getTabPositionByCategory(category), null);
                    })
                    .setNegativeButton(getString(R.string.no), null).show();
        }
    }

    private int findSimilarProductPosition(String category, Item item) {
        for (int i = 0; i < mAllProducts.get(category).size(); i++) {
            if (mAllProducts.get(category).get(i).getItemName().toLowerCase().equals(item.getItemName().toLowerCase())) {
                return i;
            }
        }
        return AppConstants.INVALID_VALUE_IDENTIFIER;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mObserver.dispose();
    }
}
