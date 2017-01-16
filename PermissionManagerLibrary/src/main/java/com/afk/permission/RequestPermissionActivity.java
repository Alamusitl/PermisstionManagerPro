package com.afk.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

@TargetApi(Build.VERSION_CODES.M)
public class RequestPermissionActivity extends Activity {

    private static final String KEY_ORIGINAL_PID = "key_original_pid";
    private int mOriginalProcessId;
    private int mRequestCode = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mOriginalProcessId = Process.myPid();
        } else {
            mOriginalProcessId = savedInstanceState.getInt(KEY_ORIGINAL_PID, mOriginalProcessId);
            if (mOriginalProcessId != Process.myPid()) {
                finish();
            }
        }
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ORIGINAL_PID, mOriginalProcessId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            if (!PermissionManager.getInstance().hasMethod("shouldShowRequestPermissionRationale")) {
                finish();
                return;
            }
            boolean[] mShouldShowRequestPermissionRationale = new boolean[permissions.length];
            for (int i = 0; i < permissions.length; i++) {
                mShouldShowRequestPermissionRationale[i] = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            }
            PermissionManager.getInstance().onRequestPermissionResult(permissions, grantResults, mShouldShowRequestPermissionRationale);
        }
        finish();
    }

    private void handleIntent(Intent intent) {
        if (!PermissionManager.getInstance().hasMethod("requestPermissions")) {
            finish();
            return;
        }
        String[] permissions = intent.getStringArrayExtra(PermissionManager.KEY_PERMISSION);
        ActivityCompat.requestPermissions(this, permissions, mRequestCode);
    }
}
