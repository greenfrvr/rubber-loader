package com.greenfrvr.rubberloader;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.RectF;

import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;

/**
 * Created by greenfrvr
 */
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    public static final long DEFAULT_DURATION = 700;

    private RubberLoaderView view;

    private RectF leftRect = new RectF();
    private RectF rightRect = new RectF();

    private PointF[] intersection = new PointF[]{new PointF(), new PointF()};
    private PointF[] topBezier = new PointF[]{new PointF(), new PointF()};
    private PointF[] botBezier = new PointF[]{new PointF(), new PointF()};

    private Float t = -1f;

    public Coordinator(RubberLoaderView view) {
        setFloatValues(-1, 1);
        setDuration(DEFAULT_DURATION);
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        addUpdateListener(this);
        setInterpolator(new PulseInterpolator());
        this.view = view;
    }

    private void evaluateCoors() {
        float value1 = Math.signum(t) < 0 ? Math.abs(t) : 0;
        float value2 = Math.signum(t) > 0 ? Math.abs(t) : 0;

        leftRect.set(
                -Math.abs(t) * 4 * view.getDiff() - (view.getRadius() - view.getDiff() * value1),
                -(view.getRadius() - view.getDiff() * value1),
                -Math.abs(t) * 4 * view.getDiff() + (view.getRadius() - view.getDiff() * value1),
                view.getRadius() - view.getDiff() * value1
        );
        rightRect.set(
                Math.abs(t) * 4 * view.getDiff() - (view.getRadius() - view.getDiff() * value2),
                -(view.getRadius() - view.getDiff() * value2),
                Math.abs(t) * 4 * view.getDiff() + (view.getRadius() - view.getDiff() * value2),
                view.getRadius() - view.getDiff() * value2
        );

        leftRect.offset(view.getWidth() / 2, view.getHeight() / 2);
        rightRect.offset(view.getWidth() / 2, view.getHeight() / 2);
    }

    private void evaluateBezierPoints() {
        Calculator.circlesIntersection(leftRect, rightRect, intersection);
        Calculator.evaluateBezierEndpoints(leftRect, rightRect, intersection[0].x, intersection[0].y - .7f * view.getDiff() * Math.abs(t), topBezier, true);
        Calculator.evaluateBezierEndpoints(leftRect, rightRect, intersection[1].x, intersection[1].y + .7f * view.getDiff() * Math.abs(t), botBezier, false);
    }

    public RectF leftCircle() {
        return leftRect;
    }

    public RectF rightCircle() {
        return rightRect;
    }

    public PointF topLeft() {
        return topBezier[0];
    }

    public PointF topRight() {
        return topBezier[1];
    }

    public PointF botRight() {
        return botBezier[1];
    }

    public PointF botLeft() {
        return botBezier[0];
    }

    public PointF topInter() {
        return intersection[0];
    }

    public PointF botInter() {
        return intersection[1];
    }

    public float sign() {
        return Math.signum(t);
    }

    public float abs() {
        return Math.abs(t);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        t = (Float) animation.getAnimatedValue();
        evaluateCoors();
        evaluateBezierPoints();
        view.invalidate();
    }
}
