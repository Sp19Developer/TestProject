package com.etech.testproject.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.etech.testproject.app.TestApp;


public class PermissionUtils {
    public static final String TAG = "PermissionUtils";

    public static String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    /**
     * True if all permission are granted.
     *
     * @param context
     * @param permission
     * @param requestCode
     * @return
     */
    public static boolean AskAndCheckPermission(Activity context, String permission[], int requestCode) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkFirstTimePermission(permission)) {
                context.requestPermissions(permission, requestCode);
                for (String s : permission) {
                    TestApp.getDataManager().setBooleanAppPref(s, true);
                }
            } else {
                if (isPermissionGranted(context, permission))
                    return true;
                for (String per : permission) {
                    if (context.checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED && !context.shouldShowRequestPermissionRationale(per)) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }

                if (flag) {
                    flag = false;
//                    CustomAlertDialog.showPermissionAlert(context, context.getResources().getString(R.string.msg_alert_you_have_permission));
                } else {
                    context.requestPermissions(permission, requestCode);
                }
            }
        } else {
            return true;
        }
        return flag;
    }

    private static boolean checkFirstTimePermission(String permission[]) {
        for (String s : permission) {
            if (!TestApp.getDataManager().getBooleanAppPref(s, false)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPermissionGranted(Activity context, String permission[]) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String per : permission) {
                if (context.checkSelfPermission(per) == PackageManager.PERMISSION_GRANTED) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        } else {
            flag = true;
        }
        return flag;
    }


    public static boolean hasSelfPermission(Activity activity, String[] permissions) {

        // Verify that all the permissions.
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
