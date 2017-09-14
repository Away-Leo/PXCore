package com.pingxundata.pxcore.utils;

/**
 * 防止按钮短时间内被多次点击的工具类
 *
 */

public class ClickSleep {
    //默认休眠时间
    public static final long DEFAULT_SLEEP_TIME = 2000;
    //线程运行标志位
    private static boolean isRuning = false;
    //线程是否正在运行
    public static boolean isRuning() {
        return isRuning;
    }
    //运行线程，开始休眠线程
    public static   void runWithTime(final long defaultSleepTime) {
        isRuning = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(defaultSleepTime, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isRuning = false;
                super.run();
            }
        }.start();
    }
}
