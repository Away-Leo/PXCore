package com.pingxundata.library.demo.pxcoredemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pingxundata.pxcore.autoupdate.UpdateChecker;
import com.pingxundata.pxcore.utils.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button testButton=(Button)findViewById(R.id.testButton);

//        UpdateChecker.checkForDialog(this,"xiaomi-DKQB","https://119.23.64.92/front/product/findProductVersion.json");

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DemoWebActivity.class);
                intent.putExtra("url","https://help.wikihost.cn/activity/register/register.html");
//                intent.putExtra("url","http://api.cashtaxi.cn/download/index.html");
                startActivity(intent);
//                PermissionUtil.goSystemPermissonSetting(MainActivity.this);
            }
        });
    }
}
