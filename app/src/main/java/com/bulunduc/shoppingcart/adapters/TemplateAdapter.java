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
import com.bulunduc.shoppingcart.models.Template;
import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
    private static final String TAG = "TemplateAdapter";
    private Activity mActivity;
    private Context mContext;
    private ArrayList<Template> mTemplates;

    public TemplateAdapter(Context applicationContext, Activity activity, ArrayList<Template> templates) {
        mActivity = activity;
        mContext = applicationContext;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTemplateTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTemplateTitle = itemView.findViewById(R.id.tvTemplateTitle);
        }

        void bind(Template template){
            tvTemplateTitle.setText(template.getTitle());

        }
    }


}
