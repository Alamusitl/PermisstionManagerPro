package com.afk.permission;

/**
 * Permission Bean
 * Created by Alamusi on 2016/10/24.
 */

public class Permission {

    public final String permissionName;
    public final boolean isGranted;
    public final boolean shouldShowRequestPermissionRationale;

    Permission(String name, boolean isGranted) {
        this(name, isGranted, false);
    }

    Permission(String name, boolean isGranted, boolean shouldShowRequestPermissionRationale) {
        this.permissionName = name;
        this.isGranted = isGranted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    public int hashCode() {
        int result = permissionName.hashCode();
        result = 31 * result + (isGranted ? 1 : 0);
        result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Permission that = (Permission) obj;
        return isGranted == that.isGranted
                && shouldShowRequestPermissionRationale == that.shouldShowRequestPermissionRationale
                && permissionName.equals(that.permissionName);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionName='" + permissionName + '\'' +
                ", isGranted=" + isGranted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}';
    }
}
