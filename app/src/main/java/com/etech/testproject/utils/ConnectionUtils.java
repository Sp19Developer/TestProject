package com.etech.testproject.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TimePicker;

import com.etech.testproject.app.TestApp;

import java.util.ArrayList;
import java.util.Calendar;


public class ConnectionUtils {
    public static final String TAG = "ConnectionUtils";

    public static boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) TestApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
