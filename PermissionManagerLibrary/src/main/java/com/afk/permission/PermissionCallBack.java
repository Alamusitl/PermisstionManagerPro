package com.afk.permission;

/**
 * CallBack of check and request System permissions
 * Created by Alamusi on 2016/10/24.
 */

public interface PermissionCallBack {
    void onPermissionRequestResult(Permission permission);
}
