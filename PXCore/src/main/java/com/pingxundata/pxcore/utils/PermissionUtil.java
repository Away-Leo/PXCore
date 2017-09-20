package com.pingxundata.pxcore.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
* @Title: PermissionUtil.java
* @Description: 权限工具类
* @author Away
* @date 2017/9/20 16:15
* @copyright 重庆平讯数据
* @version V1.0
*/
public class PermissionUtil {

    public static void goSystemPermissonSetting(Context context){
        if(MIUIUtil.isMIUI()){
            MIUIUtil.jumpToPermissionsEditorActivity(context);
        }else{
            goAppDetailSettingIntent(context);
        }
    }

    public static void goAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

}
