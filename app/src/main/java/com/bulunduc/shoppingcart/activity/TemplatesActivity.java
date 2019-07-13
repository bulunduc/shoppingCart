package com.bulunduc.shoppingcart.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.TemplateAdapter;
import com.bulunduc.shoppingcart.fragments.AddEditTemplateFragment;
import com.bulunduc.shoppingcart.helpers.SimpleItemTouchHelperCallback;
import com.bulunduc.shoppingcart.listeners.TemplateDialogClickListener;
import com.bulunduc.shoppingcart.models.Template;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

public class TemplatesActivity extends BaseActivity implements TemplateDialogClickListener {
    private RecyclerView rvTemplates;
    private FloatingActionButton mFloatingActionButton;
    private TemplateAdapter mTemplateAdapter = null;
    private ArrayList<Template> mTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        initView();
    }

    private void initView(){
        rvTemplates = findViewById(R.id.rv_templates);
        rvTemplates.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mTemplates = AppUtilities.getTemplatesList(this.getApplicationContext());
        mTemplateAdapter = new TemplateAdapter(this, mTemplates);
        rvTemplates.setAdapter(mTemplateAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mTemplateAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvTemplates);

        mFloatingActionButton = findViewById(R.id.fab_add_item);
        mFloatingActionButton.setOnClickListener(v -> {
            AddEditTemplateFragment fragment = new AddEditTemplateFragment();
            fragment.show(this.getFragmentManager(), fragment.getTag());
        });
        initToolbar(true);
        setToolbarTitle(getString(R.string.templates));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_lists:
                    Intent intent0 = new Intent(TemplatesActivity.this, ItemCategoryActivity.class);
                    startActivity(intent0);
                    break;
                case R.id.action_templates:
                    break;
                case R.id.action_cart:
                    Intent intent2 = new Intent(TemplatesActivity.this, CartActivity.class);
                    startActivity(intent2);
                    break;
            }
            return false;
        });
    }

    @Override
    public void onTemplateAddClick(Template template, int position) {
        if (position < 0) {
            mTemplates.add(template);
        } else {
            mTemplates.set(position, template);
        }
        mTemplateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTemplateDeleteClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_template_delete_message, mTemplates.get(position).getTitle()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.yes), (dialog, whichButton) -> {
                    mTemplates.remove(position);
                    mTemplateAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(getString(R.string.no), null).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtilities.saveTemplatesList(getApplicationContext(), mTemplates);
    }
}
