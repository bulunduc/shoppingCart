package com.bulunduc.shoppingcart.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.activity.ItemCategoryActivity;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.filters.NumberInputFilter;
import com.bulunduc.shoppingcart.listeners.ItemRecyclerViewClickListener;
import com.bulunduc.shoppingcart.models.Item;

import java.util.ArrayList;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<Item> mItems;
    private ItemRecyclerViewClickListener mItemRecyclerViewClickListener;

    public ItemViewAdapter(Activity activity, ArrayList<Item> productList) {
        mActivity = activity;
        mItems = new ArrayList<>();
        mItems = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItemRecyclerViewClickListener(ItemRecyclerViewClickListener itemRecyclerViewClickListener) {
        this.mItemRecyclerViewClickListener = itemRecyclerViewClickListener;
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        Button lessCount;
        EditText count;
        TextView unit;
        Button moreCount;
        TextView price;
        ImageButton addToCart;
        int position = 0;


        ViewHolder(View itemView){
            super(itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemRecyclerViewClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
            title =  itemView.findViewById(R.id.item_title);
            lessCount =  itemView.findViewById(R.id.less_count);
            count =  itemView.findViewById(R.id.item_count);
            count.setFilters(new NumberInputFilter[]{new NumberInputFilter(4,2)});
            unit =  itemView.findViewById(R.id.item_unit);
            moreCount =  itemView.findViewById(R.id.more_count);
            price =  itemView.findViewById(R.id.item_price);
            addToCart =  itemView.findViewById(R.id.add_to_cart);

        }

        void bind(final Item item){
            title.setText(item.getItemName());
            String txtCount = Item.getStringFormatCount(item.getCount(), item.getCountUnit());
            count.setText(txtCount);
            count.setSelection(txtCount.length());
            price.setText(String.valueOf(item.getPrice() * item.getCount()));
            lessCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( count.getText().toString().equals("")) count.setText(String.valueOf(AppConstants.ZERO_DOUBLE_VALUE));
                    Double newCount = Double.parseDouble(count.getText().toString()) - item.getStepCount();
                    if (newCount < AppConstants.ZERO_DOUBLE_VALUE) newCount = AppConstants.ZERO_DOUBLE_VALUE;
                    count.setText(Item.getStringFormatCount(newCount, item.getCountUnit()));
                }
            });
            count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int tcCount) {
                    try {
                        item.setCount(Double.parseDouble(s.toString()));
                        price.setText(String.valueOf(item.getFinalPrice()));
                    }catch (NumberFormatException e) {
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            unit.setText(item.getCountUnit());
            moreCount.setOnClickListener(null);
            moreCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( count.getText().toString().isEmpty()) count.setText(String.valueOf(AppConstants.ZERO_DOUBLE_VALUE));
                    Double newWeight = Double.parseDouble(count.getText().toString()) + item.getStepCount();
                    count.setText(Item.getStringFormatCount(newWeight, item.getCountUnit()));
                    }
            });
            addToCart.setOnClickListener(null);
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ItemCategoryActivity)mActivity).addToCart(new Item(item.getItemName(), Double.parseDouble(count.getText().toString()), item.getCountUnit(), item.getPrice())) ;
                }
            });
        }
    }

}
