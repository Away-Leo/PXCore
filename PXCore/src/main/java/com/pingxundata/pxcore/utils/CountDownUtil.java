package com.pingxundata.pxcore.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 计时器
 */
public class CountDownUtil extends CountDownTimer {

    private TextView display;

    public CountDownUtil(long millisInFuture, long countDownInterval,
                         TextView _display) {
        super(millisInFuture, countDownInterval);
        // TODO Auto-generated constructor stub
        display = _display;
    }

    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
        display.setClickable(true);
        display.setText("重新获取");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub
        if (display != null) {
            display.setClickable(false);
            display.setText(millisUntilFinished / 1000 + "秒后可重发");
        }
    }
}
