package com.lomo.demo;

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

    private float mRaidusOffset = 30;

    private boolean mHasDrawLine = false;

    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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

    @Override
    protected void onDraw(Canvas canvas) {

        if (mSweepAngle < 360) {
            mSweepAngle += 10;
            canvas.drawArc(mRectF, 90, mSweepAngle, false, mOutPaint);
            postInvalidate();
        } else {
            mCirclePaint.setColor(Color.parseColor("#fa7829"));
            canvas.drawCircle(300, 300, mCircleRadius, mCirclePaint);
            mTempRadius -= 5;
            mCirclePaint.setColor(Color.parseColor("#ffffff"));
            canvas.drawCircle(300, 300, mTempRadius, mCirclePaint);
            if (mTempRadius > 0) {
                postInvalidate();
            } else {
                canvas.drawLines(mPoints, mPointPaint);
                mCirclePaint.setColor(Color.parseColor("#fa7829"));
                if (mRaidusOffset <= 30 && mRaidusOffset > 0) {
                    mRaidusOffset -= 5;
                    canvas.drawCircle(300, 300, mCircleRadius + 30 - mRaidusOffset, mCirclePaint);
                    postInvalidate();
                } else if (mRaidusOffset > -30) {
                    mRaidusOffset -= 5;
                    canvas.drawCircle(300, 300, mCircleRadius + 30 + mRaidusOffset, mCirclePaint);
                    postInvalidate();
                }
            }
        }
    }
}
