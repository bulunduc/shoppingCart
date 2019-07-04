package com.bulunduc.shoppingcart.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.TemplateAdapter;
import com.bulunduc.shoppingcart.fragments.AddEditTemplateFragment;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Template;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

public class TemplatesActivity extends BaseActivity {
    private RecyclerView rvTemplates;
    private FloatingActionButton mFloatingActionButton;
    private TemplateAdapter adapter = null;
    private ArrayList<Template> templates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        initView();
    }

    private void initView(){
        rvTemplates = findViewById(R.id.rvTemplates);
        rvTemplates.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        templates = AppUtilities.getTemplatesList(this.getApplicationContext());
        adapter = new TemplateAdapter(this.getApplicationContext(), this, templates);
        rvTemplates.setAdapter(adapter);
        mFloatingActionButton = findViewById(R.id.addFab);
        mFloatingActionButton.setOnClickListener(v -> {
            AddEditTemplateFragment showProducts = AddEditTemplateFragment.newInstance(this.getApplicationContext());
            showProducts.show(this.getFragmentManager(), showProducts.getTag());
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
}
