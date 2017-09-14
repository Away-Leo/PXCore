package com.pingxundata.pxcore.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.utils.ObjectHelper;

import java.util.ArrayList;

/**
* @Title: MarqueeView.java
* @Description: 跑马灯自定义view
* @author Away
* @date 2017/9/12 11:31
* @copyright 重庆平讯数据
* @version V1.0
*/
public class MarqueeView extends FrameLayout {
    private ArrayList<CharSequence> marqueeData;
    private int internal = 2000;//默认滚动时间间隔
    private int textSize = 16;
    private int textGravity = Gravity.LEFT;
    private TextView child1;
    private TextView child2;
    private boolean isRunningAnim = false;
    private int currentPosition = 0;
    private FloatEvaluator floatEval = new FloatEvaluator();

    public MarqueeView(Context context) {
        this(context,null);
    }
    public MarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //init default attrs
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeStyle, defStyleAttr, 0);
        internal = typedArray.getInteger(R.styleable.MarqueeStyle_marqueeInterval,internal);
        textSize = typedArray.getDimensionPixelSize(R.styleable.MarqueeStyle_marqueeTextSize,textSize);
        int gravity = typedArray.getInt(R.styleable.MarqueeStyle_marqueeTextGravity,0);
        switch (gravity){
            case 0:
                textGravity = Gravity.LEFT;
                break;
            case 1:
                textGravity = Gravity.CENTER;
                break;
            case 2:
                textGravity = Gravity.RIGHT;
                break;
        }

        typedArray.recycle();

        initChild();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置文字切换的时间间隔
     * @param internal
     */
    public void setInterval(int internal){
        this.internal = internal;
    }

    /**
     * 设置要滚动的字符串数据，设置完毕后会自动滚动
     * @param marqueeData
     */
    public void setMarqueeData(ArrayList<CharSequence> marqueeData){
        this.marqueeData = marqueeData;

        start();
    }

    private void initChild() {
        if(getChildCount()==2){
            return;
        }
        child1 = createTextView();
        child2 = createTextView();

        addView(child1);
        addView(child2);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(ObjectHelper.isNotEmpty(marqueeData)){
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    ViewCompat.setTranslationY(child2,child2.getHeight());
                    child1.setText(marqueeData.get(currentPosition));
                    child2.setText(marqueeData.get(currentPosition+1));
                }
            }
        });
    }

    private TextView createTextView(){
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER_VERTICAL|textGravity);
        return textView;
    }

    /**
     * 重新开始滚动，调用setMarqueeData方法后会自动调用该方法
     */
    public void start(){
        if(marqueeData==null || marqueeData.size()==0)return;
        if(isRunningAnim)return;
        isStarted = true;

        postDelayed(translationTask,internal);
    }

    private boolean isStarted = false;

    /**
     * 停止滚动
     */
    public void stop(){
        removeCallbacks(translationTask);
        isStarted = false;
    }

    /**
     * 切换滚动与停止
     */
    public void toggleMarquee(){
        if(isStarted){
            stop();
        }else {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    Runnable translationTask = new Runnable(){
        @Override
        public void run() {

            //doAnim
            final float startY1 = child1.getTranslationY();
            final float startY2 = child2.getTranslationY();
            final float endY1 = startY1==0?-getHeight():0;
            final float endY2 = startY2==0?-getHeight():0;

            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    child1.setTranslationY(floatEval.evaluate(fraction,startY1,endY1));
                    child2.setTranslationY(floatEval.evaluate(fraction,startY2,endY2));
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isRunningAnim = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(ObjectHelper.isNotEmpty(marqueeData)){
                        currentPosition = (currentPosition+1)%marqueeData.size();

                        TextView moveView = (child1.getTranslationY()==-getHeight()?child1:child2);
                        moveView.setTranslationY(getHeight()*2);
                        moveView.setText(marqueeData.get(currentPosition));

                        //again
                        postDelayed(translationTask,internal);
                    }

                    isRunningAnim = false;
                }
            });
            animator.setDuration(getHeight()*8)
                    .setInterpolator(new LinearInterpolator());
            animator.start();

        }
    };




}
