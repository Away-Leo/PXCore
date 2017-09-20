package com.pingxundata.pxcore.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 用于小米手机权限检查的工具类
 * Created by hbl on 2017/7/13.
 */

public class MIUIUtil {
    public static final int MODE_NOTMIUI=-1;
    public static final int MODE_ALLOWED=0;
    public static final int MODE_IGNORED=1;
    public static final int MODE_ASK=4;
    /**
     * 查看原生态的权限是否有授权
     *
     * @param context
     * @param op      如定位权限AppOpsManager.OPSTR_FINE_LOCATION
     *                小米独特的返回值：
     *                0 允许
     *                1 拒绝
     *                4 询问
     * @return
     */
    public static int checkAppops(Context context, String op) {
        if (isMIUI()) { // 只有小米手机才检测
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(op, Binder.getCallingUid(), context.getPackageName());
                return checkOp;
            }
        }
        return MODE_NOTMIUI;
    }

    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    // 注意这个状态最好用SharePreference保存起来，需要每次读取检测
    public static boolean isMIUI() {
        //获取缓存状态
//        String miui = SPUtil.getStringForDefault(SPConstant.IS_MIUI);
//        if (miui != null) {
//            if ("1".equals(miui))
//                return true;
//            else if ("2".equals(miui))
//                return false;
//        }
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
//        SPUtil.putStringForDefault(SPConstant.IS_MIUI, isMIUI ? "1" : "2");
        return isMIUI;
    }

    /**
     * 跳转到MIUI应用权限设置页面
     *
     * @param context context
     */
    public static void jumpToPermissionsEditorActivity(Context context) {
        if (isMIUI()) {
            try {
                // MIUI 8
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            } catch (Exception e) {
                try {
                    // MIUI 5/6/7
                    Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    localIntent.putExtra("extra_pkgname", context.getPackageName());
                    context.startActivity(localIntent);
                } catch (Exception e1) {
                    // 否则跳转到应用详情
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            }
        }
    }

}
