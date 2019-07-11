package com.bulunduc.shoppingcart.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.CategorySectionAdapter;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.fragments.AddItemFragment;
import com.bulunduc.shoppingcart.listeners.AddItemDialogClickListener;
import com.bulunduc.shoppingcart.listeners.CartItemIsBuyedCheckListener;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CartActivity extends BaseActivity implements CartItemIsBuyedCheckListener, View.OnClickListener, AddItemDialogClickListener {
    private static final String TAG = "CartActivity";
    private Activity mActivity;
    private Context mContext;
    private ArrayList<CartItem> mCartItemList;
    private ArrayList<CartItem> mCartCheckedItemList;
    private Double mTotalPrice;
    private Double mCheckedItemsPrice;

    private SectionedRecyclerViewAdapter mSectionAdapter;

    private RecyclerView rvCart;
    private TextView tvEmptyView;
    private TextView tvTotalPrice;
    private TextView tvCheckedItemsPrice;
    private FloatingActionButton fabAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        initView();
    }

    public void initVar() {
        mActivity = CartActivity.this;
        mContext = mActivity.getApplicationContext();
        mCartCheckedItemList = new ArrayList<>();
        loadCartItemsData();
        mSectionAdapter = new SectionedRecyclerViewAdapter();
    }

    private void loadCartItemsData() {
        mCartItemList = AppUtilities.getCartList(mContext);
        updatePriceValues();
    }

    public void initView() {
        setContentView(R.layout.activity_cart);
        rvCart = findViewById(R.id.recycler_view_cart);
        tvEmptyView = findViewById(R.id.empty_view);
        showRecyclerView();
        rvCart.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        initSectionAdapter();
        rvCart.setAdapter(mSectionAdapter);

        initToolbar(true);
        setToolbarTitle(getString(R.string.cart));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_cart:
                    break;
                case R.id.action_templates:
                    Intent intent1 = new Intent(CartActivity.this, TemplatesActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.action_lists:
                    Intent intent2 = new Intent(CartActivity.this, ItemCategoryActivity.class);
                    startActivity(intent2);
                    break;
            }
            return false;
        });

        tvTotalPrice = findViewById(R.id.total_price);
        tvCheckedItemsPrice = findViewById(R.id.checked_items_price);
        updatePriceTextView();

        fabAddItem = findViewById(R.id.fab_add_item);
        fabAddItem.setOnClickListener(this);
    }

    private void showRecyclerView() {
        if (mCartItemList.isEmpty()) {
            rvCart.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            rvCart.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }

    private ArrayList<CartItem> getCartItemsByCategory(String category) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        for (CartItem cartItemModel : mCartItemList) {
            if (String.valueOf(cartItemModel.getCategory()).equals(category)) {
                //sum analog items
                if (cartItems.contains(cartItemModel)) {
                    int position = cartItems.indexOf(cartItemModel);
                    cartItems.get(position).getItem()
                            .addCount(cartItemModel.getItem().getCount(), cartItemModel.getItem().getCountUnit());
                } else {
                    cartItems.add(cartItemModel);
                }

            }
        }
        return cartItems;
    }


    private void updateAndWriteData() {
        AppUtilities.saveCartList(mContext, mCartItemList);
        updatePrice();
        updateSectionAdapter();
        showRecyclerView();
        Log.d(TAG, "updateAndWriteData: updated" );
    }

    //-------------SECTION_ADAPTER----------------//
    private void updateSectionAdapter() {
        mSectionAdapter.removeAllSections();
        initSectionAdapter();

    }

    private void initSectionAdapter() {
        for (String category : getCategories()) {
            mSectionAdapter.addSection(new CategorySectionAdapter(mContext, category, getCartItemsByCategory(category), CartActivity.this));
        }
    }

    private Set<String> getCategories() {
        Set<String> categories = new TreeSet<>();
        for (CartItem cartItemModel : mCartItemList) {
            categories.add(String.valueOf(cartItemModel.getCategory()));
        }
        return categories;
    }

    //---------------PRICE METHODS---------------------//
    private void updatePrice() {
        updatePriceValues();
        updatePriceTextView();
    }

    private void updatePriceValues() {
        zeroPriceValues();
        for (CartItem cartItem : mCartItemList) {
            mTotalPrice = mTotalPrice + cartItem.getItem().getFinalPrice();
            if (cartItem.isBuyed()) {
                mCheckedItemsPrice = mCheckedItemsPrice + cartItem.getItem().getFinalPrice();
            }

        }
    }

    private void updatePriceTextView() {
        tvTotalPrice.setText(mTotalPrice.toString());
        tvCheckedItemsPrice.setText(mCheckedItemsPrice.toString());
    }

    private void zeroPriceValues() {
        mTotalPrice = Double.valueOf(0.0);
        mCheckedItemsPrice = Double.valueOf(0.0);
    }

    //------------------------------------------------//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                List<Intent> targetShareIntents = new ArrayList<>();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(sendIntent, 0);
                if (!resInfos.isEmpty()) {
                    for (ResolveInfo resInfo : resInfos) {
                        String packageName = resInfo.activityInfo.packageName;
                        if (packageName.contains("whatsapp") || packageName.contains("sms")
                                || packageName.contains("viber") || packageName.contains("skype")) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, cartListToSms());
                            intent.setPackage(packageName);
                            targetShareIntents.add(intent);
                        }
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra("sms_body", cartListToSms());
                    intent.setType("vnd.android-dir/mms-sms");
                    targetShareIntents.add(intent);

                    if (!targetShareIntents.isEmpty()) {
                        Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), getString(R.string.choose_app));
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                        startActivity(chooserIntent);
                    } else {
                        AppUtilities.showToast(mContext, getString(R.string.fail_sending));
                    }
                }
                return true;
            case R.id.action_deleteAll:
                mCartItemList.clear();
                updateAndWriteData();
                break;
            case R.id.action_deleteChecked:
                mCartItemList.removeAll(mCartCheckedItemList);
                mSectionAdapter.notifyDataSetChanged();
                updateAndWriteData();
                break;
            case R.id.action_settings:
                AppUtilities.showToast(mContext, getString(R.string.will_be_soon));
                /*
                Intent intent = new Intent(CartActivity.this, SettingsActivity.class);
                startActivity(intent);
                */
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private String cartListToSms() {
        StringBuilder sms = new StringBuilder();
        String category = "";
        for (CartItem cartItemModel : mCartItemList) {
            if (!category.equals(cartItemModel.getCategory())) {
                if (sms.length() > 0) {
                    sms.deleteCharAt(sms.length() - 2)
                            .append(" / ");
                }
                sms.append(cartItemModel.getCategory());
                sms.append(": ");
                category = cartItemModel.getCategory();
            }
            sms.append(cartItemModel.getItem().getItemName())
                    .append("(")
                    .append(cartItemModel.getItem().getCount())
                    .append(cartItemModel.getItem().getCountUnit())
                    .append(")")
                    .append(", ");
        }
        sms.deleteCharAt(sms.length() - 2);
        return sms.toString();
    }

    @Override
    public void onCheckBoxClicked(CartItem itemModel, boolean isBuyed) {
        itemModel.setBuyed(isBuyed);
        if (isBuyed){
            mCartCheckedItemList.add(itemModel);
        }else{
            mCartCheckedItemList.remove(itemModel);
        }
        updatePrice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSectionAdapter != null) {
            updateSectionAdapter();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtilities.saveCartList(mContext, mCartItemList);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add_item) {
            ArrayList<String> categories = AppUtilities.getCategories(mContext);
            Bundle args = new Bundle();
            args.putStringArrayList(AppConstants.KEY_ITEM_CATEGORIES, categories);
            args.putInt(AppConstants.KEY_ITEM_CATEGORY_POSITION, 1);
            AddItemFragment fragment = new AddItemFragment();
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), fragment.getTag());
        }
    }

    @Override
    public void onItemAddClick(Item item, String category) {
        mCartItemList = AppUtilities.addItemAndReturnCartList(mContext, new CartItem(item, category, false));
        AppUtilities.addItemToProductList(mContext, category, new Item(item.getItemName(), 1.0, item.getCountUnit(), item.getPrice()));
        updateAndWriteData();
    }

}
