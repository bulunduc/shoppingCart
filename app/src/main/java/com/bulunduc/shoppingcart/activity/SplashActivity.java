package com.bulunduc.shoppingcart.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bulunduc.shoppingcart.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;
    private ImageView imageView;
    private Animation animation;
    private ProgressBar progressBar;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar);
        layout = findViewById(R.id.splash_layout);
        imageView = findViewById(R.id.iv_splash_icon);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);

    }

    private void initFunctionality(){
        layout.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            imageView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = new Intent(SplashActivity.this.getApplicationContext(), ItemCategoryActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }, SPLASH_DURATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }
}
