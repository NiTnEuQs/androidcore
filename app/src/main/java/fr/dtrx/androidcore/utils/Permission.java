package fr.dtrx.androidcore.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {

    public static final int CAMERA_PERMISSION = 0;
    public static final int INTERNET_PERMISSION = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    public static final int UPDATE_PERMISSION = 3;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 4;

    public static boolean CAMERA(Context context) {
        return isGranted(context, Manifest.permission.CAMERA);
    }

    public static boolean INTERNET(Context context) {
        return isGranted(context, Manifest.permission.INTERNET);
    }

    public static boolean WRITE_EXTERNAL_STORAGE(Context context) {
        return isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean READ_EXTERNAL_STORAGE(Context context) {
        return isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE);
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static boolean isGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        if (!hasPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    public static boolean hasPermissions(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (!Permission.isGranted(activity, permission)) {
                return false;
            }
        }
        return true;
    }

}
