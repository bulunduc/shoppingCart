package com.bulunduc.shoppingcart.utilities;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtilities.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf");
    }
}
