package com.pingxundata.pxcore.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by luoyang on 15/8/17.
 */
public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {

    private CanScrollUpListener mCanScrollUpListener;

    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScrollUpListener(CanScrollUpListener canScrollUpListener) {
        mCanScrollUpListener = canScrollUpListener;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCanScrollUpListener != null) {
            return mCanScrollUpListener.canScrollUp();
        }
        return super.canChildScrollUp();
    }

    public interface CanScrollUpListener {
        boolean canScrollUp();
    }


}
