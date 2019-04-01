package com.bulunduc.shoppingcart.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private Item mItem;
    private String mCategory;
    private boolean mIsBuyed;

    public CartItem(Item item, String category, boolean isBuyed) {
        this.mItem = item;
        this.mCategory = category;
        this.mIsBuyed = isBuyed;
    }

    protected CartItem(Parcel in) {
        mItem = in.readParcelable(Item.class.getClassLoader());
        mCategory = in.readString();
        mIsBuyed = in.readByte() != 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        this.mItem = item;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public boolean isBuyed() {
        return mIsBuyed;
    }

    public void setBuyed(boolean buyed) {
        mIsBuyed = buyed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mItem, flags);
        dest.writeString(mCategory);
        dest.writeByte((byte) (mIsBuyed ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        if (!getItem().getItemName().equals(cartItem.getItem().getItemName())) return false;
        if (!getItem().getCountUnit().equals(cartItem.getItem().getCountUnit())) return false;
        return getCategory().equals(cartItem.getCategory());
    }

    @Override
    public int hashCode() {
        int result = getItem().getItemName().hashCode() + getItem().getCountUnit().hashCode();
        result = 31 * result + getCategory().hashCode();
        return result;
    }
}
