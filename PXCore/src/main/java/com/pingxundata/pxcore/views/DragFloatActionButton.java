package com.pingxundata.pxcore.views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.pingxundata.pxcore.utils.DensityUtil;
import com.pingxundata.pxcore.utils.ObjectHelper;
import com.pingxundata.pxcore.utils.ToastUtils;

/**   
* @Title: 可拖拽控件
* @Description: 可退拽控件
* @author Away
* @date 2017/10/26 10:09 
* @copyright 重庆平讯数据
* @version V1.0   
*/
@SuppressLint("AppCompatCustomView")
public class DragFloatActionButton extends Button {

    private int parentHeight;
    private int parentWidth;
    private Context mContext;
    private static OnClickListener mOnClickListener;


    public static OnClickListener getmOnClickListener() {
        return mOnClickListener;
    }

    public static void setmOnClickListener(OnClickListener mOnClickListener) {
        DragFloatActionButton.mOnClickListener = mOnClickListener;
    }

    public DragFloatActionButton(Context context) {
        super(context);
        this.mContext=context;
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
    }


    private int lastX;
    private int lastY;
    private float firstClickX;
    private float endClickX;



    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                firstClickX = endClickX = event.getX();
                setPressed(true);
                isDrag=false;
                lastX=rawX;
                lastY=rawY;
                ViewGroup parent;
                if(getParent()!=null){
                    parent= (ViewGroup) getParent();
                    parentHeight=parent.getHeight();
                    parentWidth=parent.getWidth();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(parentHeight<=0||parentWidth==0){
                    isDrag=false;
                    break;
                }else {
                    isDrag=true;
                }
                int dx=rawX-lastX;
                int dy=rawY-lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                if(distance==0){
                    isDrag=false;
                    break;
                }
                float x=getX()+dx;
                float y=getY()+dy;
                //检测是否到达边缘 左上右下
                x=x<0?0:x>parentWidth-getWidth()?parentWidth-getWidth():x;
                y=getY()<0?0:getY()+getHeight()>parentHeight?parentHeight-getHeight():y;
                setX(x);
                setY(y);
                lastX=rawX;
                lastY=rawY;
                Log.i("aa","isDrag="+isDrag+"getX="+getX()+";getY="+getY()+";parentWidth="+parentWidth);
                break;
            case MotionEvent.ACTION_UP:
                endClickX = event.getX();
                if (endClickX - firstClickX == 0) {
                    if(ObjectHelper.isNotEmpty(this.mOnClickListener))this.mOnClickListener.onClick(this);
                }else{
                    if(!isNotDrag()){
                        //恢复按压效果
                        setPressed(false);
                        //Log.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
                        if(rawX>=parentWidth/2){
                            //靠右吸附
                            animate().setInterpolator(new DecelerateInterpolator())
                                    .setDuration(500)
                                    .xBy(parentWidth-getWidth()-getX()+ DensityUtil.dip2px(this.mContext,25))
                                    .start();
                        }else {
                            //靠左吸附
                            ObjectAnimator oa=ObjectAnimator.ofFloat(this,"x",getX(),0-DensityUtil.dip2px(this.mContext,25));
                            oa.setInterpolator(new DecelerateInterpolator());
                            oa.setDuration(500);
                            oa.start();
                        }
                    }
                }
                break;
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return !isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag(){
        return !isDrag&&(getX()==0
                ||(getX()==parentWidth-getWidth()));
    }

}
