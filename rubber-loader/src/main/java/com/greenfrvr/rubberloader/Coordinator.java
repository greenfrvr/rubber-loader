package com.greenfrvr.rubberloader;

import android.animation.ValueAnimator;

import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;

/**
 * Created by greenfrvr
 */
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    public static final long DEFAULT_DURATION = 700;

    private RubberLoaderView view;

    public Coordinator(RubberLoaderView view) {
        setFloatValues(-1, 1);
        setDuration(DEFAULT_DURATION);
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        addUpdateListener(this);
        setInterpolator(new PulseInterpolator());
        this.view = view;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        view.invalidate((Float) animation.getAnimatedValue());
    }
}
