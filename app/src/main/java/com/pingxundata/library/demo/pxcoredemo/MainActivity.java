package com.pingxundata.library.demo.pxcoredemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button testButton=(Button)findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DemoWebActivity.class);
                intent.putExtra("url","http://91qianmi.com/bmember/imgregister.xhtm?inviteCode=A918216");
                startActivity(intent);
            }
        });
    }
}
