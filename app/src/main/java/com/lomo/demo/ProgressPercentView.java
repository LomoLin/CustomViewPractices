package com.lomo.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Lomo on 2017/9/22.
 */

public class ProgressPercentView extends View {

    private static final float DEFAULT_TEXT_SIZE = 40;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_STROKE_COLOR = Color.BLACK;
    private static final float DEFAULT_STROKE_WIDTH = 1;
    private static final int DEFAULT_PROGRESS_COLOR = Color.RED;
    private static final int DEFAULT_PROGRESS_BG_COLOR = Color.WHITE;

    private float mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mStrokeColor = DEFAULT_STROKE_COLOR;
    private float mStrokeWidth = DEFAULT_STROKE_WIDTH;
    private int mProgressColor = DEFAULT_PROGRESS_COLOR;
    private int mProgressBgColor = DEFAULT_PROGRESS_BG_COLOR;
    private float mMax = 100;
    private float mCurrent = 50;
    private String mText = "测试";
    private float mGap = 20;

    private Paint mPaint;

    public void setCurrent(float current) {
        mCurrent = current;
//        if (0 < mCurrent && mCurrent < 60) {
//            mText = "%";
//        } else if (mCurrent >=60 && mCurrent < 80) {
//            mText = "及格";
//        } else if (mCurrent >= 80 && mCurrent < 90) {
//            mText = "良好";
//        } else if (mCurrent >= 90) {
//            mText = "优秀";
//        }
        mText = (int)mCurrent + "%";
        postInvalidate();
    }

    public ProgressPercentView(Context context) {
        this(context, null);
    }

    public ProgressPercentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressPercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressPercentView);
        mTextColor = typedArray.getColor(R.styleable.ProgressPercentView_pv_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = typedArray.getDimension(R.styleable.ProgressPercentView_pv_textSize, DEFAULT_TEXT_SIZE);
        mStrokeColor = typedArray.getColor(R.styleable.ProgressPercentView_pv_strokeColor, DEFAULT_STROKE_COLOR);
        mStrokeWidth = typedArray.getDimension(R.styleable.ProgressPercentView_pv_strokeWidth, DEFAULT_STROKE_WIDTH);
        mProgressColor = typedArray.getColor(R.styleable.ProgressPercentView_pv_progressColor, DEFAULT_PROGRESS_COLOR);
        mProgressBgColor = typedArray.getColor(R.styleable.ProgressPercentView_pv_progressBgColor, DEFAULT_PROGRESS_BG_COLOR);
        mMax = typedArray.getFloat(R.styleable.ProgressPercentView_pv_max, mMax);
        mCurrent = typedArray.getFloat(R.styleable.ProgressPercentView_pv_current, mCurrent);
        mGap = typedArray.getDimension(R.styleable.ProgressPercentView_pv_gap, mGap);

        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("xmlinjj", "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("xmlinjj", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("xmlinjj", "onMeasure");
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width,height;
//        if (widthMode == MeasureSpec.EXACTLY) {
//            width = widthSize;
//        } else {
//            width = widthSize + getPaddingLeft() + getPaddingRight();
//        }
//        if (heightMode == MeasureSpec.EXACTLY) {
//            height = heightSize;
//        } else {
//            height = heightSize + getPaddingTop() + getPaddingBottom();
//        }
//
//        setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("xmlinjj", "onDraw");
        super.onDraw(canvas);
        Rect rect = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), rect);

        //边框
        mPaint.setColor(mStrokeColor);
        RectF strokeRectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - mGap - rect.width() - getPaddingRight(), getHeight() - getPaddingBottom());
        canvas.drawRoundRect(strokeRectF, getHeight() / 2, getHeight() / 2, mPaint);

        //画背景色
        mPaint.setColor(mProgressBgColor);
        RectF bgRectF = new RectF(mStrokeWidth + getPaddingLeft(), mStrokeWidth + getPaddingTop(), getWidth() - mStrokeWidth - mGap - rect.width() - getPaddingRight(), getHeight() - mStrokeWidth - getPaddingRight());
        canvas.drawRoundRect(bgRectF, (getHeight() - mStrokeWidth) / 2, (getHeight() - mStrokeWidth) / 2, mPaint);

        //画进度条
        mPaint.setColor(mProgressColor);
        RectF progressRectF = new RectF(mStrokeWidth + getPaddingLeft(), mStrokeWidth + getPaddingTop(), (getWidth() - mStrokeWidth - mGap - rect.width() - getPaddingRight()) * mCurrent / mMax, getHeight() - mStrokeWidth - getPaddingBottom());
        canvas.drawRoundRect(progressRectF, (getHeight() - mStrokeWidth) / 2, (getHeight() - mStrokeWidth) / 2, mPaint);

        //文字
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, strokeRectF.width() + mGap + getPaddingLeft(), (getHeight() - (mPaint.ascent() + mPaint.descent())) / 2, mPaint);
    }
}
