package com.bulunduc.shoppingcart.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bulunduc.shoppingcart.R;

public class TemplatesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        initView();
    }

    private void initView(){
        initToolbar(true);
        setToolbarTitle(getString(R.string.templates));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
