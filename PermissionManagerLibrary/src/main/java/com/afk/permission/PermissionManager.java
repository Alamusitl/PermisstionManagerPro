package com.afk.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to request and check System permissions for apps targeting Android M
 * Created by Alamusi on 2016/10/24.
 */

public class PermissionManager {

    static final String KEY_PERMISSION = "permissions";
    private static final String TAG = "PermissionManager";
    private static PermissionManager mInstance = null;
    private List<String> mNeedRequestPermissions;
    private List<String> mRequestingPermissions;
    private PermissionCallBack mCallBack;

    public PermissionManager() {
        mNeedRequestPermissions = new ArrayList<>();
        mRequestingPermissions = new ArrayList<>();
    }

    public static PermissionManager getInstance() {
        if (mInstance == null) {
            synchronized (PermissionManager.class) {
                if (mInstance == null) {
                    mInstance = new PermissionManager();
                }
            }
        }
        return mInstance;
    }

    public void requestPermission(Context context, PermissionCallBack callBack, String... permissions) {
        mCallBack = callBack;
        if (hasPermissions(context, permissions)) {
            return;
        }
        ArrayList<String> addNewRequestPermissions = new ArrayList<>();
        for (String permission : mNeedRequestPermissions) {
            if (!mRequestingPermissions.contains(permission)) {
                addNewRequestPermissions.add(permission);
                mRequestingPermissions.add(permission);
            }
        }
        mNeedRequestPermissions.clear();
        if (addNewRequestPermissions.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, RequestPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_PERMISSION, addNewRequestPermissions.toArray(new String[addNewRequestPermissions.size()]));
        context.startActivity(intent);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!hasMethod("checkSelfPermission")) {
            return false;
        }
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (!mNeedRequestPermissions.contains(permission)) {
                    mNeedRequestPermissions.add(permission);
                }
            }
        }
        return mNeedRequestPermissions.isEmpty();
    }

    boolean hasMethod(String methodName) {
        try {
            Class clz = Class.forName("android.support.v4.app.ActivityCompat");
            if (clz == null) {
                return false;
            }
            Method[] methods = clz.getMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "class ActivityCompat not exist, please check!");
        }
        return false;
    }

    void onRequestPermissionResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0; i < permissions.length; i++) {
            mRequestingPermissions.remove(permissions[i]);
            if (mCallBack != null) {
                boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                mCallBack.onPermissionRequestResult(new Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]));
            }
        }
    }
}
