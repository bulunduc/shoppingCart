package com.bulunduc.shoppingcart.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.fragments.ShowTemplateProductsFragment;
import com.bulunduc.shoppingcart.helpers.ItemTouchHelperAdapter;
import com.bulunduc.shoppingcart.helpers.ItemTouchHelperViewHolder;
import com.bulunduc.shoppingcart.models.Template;
import java.util.ArrayList;
import java.util.Collections;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder>  implements ItemTouchHelperAdapter {
    private static final String TAG = "TemplateAdapter";
    private Activity mActivity;
    private ArrayList<Template> mTemplates;

    public TemplateAdapter(Activity activity, ArrayList<Template> templates) {
        mActivity = activity;
        mTemplates = templates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mTemplates.get(position));
        holder.tvTemplateTitle.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(AppConstants.KEY_TEMPLATE_TITLE,  mTemplates.get(position).getTitle());
            args.putParcelableArrayList(AppConstants.KEY_TEMPLATE_PRODUCT_LIST,  mTemplates.get(position).getItems());
            args.putInt(AppConstants.KEY_POSITION, position);
            ShowTemplateProductsFragment fragment = new ShowTemplateProductsFragment();
            fragment.setArguments(args);
            fragment.show(mActivity.getFragmentManager(), fragment.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return mTemplates.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTemplates, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTemplates, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private TextView tvTemplateTitle;

        ViewHolder(View itemView) {
            super(itemView);
            tvTemplateTitle = itemView.findViewById(R.id.tv_template_title);
        }

        void bind(Template template){
            tvTemplateTitle.setText(template.getTitle());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundResource(R.color.lightGrayAccent);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundResource(R.drawable.bg_activity);
        }
    }


}
