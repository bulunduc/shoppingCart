package com.bulunduc.shoppingcart.listeners;

import android.view.View;

public interface ItemRecyclerViewClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
    void onItemDoubleClick(View view, int position);
}
