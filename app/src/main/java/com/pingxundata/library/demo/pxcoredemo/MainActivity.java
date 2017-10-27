package com.pingxundata.library.demo.pxcoredemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pingxundata.pxcore.utils.IntegralWallManager;
import com.pingxundata.pxcore.utils.WechatBanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button testButton = (Button) findViewById(R.id.testButton);

//        UpdateChecker.checkForDialog(this,"xiaomi-DKQB","https://119.23.64.92/front/product/findProductVersion.json");

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,DemoWebActivity.class);
//                intent.putExtra("url","https://help.wikihost.cn/activity/register/register.html");
//                startActivity(intent);

                ContactUsPopupView contactUsPopupView = new ContactUsPopupView(MainActivity.this);
                contactUsPopupView.setPopupWindowFullScreen(true);
                contactUsPopupView.showPopupWindow();
                //http://192.168.1.100:8099/pxbanner.jpg   http://192.168.1.100:8099/pxwechat.jpg

            }
        });
        WechatBanner.with(this, "http://192.168.1.100:8099/pxbanner.jpg").pop("http://192.168.1.100:8099/pxwechat.jpg");
//        FloatViewServiceManager.showFloatView(this);
//        FloatViewServiceManager.setOnClickCallBack(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"YYYYYYYY",Toast.LENGTH_SHORT).show();
//            }
//        });
        IntegralWallManager.integralWindow(0,this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IntegralWallManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
