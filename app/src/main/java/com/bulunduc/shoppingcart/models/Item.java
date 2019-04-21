package com.bulunduc.shoppingcart.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bulunduc.shoppingcart.constants.AppConstants;

public class Item implements Parcelable {
    private static final String TAG = "Item";
    private String item;
    private Double mCount;
    private String countUnit;
    private Double price;

    public Item(String item, Double mCount, String countUnit, Double price) {
        this.item = item;
        this.mCount = mCount;
        this.countUnit = countUnit;
        this.price = price;
    }

    public String getItemName() {
        return item;
    }

    public Double getCount() {
        return mCount;
    }

    public int getStepCount() {
        return AppConstants.WEIGHT_MIN_KG_L_PIECE_STEP;
    }

    public String getCountUnit() {
        return countUnit;
    }


    public static String getStringFormatCount(double count, String unit) {
        if (!unit.equals("шт"))
            return String.valueOf(count);
        else
            return (String.valueOf((int) count));
    }


    public double getPrice() {
        return Math.round(price * 100.0) / 100.0;
    }

    public double getFinalPrice() {
        return Math.round(mCount * price * 100.0) / 100.0;
    }

    public void addCount(Double count, String unit) {
        this.mCount += count;
    }

    public static Creator<Item> getCREATOR() {
        return CREATOR;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setCount(Double minCount) {
        this.mCount = minCount;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }

    public void setPrice(Double price) {
        this.price = Math.round(price * 100.0) / 100.0;
    }


    protected Item(Parcel in) {
        item = in.readString();
        mCount = in.readDouble();
        countUnit = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item);
        dest.writeDouble(mCount);
        dest.writeString(countUnit);
        dest.writeDouble(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (this.item != null ? !this.item.equals(item.item) : item.item != null) return false;
        return getCountUnit() != null ? getCountUnit().equals(item.getCountUnit()) : item.getCountUnit() == null;
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (getCountUnit() != null ? getCountUnit().hashCode() : 0);
        return result;
    }
}
