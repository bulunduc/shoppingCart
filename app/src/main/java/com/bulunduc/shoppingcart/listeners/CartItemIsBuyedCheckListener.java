package com.bulunduc.shoppingcart.listeners;

import com.bulunduc.shoppingcart.models.CartItem;

public interface CartItemIsBuyedCheckListener {
    void onCheckBoxClicked(CartItem item, boolean isChecked);
}
