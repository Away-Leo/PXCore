package com.pingxundata.pxcore.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.metadata.listener.OnRulerChangeListener;

import java.text.SimpleDateFormat;


/**
 * Created by ry on 2016/9/22.
 * 自定义尺子
 */
public class RulerView extends View {

    Context context;

    //父容器滚动窗
    private ScrollView parentScrollView;

    //最大刻度
    private int maxValue;
    //最小刻度
    private int minValue;
    //刻度字体大小
    private int scaleTextSize;
    //刻度字体颜色
    private int scaleTextColor;
    //刻度超过颜色
    private int scaleSelectColor;
    //刻度超过背景
    private int scaleSelectBackgroundColor;
    //刻度未超颜色
    private int scaleUnSelectColor;
    //标记颜色
    private int cursorColor;
    //标尺开始位置
    private int currLocation = 0;
    //一屏显示Item
    private int showItemSize;
    //一个刻度的大小
    private int oneItemValue;
    //画线Paint
    private Paint scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //画线Paint
    private Paint bottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //画线Paint
    private Paint cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //画字Paint
    private Paint paintText;
    //屏幕宽度
    private int screenWidth;
    //尺子控件总宽度
    private float viewWidth;
    //尺子控件总宽度
    private float viewHeight;
    //刻度宽度
    private int scaleWidth;
    //刻度高度
    private float scaleHeight = 40;
    //滚动器
    private Scroller scroller;
    // 手势识别
    private GestureDetector gestureDetector;
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中
    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private int headerViewHeight; // 头布局的高度
    private View headerView; // 头布局的对象
    private ImageView ivArrow; // 头布局的剪头
    private ProgressBar mProgressBar; // 头布局的进度条
    private TextView tvState; // 头布局的状态
    private TextView tvLastUpdateTime; // 头布局的最后更新时间
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    //滚动偏移量
    private int scrollingOffset;
    // 是否在滚动
    private boolean isScrollingPerformed;
    // 最低速度
    private static final int MIN_DELTA_FOR_SCROLLING = 1;
    //最后一次滚动到哪
    private int lastScrollX;
    // 消息
    private final int MESSAGE_SCROLL = 0;

    private OnRulerChangeListener onRulerChangeListener;

    public void setOnRulerChangeListener(OnRulerChangeListener onRulerChangeListener) {
        this.onRulerChangeListener = onRulerChangeListener;
    }

    public void setCurrLocation(int currLocation) {
        this.currLocation = currLocation;
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
//        maxValue = typedArray.getDimensionPixelOffset(R.styleable.RulerView_max_value, 200000);
        maxValue = typedArray.getDimensionPixelOffset(R.styleable.RulerView_max_value, 201000);
        minValue = typedArray.getDimensionPixelOffset(R.styleable.RulerView_min_value, 0);
        scaleTextSize = typedArray.getDimensionPixelOffset(R.styleable.RulerView_scale_text_size, 34);
        scaleTextColor = typedArray.getColor(R.styleable.RulerView_scale_text_color, Color.parseColor("#111111"));
        scaleSelectColor = typedArray.getColor(R.styleable.RulerView_scale_select_color, Color.parseColor("#76e4ff"));
        scaleSelectBackgroundColor = typedArray.getColor(R.styleable.RulerView_scale_select_background_color, Color.parseColor("#6676e4ff"));
        scaleUnSelectColor = typedArray.getColor(R.styleable.RulerView_scale_unselect_color, Color.parseColor("#111111"));
        cursorColor = typedArray.getColor(R.styleable.RulerView_tag_color, Color.parseColor("#ff5555"));
        if (currLocation == 0) {
            currLocation = typedArray.getDimensionPixelOffset(R.styleable.RulerView_start_location, 0);
        }
        showItemSize = typedArray.getInteger(R.styleable.RulerView_show_item_size, 5);
        oneItemValue = typedArray.getInteger(R.styleable.RulerView_show_item_size, 1000);
        typedArray.recycle();
        //一个刻度的宽度
        scaleWidth = (screenWidth / (showItemSize * 10));
        //尺子长度总的个数*一个的宽度
        viewWidth = maxValue / oneItemValue * scaleWidth;
        //滚动计算器
        scroller = new Scroller(context);
        //手势解析器
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBottomLine(canvas);
        drawScale(canvas);
        drawCursor(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

    }

    private void drawBottomLine(Canvas canvas) {
        bottomLinePaint.setStrokeWidth(3);
        bottomLinePaint.setColor(scaleUnSelectColor);
        canvas.drawLine(0, viewHeight, viewWidth, viewHeight, bottomLinePaint);
    }


    private void drawCursor(Canvas canvas) {
        cursorPaint.setStrokeWidth(3);
        cursorPaint.setColor(cursorColor);
        canvas.drawLine(screenWidth / 2, viewHeight / 5, screenWidth / 2, viewHeight, cursorPaint);
    }

    private void drawScale(Canvas canvas) {
        scalePaint.setStrokeWidth(2);
        //计算游标开始绘制的位置
        float startLocation = (screenWidth / 2) - ((scaleWidth * (currLocation / oneItemValue)));
        for (int i = 0; i < maxValue / oneItemValue; i++) {
            //判断当前刻度是否小于当前刻度
            if (i * oneItemValue <= currLocation) {
                scalePaint.setColor(scaleSelectColor);
            } else {
                scalePaint.setColor(scaleUnSelectColor);
            }
            float location = startLocation + i * scaleWidth;
            if (i % 10 == 0) {
                canvas.drawLine(location, viewHeight - scaleHeight-100, location, viewHeight, scalePaint);
                paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setTextSize(scaleTextSize);
                if (i * oneItemValue <= currLocation) {
                    paintText.setColor(scaleSelectColor);
                } else {
                    paintText.setColor(scaleTextColor);
                }
                String drawStr = oneItemValue * i + "";
                Rect bounds = new Rect();
                paintText.getTextBounds(drawStr, 0, drawStr.length(), bounds);
                canvas.drawText(drawStr, location - bounds.width() / 2, viewHeight - (scaleHeight + 5)-120, paintText);
            } else {
                canvas.drawLine(location, viewHeight - scaleHeight / 2-80, location, viewHeight, scalePaint);
            }
            //绘制选中的背景
            scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            scalePaint.setStyle(Paint.Style.FILL);
            scalePaint.setColor(scaleSelectBackgroundColor);
            canvas.drawRect(startLocation, viewHeight - scaleHeight / 3, startLocation + currLocation / oneItemValue * scaleWidth, viewHeight, scalePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
//                parentScrollView.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                parentScrollView.requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return true;
    }


    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            parentScrollView.requestDisallowInterceptTouchEvent(true);
            if (!isScrollingPerformed) {
                isScrollingPerformed = true;
            }
            doScroll((int) -distanceX);
            invalidate();
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            lastScrollX = getCurrentItem() * getItemWidth() + scrollingOffset;
            int maxX = getItemsCount()
                    * getItemWidth();
            int minX = 0;
            scroller.fling(lastScrollX, 0, (int) (-velocityX / 1.5), 0, minX, maxX, 0, 0);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    private void setNextMessage(int message) {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.sendEmptyMessage(message);
    }

    // 动画处理
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currX = scroller.getCurrX();
            int delta = lastScrollX - currX;
            lastScrollX = currX;
            if (delta != 0) {
                doScroll(delta);
            }
            // 滚动还没有完成，到最后，完成手动
            if (Math.abs(currX - scroller.getFinalX()) < MIN_DELTA_FOR_SCROLLING) {
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else {
                finishScrolling();
            }
        }
    };


    private void finishScrolling() {
        if (isScrollingPerformed) {
            isScrollingPerformed = false;
        }
        invalidate();
    }

    private void doScroll(int delta) {
        //偏移量叠加
        scrollingOffset += delta;
        //总共滚动了多少个Item
        int count = scrollingOffset / getItemWidth();
        //当前位置
        int pos = getCurrentItem() - count;
        //限制滚到范围
        if (isScrollingPerformed) {
            if (pos < 0) {
                count = getCurrentItem();
                pos = 0;
            } else if (pos >= getItemsCount()) {
                count = getCurrentItem() - getItemsCount() + 1;
                pos = getItemsCount() - 1;
            }
        }
        int offset = scrollingOffset;
        //移动了一个Item的距离，就更新页面
        if (pos != getCurrentItem()) {
            setCurrentItem(pos);
        }

        // 重新更新一下偏移量
        scrollingOffset = offset - count * getItemWidth();

    }

    public int getItemWidth() {
        return scaleWidth;
    }

    public int getCurrentItem() {
        return currLocation / oneItemValue;
    }

    public int getItemsCount() {
        return maxValue / oneItemValue;
    }

    public void setCurrentItem(int index) {
        scrollingOffset = 0;
        currLocation = index * oneItemValue;
        invalidate();
        if (onRulerChangeListener != null) {
            onRulerChangeListener.onChanged(currLocation);
        }
    }

    public int getCurrLocation() {
        return currLocation;
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        try {
            headerView.setPadding(0, -headerViewHeight, 0, 0);
            ivArrow.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            tvState.setText("下拉刷新");
            tvLastUpdateTime.setText(getLastUpdateTime());
        } catch (Exception e) {
            Log.i("error", e.getMessage());
        }
        currentState = DOWN_PULL_REFRESH;
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }

    public ScrollView getParentScrollView() {
        return parentScrollView;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }
}
