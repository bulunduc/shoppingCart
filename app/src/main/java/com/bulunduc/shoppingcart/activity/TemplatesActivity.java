package com.bulunduc.shoppingcart.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bulunduc.shoppingcart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplatesActivity extends BaseActivity {
    @BindView(R.id.bottom_navigation) protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        initView();
    }

    private void initView(){
        initToolbar(true);
        ButterKnife.bind(this);
        setToolbarTitle(getString(R.string.templates));
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }
}
