package com.etech.testproject.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.etech.testproject.data.AppDataManager;
import com.etech.testproject.data.DataManager;
import com.etech.testproject.data.db.AppDbHelper;
import com.etech.testproject.data.network.AppApiHelper;
import com.etech.testproject.data.prefs.AppPreferencesHelper;


/**
 * Created by etech3 on 21/11/17.
 */

public class TestApp extends MultiDexApplication {
    public static Context appContext;
    private static DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        dataManager = new AppDataManager(this, new AppDbHelper(), new AppPreferencesHelper(this), new AppApiHelper());

    }

    public static DataManager getDataManager() {
        return dataManager;
    }


}
