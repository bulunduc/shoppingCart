package com.bulunduc.shoppingcart.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.models.Template;

import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
    private static final String TAG = "TemplateAdapter";
    private Activity mActivity;
    private Context mContext;
    private ArrayList<Template> mTemplates = new ArrayList<>();

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
    }

    @Override
    public int getItemCount() {
        return mTemplates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTemplateImage;
        private TextView tvTemplateTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            ivTemplateImage = itemView.findViewById(R.id.ivTemplateImage);
            tvTemplateTitle = itemView.findViewById(R.id.tvTemplateTitle);
        }

        void bind(Template template){
            ivTemplateImage.setImageResource(template.getImageId());
            tvTemplateTitle.setText(template.getTitle());

        }
    }


}
