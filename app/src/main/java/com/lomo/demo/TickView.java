package com.lomo.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lomo on 2017/10/25.
 */

public class TickView extends View {

    private Paint mOutPaint;
    private float mSweepAngle = 0;
    private RectF mRectF = new RectF(200, 200, 400, 400);
    private float mCircleRadius = 100;
    private float mTempRadius = 100;

    private Paint mCirclePaint;
    private Paint mPointPaint;
    private float[] mPoints = new float[]{280, 300, 300, 320, 300, 320, 330, 280};

    private float mRadiusOffset = 30;

    private AnimatorSet mAnimatorSet;

    private boolean mIsAnimatorStart = false;

    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
        initAnimator();
    }

    public float getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
        postInvalidate();
    }

    public float getTempRadius() {
        return mTempRadius;
    }

    public void setTempRadius(float tempRadius) {
        mTempRadius = tempRadius;
        postInvalidate();
    }

    public float getRadiusOffset() {
        return mRadiusOffset;
    }

    public void setRadiusOffset(float radiusOffset) {
        mRadiusOffset = radiusOffset;
        postInvalidate();
    }

    /**
     * 初始化画笔对象
     */
    private void initPaint() {
        mOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutPaint.setColor(Color.parseColor("#fa7829"));
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setStrokeWidth(10);
        mOutPaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStrokeWidth(5);
    }

    private void initAnimator() {
        ObjectAnimator sweepAnimator = ObjectAnimator.ofFloat(this, "sweepAngle", 0, 360);
        sweepAnimator.setDuration(800);
        sweepAnimator.setInterpolator(null);

        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "tempRadius", mCircleRadius - 5, 0);
        sweepAnimator.setDuration(480);
        radiusAnimator.setInterpolator(null);

        ObjectAnimator offsetAnimator = ObjectAnimator.ofFloat(this, "radiusOffset", 0, 30, 0);
        offsetAnimator.setDuration(720);
        offsetAnimator.setInterpolator(null);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(sweepAnimator, radiusAnimator, offsetAnimator);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mSweepAngle < 360) {
            canvas.drawArc(mRectF, 90, mSweepAngle, false, mOutPaint);
        } else {
            mCirclePaint.setColor(Color.parseColor("#fa7829"));
            canvas.drawCircle(300, 300, mCircleRadius, mCirclePaint);
            mCirclePaint.setColor(Color.parseColor("#ffffff"));
            canvas.drawCircle(300, 300, mTempRadius, mCirclePaint);
            if (mTempRadius == 0) {
                mCirclePaint.setColor(Color.parseColor("#fa7829"));
                canvas.drawCircle(300, 300, mCircleRadius + mRadiusOffset, mCirclePaint);
                canvas.drawLines(mPoints, mPointPaint);
            }
        }

        if (!mIsAnimatorStart) {
            mIsAnimatorStart = true;
            mAnimatorSet.start();
        }
    }
}
