package com.bulunduc.shoppingcart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.filters.NumberInputFilter;
import com.bulunduc.shoppingcart.listeners.CartItemIsBuyedCheckListener;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


public class CategorySectionAdapter extends StatelessSection {
    String categoryTitle;
    ArrayList<CartItem> cartList;
    Context appContext;
    private CartItemIsBuyedCheckListener mItemIsBuyedCheckListener;

    public CategorySectionAdapter(Context context, String title, ArrayList<CartItem> list, CartItemIsBuyedCheckListener listener) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.recyclerview_item_cart)
                .headerResourceId(R.layout.recyclerview_category_title_cart)
                .build());

        this.appContext = context;
        this.categoryTitle = title;
        this.cartList = list;
        this.mItemIsBuyedCheckListener = listener;
    }

    @Override
    public int getContentItemsTotal() {
        return cartList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        view.setOnClickListener(new CardTapListener());
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.expandView.setVisibility(View.GONE);
        final CartItem item = cartList.get(position);
        itemViewHolder.itemName.setText(String.format(appContext.getString(R.string.cart_product_title), item.getItem().getItemName(), item.getItem().getCount().toString(), item.getItem().getCountUnit()));
        itemViewHolder.itemPrice.setText(String.valueOf(item.getItem().getFinalPrice()));
        itemViewHolder.isItemChecked.setChecked(item.isBuyed());
        itemViewHolder.itemContainer.setBackgroundColor(appContext.getResources().getColor(item.isBuyed() ? R.color.lightGray : R.color.backgroundColor));
        itemViewHolder.isItemChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setBuyed(isChecked);
            itemViewHolder.itemContainer.setBackgroundColor(appContext.getResources().getColor(isChecked ? R.color.lightGray : R.color.backgroundColor));
            mItemIsBuyedCheckListener.onCheckBoxClicked(item, isChecked);
        });

        itemViewHolder.etCount.setText(String.valueOf(item.getItem().getCount()));
        itemViewHolder.etCount.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});

        final ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(appContext, R.layout.spinner_item, appContext.getResources().getStringArray(R.array.units));
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        itemViewHolder.spUnit.setAdapter(spinnerAdapter);
        itemViewHolder.spUnit.setSelection(spinnerAdapter.getPosition(item.getItem().getCountUnit()));
        itemViewHolder.etPrice.setText(String.valueOf(item.getItem().getPrice()));
        itemViewHolder.etPrice.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});

        itemViewHolder.btnSaveCartItem.setOnClickListener(v -> {
            try {
                Double count = Double.parseDouble(itemViewHolder.etCount.getText().toString());
                Double price = Double.parseDouble(itemViewHolder.etPrice.getText().toString());

                item.getItem().setCount(count);
                item.getItem().setCountUnit(itemViewHolder.spUnit.getSelectedItem().toString());
                item.getItem().setPrice(price);
                itemViewHolder.itemName.setText(String.format(appContext.getString(R.string.cart_product_title), item.getItem().getItemName(), item.getItem().getCount().toString(), item.getItem().getCountUnit()));
                itemViewHolder.itemPrice.setText(String.valueOf(item.getItem().getFinalPrice()));

            } catch (NumberFormatException e) {
                AppUtilities.showToast(appContext, appContext.getString(R.string.check_fields));
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.tvTitle.setText(categoryTitle);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        HeaderViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.categoryTitle);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView itemPrice;
        private final CheckBox isItemChecked;
        private final RelativeLayout itemContainer;
        private final RelativeLayout expandView;
        private final EditText etCount;
        private final Spinner spUnit;
        private final EditText etPrice;
        private final ImageButton btnSaveCartItem;

        ItemViewHolder(View view) {
            super(view);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            isItemChecked = itemView.findViewById(R.id.cartItemIsChecked);
            itemContainer = itemView.findViewById(R.id.cartContainer);
            expandView = itemView.findViewById(R.id.expand_container);
            etCount = itemView.findViewById(R.id.etCount);
            spUnit = itemView.findViewById(R.id.spUnit);
            etPrice = itemView.findViewById(R.id.etPrice);
            btnSaveCartItem = itemView.findViewById(R.id.btnSaveCartItem);
        }
    }

    private class CardTapListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            View someView = v.findViewById(R.id.expand_container);
            if (someView.getVisibility() == View.GONE) {
                someView.setVisibility(View.VISIBLE);
            } else if (someView.getVisibility() == View.VISIBLE) {
                someView.setVisibility(View.GONE);
            }

        }
    }
}
