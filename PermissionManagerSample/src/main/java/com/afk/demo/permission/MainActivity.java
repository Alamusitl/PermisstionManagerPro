package com.afk.demo.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afk.permission.Permission;
import com.afk.permission.PermissionCallBack;
import com.afk.permission.PermissionManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.id_btn_storage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.getInstance().requestPermission(MainActivity.this, new PermissionCallBack() {
                    @Override
                    public void onPermissionRequestResult(Permission permission) {
                        doSomeThing(permission);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });
    }

    private void doSomeThing(Permission permission) {
        Log.i("MainActivity", "doSomeThing: " + permission.permissionName + " " + permission.isGranted);
        if (permission.permissionName.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permission.permissionName.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (permission.isGranted) {
                Toast.makeText(MainActivity.this, permission.permissionName + " 已授权", Toast.LENGTH_SHORT).show();
            } else if (permission.shouldShowRequestPermissionRationale) {
                Toast.makeText(MainActivity.this, permission.permissionName + " 被拒绝，可以再次请求权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, permission.permissionName + " 被拒绝，需要到应用权限管理打开权限", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
            }
        }
    }
}
