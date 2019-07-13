package com.bulunduc.shoppingcart.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.TemplateAdapter;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.helpers.ItemTouchHelperAdapter;
import com.bulunduc.shoppingcart.helpers.ItemTouchHelperViewHolder;
import com.bulunduc.shoppingcart.helpers.SimpleItemTouchHelperCallback;
import com.bulunduc.shoppingcart.models.Template;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class EditCategoriesActiviry extends BaseActivity {
    private static final String TAG = "EditCategoriesActiviry";
    private RecyclerView rvCategories;
    private CategoriesAdapter mCategoriesAdapter = null;
    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categories_activiry);
        initToolbar(true);
        setToolbarTitle("Редактировать категории");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rvCategories = findViewById(R.id.rv_categories);
        mList.addAll(getIntent().getStringArrayListExtra(AppConstants.CATEGORIES));
        mCategoriesAdapter = new CategoriesAdapter(mList);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(mCategoriesAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mCategoriesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvCategories);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.action_save:
                Intent intent = new Intent();
                intent.putExtra(AppConstants.CATEGORIES, mList);
                setResult(RESULT_OK, intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> implements ItemTouchHelperAdapter {
        private ArrayList<String> mCategories;
        CategoriesAdapter(ArrayList<String> categories){
            mCategories = categories;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template, parent, false);
            return new CategoriesAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(mCategories.get(position));

        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mCategories, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mCategories, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;        }

        @Override
        public void onItemDismiss(int position) {
            new AlertDialog.Builder(EditCategoriesActiviry.this)
                    .setTitle("Подтверждение удаления")
                    .setMessage("Вы действительно хотите удалить категорию: " + mList.get(position))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.yes), (dialog, whichButton) -> {
                        mList.remove(position);
                        //notifyItemRemoved(position);
                    })
                    .setNegativeButton(getString(R.string.no), null).show();
            //notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
            private TextView tvCategory;

            ViewHolder(View itemView) {
                super(itemView);
                tvCategory = itemView.findViewById(R.id.tv_template_title);
            }

            void bind(String category){
                tvCategory.setText(category);
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
}
