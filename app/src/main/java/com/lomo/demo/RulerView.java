package com.lomo.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

/**
 * Created by Lomo on 2017/10/27.
 */

public class RulerView extends View {
    //最小刻度
    private static final int MIN = 0;
    //最大刻度
    private static final int MAX = 20;
    //短刻度长度
    private static final int SHORT_LENGTH = 50;
    //长刻度长度
    private static final int LONG_LENGTH = 200;
    //每个刻度之间的小刻度数
    private static final int COUNT = 10;
    //每个小刻度之间的距离
    private static final int INTERVAL = 50;

    private Context mContext;

    private Paint mRulerPaint;

    private OverScroller mOverScroller;

    private VelocityTracker mVelocityTracker;

    private float mLastX;
    //最小速度
    private int mMinVelocity;
    //最大速度
    private int mMaxVelocity;
    //overscroller最小overscroll距离
    private int mMinPosition;
    //overscroller最大overscroll距离
    private int mMaxPosition;

    private int mLength;
    //当前的刻度（乘以10）
    private float mCurrentScale = 1 * COUNT;

    private RulerCallback mRulerCallback;

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mMinVelocity = ViewConfiguration.getMinimumFlingVelocity();
        mMaxVelocity = ViewConfiguration.getMaximumFlingVelocity();

        mRulerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRulerPaint.setStyle(Paint.Style.STROKE);
        mRulerPaint.setColor(Color.GREEN);
        mRulerPaint.setTextSize(40);

        mOverScroller = new OverScroller(mContext);
        mVelocityTracker = VelocityTracker.obtain();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                goToScale(mCurrentScale);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = MIN;
        for (int i = MIN;i <= (MAX - MIN) * COUNT;i++) {
            canvas.drawLine(x, 0, x + INTERVAL, 0, mRulerPaint);
            if (i % COUNT == 0) {
                canvas.drawLine(x, 0, x, LONG_LENGTH, mRulerPaint);
                canvas.drawText(String.valueOf(i / COUNT), x, LONG_LENGTH + 50, mRulerPaint);
            } else {
                canvas.drawLine(x, 0, x, SHORT_LENGTH, mRulerPaint);
            }

            x += INTERVAL;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLength = (MAX - MIN) * INTERVAL * COUNT;
        mMinPosition = -getMeasuredWidth() / 2;
        mMaxPosition = mLength + mMinPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                mLastX = currentX;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = mLastX - currentX;
                mLastX = currentX;
                scrollBy((int) moveX, 0);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                if (Math.abs(velocityX) > mMinVelocity) {
                    mOverScroller.fling(getScrollX(), 0, -velocityX, 0, mMinPosition, mMaxPosition, 0, 0);
                    postInvalidate();
                } else {
                    scrollBackToCurrentScale();
                }
                mVelocityTracker.clear();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                break;
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (x < mMinPosition) {
            x = mMinPosition;
        }
        if (x > mMaxPosition) {
            x = mMaxPosition;
        }
        if (x != getScrollX()) {
            super.scrollTo(x, y);
        }
        mCurrentScale = scrollXtoScale(x);
        if (null != mRulerCallback) {
            mRulerCallback.onScaleChange(Math.round(mCurrentScale));
        }
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());
            if (!mOverScroller.computeScrollOffset() && mCurrentScale != Math.round(mCurrentScale)) {
                scrollBackToCurrentScale();
            }
            postInvalidate();
        }
    }

    private float scrollXtoScale (int scrollX) {
        return ((float)(scrollX - mMinPosition) / mLength) * (MAX - MIN) * COUNT + MIN;
    }

    private int scaleToScrollX(float scale) {
        return (int) ((scale / COUNT - MIN) * COUNT * INTERVAL + mMinPosition);
    }

    private void scrollBackToCurrentScale() {
        mCurrentScale = Math.round(mCurrentScale);
        mOverScroller.startScroll(getScrollX(), 0, scaleToScrollX(mCurrentScale) - getScrollX(), 0, 1000);
        postInvalidate();
    }

    public void setRulerCallback(RulerCallback callback) {
        mRulerCallback = callback;
    }

    public void goToScale(float scale) {
        mCurrentScale = Math.round(scale);
        scrollTo(scaleToScrollX(mCurrentScale), 0);
        if (null != mRulerCallback) {
            mRulerCallback.onScaleChange(mCurrentScale);
        }
    }
}
