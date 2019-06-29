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

import java.util.ArrayList;

public class TemplateProductsAdapter extends RecyclerView.Adapter<TemplateProductsAdapter.ViewHolder>{
    private static final String TAG = "TemplateAdapter";
    private Activity mActivity;
    private ArrayList<Item> mProducts = new ArrayList<>();
    private ArrayList<Item> mProductsForCart = new ArrayList<>();

    public TemplateProductsAdapter(Activity activity, ArrayList<Item> products) {
        mActivity = activity;
        mProducts = products;
        mProductsForCart.addAll(mProducts);
    }

    @NonNull
    @Override
    public TemplateProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new TemplateProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public ArrayList<Item> getCheckedProductList(){
        return mProductsForCart;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductTitle;
        private CheckBox cbNeedToAdd;


        public ViewHolder(View itemView) {
            super(itemView);
            tvProductTitle = itemView.findViewById(R.id.productTitle);
            cbNeedToAdd = itemView.findViewById(R.id.needToAdd);
        }

        void bind(Item item){
            tvProductTitle.setText(item.getItemName() + " (" + item.getCount() + item.getCountUnit() + "/" + item.getCount()*item.getPrice() + ")");
            cbNeedToAdd.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    mProductsForCart.add(item);
                } else {
                    mProductsForCart.remove(item);
                }
            });
        }
    }
}
