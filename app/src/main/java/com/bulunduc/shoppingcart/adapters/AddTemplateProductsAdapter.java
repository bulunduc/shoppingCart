package com.bulunduc.shoppingcart.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.models.Item;

import java.text.MessageFormat;
import java.util.ArrayList;

public class AddTemplateProductsAdapter extends RecyclerView.Adapter<AddTemplateProductsAdapter.ViewHolder>{
    private static final String TAG = "AddTemplateProductsAdapter";
    private Activity mActivity;
    private ArrayList<Item> mProducts;

    public AddTemplateProductsAdapter(Activity activity, ArrayList<Item> products) {
        mActivity = activity;
        mProducts = products;
    }

    @NonNull
    @Override
    public AddTemplateProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new AddTemplateProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductTitle;
        private CheckBox cbNeedToAdd;


        ViewHolder(View itemView) {
            super(itemView);
            tvProductTitle = itemView.findViewById(R.id.product_title);
            cbNeedToAdd = itemView.findViewById(R.id.need_to_add_item_to_cart);
            cbNeedToAdd.setVisibility(View.GONE);
        }

        void bind(Item item){
            tvProductTitle.setText(MessageFormat.format("{0} ({1}{2}/{3})", item.getItemName(), item.getCount(), item.getCountUnit(), item.getCount() * item.getPrice()));
        }
    }
}
