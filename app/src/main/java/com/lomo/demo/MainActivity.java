package com.lomo.demo;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements RulerCallback {
    private TextView tvScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressPercentView view = (ProgressPercentView) findViewById(R.id.progressView);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "current", 0, 80);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

        RulerView rulerView = (RulerView) findViewById(R.id.ruler);
        rulerView.setRulerCallback(this);
        tvScale = (TextView) findViewById(R.id.tvScale);
    }

    @Override
    public void onScaleChange(float scale) {
        tvScale.setText((scale / 10) + "");
    }
}
