package com.bulunduc.shoppingcart.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bulunduc.shoppingcart.constants.AppConstants;

public class AppPreference {
    private static Context context;
    private static AppPreference appPreference;
    private SharedPreferences sharedPreferences, settingsPreferences;
    private SharedPreferences.Editor editor;

    public static AppPreference getInstance(Context appContext) {
        if (appPreference == null) {
            context = appContext;
            appPreference = new AppPreference();
        }
        return appPreference;
    }

    private AppPreference(){
        sharedPreferences = context.getSharedPreferences(AppConstants.APP_PREF_NAME, Context.MODE_PRIVATE);
        settingsPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void setBoolean(String key, boolean defaultValue) {
        editor.putBoolean(key, defaultValue);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }


}
