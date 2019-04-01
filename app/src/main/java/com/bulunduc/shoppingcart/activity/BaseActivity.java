package com.bulunduc.shoppingcart.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private Context context;
    private Activity activity;
    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BaseActivity.this;
        context = activity.getApplicationContext();
    }

    public void initToolbar(boolean isTitleEnabled) {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(isTitleEnabled);
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
