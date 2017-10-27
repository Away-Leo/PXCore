package com.pingxundata.pxcore.utils;


import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.pingxun.library.commondialog.CommomDialog;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.metadata.entity.Wall;
import com.pingxundata.pxcore.metadata.interfaces.IFunction;
import com.pingxundata.pxcore.views.DragFloatActionButton;

/**
 * @author Away
 * @version V1.0
 * @Title: IntegralWallManager.java
 * @Description: 积分墙管理器
 * @date 2017/10/27 9:49
 * @copyright 重庆平讯数据
 */
public class WallManager {

    private static Activity mContext;

    private static Wall wall;

    public static Wall with(Activity context, DragFloatActionButton button, String appName){
        if(ObjectHelper.isNotEmpty(context)){
            mContext=context;
            if(ObjectHelper.isEmpty(wall)){
                wall=new Wall(context,button,appName);
            }
        }
        return wall;
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean success = grantResults.length > 0;
        if(requestCode==701){
            if(success){
                for(int i=0;i<grantResults.length;i++){
                    String perName=permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        int MODE = MIUIUtil.checkAppops(mContext, AppOpsManager.OPSTR_READ_PHONE_STATE);
                        if (MODE == MIUIUtil.MODE_ASK) {
                            //TODO 系统权限设置为询问
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                mContext.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 701);
                            }
                        } else if (MODE == MIUIUtil.MODE_IGNORED) {
                            new CommomDialog(mContext, R.style.dialog, "请开启获取手机信息权限，以便提高服务质量", (dialog, confirm) -> {
                                MIUIUtil.jumpToPermissionsEditorActivity(mContext);
                                dialog.dismiss();
                            }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                        } else {
                            if(perName.equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)){
                                wall.doWallClick();
                            }
                        }
                    }else{
                        new CommomDialog(mContext, R.style.dialog, "请开启获取手机信息权限，以便提高服务质量", (dialog, confirm) -> {
                            MIUIUtil.jumpToPermissionsEditorActivity(mContext);
                            dialog.dismiss();
                        }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                    }
                }
            }
        }
    }

}
