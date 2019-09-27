

package com.etech.testproject.data;


import android.content.Context;

import com.etech.testproject.data.db.DbHelper;
import com.etech.testproject.data.model.Lecture;
import com.etech.testproject.data.network.ApiHelper;
import com.etech.testproject.data.network.model.Pagging;
import com.etech.testproject.data.prefs.PreferencesHelper;


public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    public enum ClearCatchType {
        CLEAR_ALL,
        CLEAR_RECENT_CLOSE,
        CLEAR_EMERGENCY_CONTACT
    }

    private final Context mContext;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;

    public AppDataManager(Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }
    /*
     * DATA MANAGER METHOD START
     * */





    /*
     * DATA MANAGER METHOD END
     * */


    /**
     * API METHODS START
     */


    @Override
    public void getNotificationList(Pagging<Lecture> pagging, Callback<Pagging> onApiCallback) {
        mApiHelper.getNotificationList(pagging, onApiCallback);
    }


    @Override
    public String get(String key, String defaultValue) {
        return mPreferencesHelper.get(key, defaultValue);
    }

    @Override
    public void set(String key, String value) {
        mPreferencesHelper.set(key, value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        mPreferencesHelper.setBoolean(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return mPreferencesHelper.getBoolean(key, defaultValue);
    }

    @Override
    public void setint(String key, int value) {
        mPreferencesHelper.setint(key, value);
    }

    @Override
    public int getint(String key, int defaultValue) {
        return mPreferencesHelper.getint(key, defaultValue);
    }

    @Override
    public void clear() {
        mPreferencesHelper.clear();
    }

    @Override
    public void remove(String key) {
        mPreferencesHelper.remove(key);
    }

    @Override
    public void setAppPref(String key, String value) {
        mPreferencesHelper.setAppPref(key, value);
    }

    @Override
    public String getAppPref(String key, String defaultValue) {
        return mPreferencesHelper.getAppPref(key, defaultValue);
    }

    @Override
    public void setBooleanAppPref(String key, boolean value) {
        mPreferencesHelper.setBooleanAppPref(key, value);
    }

    @Override
    public boolean getBooleanAppPref(String key, boolean defaultValue) {
        return mPreferencesHelper.getBooleanAppPref(key, defaultValue);
    }

    @Override
    public void setLong(String key, long value) {
        mPreferencesHelper.setLong(key, value);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return mPreferencesHelper.getLong(key, defaultValue);
    }

    /*=============PREFERENCE METHODS END===============*/

}
