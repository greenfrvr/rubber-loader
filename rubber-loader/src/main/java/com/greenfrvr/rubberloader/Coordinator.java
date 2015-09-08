package com.greenfrvr.rubberloader;

import android.animation.ValueAnimator;

/**
 * Created by greenfrvr
 */
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    private RubberLoaderView view;

    public Coordinator(RubberLoaderView view) {
        setFloatValues(-1, 1);
        setDuration(800);
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        addUpdateListener(this);
        this.view = view;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        view.invalidate((Float) animation.getAnimatedValue());
    }
}
