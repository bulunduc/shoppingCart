package com.bulunduc.shoppingcart.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bulunduc.shoppingcart.constants.AppConstants;

public class AppPreference {
    private static Context mContext;
    private static AppPreference mAppPreference = null;
    private SharedPreferences mSharedPreferences,mSettingsPreferences;
    private SharedPreferences.Editor mEditor;

    public static AppPreference getInstance(Context context){
        if (mAppPreference == null) {
            mContext = context;
            mAppPreference = new AppPreference();
        }
        return mAppPreference;
    }

    private AppPreference(){
        mSharedPreferences = mContext.getSharedPreferences(AppConstants.APP_PREF_NAME, Context.MODE_PRIVATE);
        mSettingsPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
    }

    public String getString(String key){

        return mSharedPreferences.getString(key, null);
    }

    public void setBoolean(String key, boolean defaultValue) {
        mEditor.putBoolean(key, defaultValue);
        mEditor.apply();
    }


    public void setInt(String key, int defaultValue) {
        mEditor.putInt(key, defaultValue);
        mEditor.apply();
    }

    public void setLong(String key, long defaultValue){
        mEditor.putLong(key, defaultValue);
        mEditor.apply();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
        }

    public int getInt(String key){
        return mSharedPreferences.getInt(key, 0);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }
}
