package com.pingxundata.pxcore.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PXListview extends ListView {
    public PXListview(Context context) {
        super(context);
    }

    public PXListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PXListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PXListview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
