package com.pingxundata.library.demo.pxcoredemo;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pingxun.library.commondialog.CommomDialog;
import com.pingxun.library.guomeilibrary.meijie.MIUIUtil;
import com.pingxundata.pxcore.metadata.interfaces.IFunction;
import com.pingxundata.pxcore.utils.GDlocationUtil;
import com.pingxundata.pxcore.utils.IntegralWallManager;
import com.pingxundata.pxcore.utils.ToastUtils;
import com.pingxundata.pxcore.utils.WallManager;
import com.pingxundata.pxcore.utils.WechatBanner;
import com.pingxundata.pxcore.views.DragFloatActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button testButton = (Button) findViewById(R.id.testButton);

//        UpdateChecker.checkForDialog(this,"xiaomi-DKQB","https://119.23.64.92/front/product/findProductVersion.json");
//        final String urrrl="http://m.doudouhua.cn/register/?agent=DDHAPP&channel=miaobidai";
//        final String urrrl="http://phone.daishuqianbao.com/h5/invite.jsp?invitationCode=null&channelCode=mbd";
        final String urrrl="http://phone.daishuqianbao.com/h5/invite.jsp?channelCode=mbd";
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DemoWebActivity.class);
                intent.putExtra("url",urrrl);
                startActivity(intent);

//                ContactUsPopupView contactUsPopupView = new ContactUsPopupView(MainActivity.this);
//                contactUsPopupView.setPopupWindowFullScreen(true);
//                contactUsPopupView.showPopupWindow();
                //http://192.168.1.100:8099/pxbanner.jpg   http://192.168.1.100:8099/pxwechat.jpg

            }
        });
//        WechatBanner.with(this, "http://192.168.1.100:8099/pxbanner.jpg").pop("http://192.168.1.100:8099/pxwechat.jpg");
//        FloatViewServiceManager.showFloatView(this);
//        FloatViewServiceManager.setOnClickCallBack(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"YYYYYYYY",Toast.LENGTH_SHORT).show();
//            }
//        });
//        getCellLac();
        WallManager.with(this,(DragFloatActionButton)findViewById(R.id.wallFloatingButton),"APP",1).doWall();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WallManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    public void  getCellLac() {
            if (Build.VERSION.SDK_INT >=23) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        new CommomDialog(this, R.style.dialog, "请开启定位权限，以便能及时获取您的位置", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean b) {
                                if (b) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                                    }
                                    dialog.dismiss();
                                }
                            }
                        }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    }
                    return;
                }
                //针对小米手机的权限检查
                int MODE = MIUIUtil.checkAppops(this, AppOpsManager.OPSTR_COARSE_LOCATION);
                if (MODE == MIUIUtil.MODE_ASK) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                } else if (MODE == MIUIUtil.MODE_IGNORED) {
                    new CommomDialog(this, R.style.dialog, "请在 系统设置-权限管理 中开启定位权限，以便能及时获取您的位置", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean b) {
                                if (b) {
                                    MIUIUtil.jumpToPermissionsEditorActivity(MainActivity.this);
                                    dialog.dismiss();
                                }
                        }
                    }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                }
            }
//        if (location == null) {
//            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null) {
//                this.location = location;
//            } else {
//                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//                if (location != null) {
//                    this.location = location;
//                } else {
//                    function.onCallBack("北京/北京市,39.964806/116.472753");
//                    return;
//                }
//            }
//        } else {
//            this.location = location;
//        }
//        function.onCallBack("北京/北京市," + location.getLatitude() + "/" + location.getLongitude());
        }

    @Override
    public void onClick(View view) {
//        ToastUtils.showToast(MainActivity.this,"3333333");
    }
}
