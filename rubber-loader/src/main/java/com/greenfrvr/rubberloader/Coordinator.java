package com.greenfrvr.rubberloader;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.RectF;

import com.greenfrvr.rubberloader.calculation.BezierEndpoints;
import com.greenfrvr.rubberloader.calculation.Intersection;
import com.greenfrvr.rubberloader.internal.BezierQ;
import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;

/**
 * Created by greenfrvr
 */
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    public static final long DEFAULT_DURATION = 700;

    private RubberLoaderView view;

    private RectF leftRect = new RectF();
    private RectF rightRect = new RectF();

    private BezierQ topBezier = new BezierQ();
    private BezierQ botBezier = new BezierQ();

    private Intersection intersection;
    private BezierEndpoints topEndpoints;
    private BezierEndpoints botEndpoints;

    private float t = -1f;

    public Coordinator(RubberLoaderView view) {
        setFloatValues(-1, 1);
        setDuration(DEFAULT_DURATION);
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        addUpdateListener(this);
        setInterpolator(new PulseInterpolator());
        this.view = view;
        this.intersection = Intersection.newInstance();
        this.topEndpoints = BezierEndpoints.top();
        this.botEndpoints = BezierEndpoints.bot();
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
        intersection.circlesIntersection(leftRect, rightRect, topBezier.getMiddle(), botBezier.getMiddle());

        topEndpoints.evaluateBezierEndpoints(leftRect, rightRect, topBezier.middleOffset(0, -.7f * view.getDiff() * Math.abs(t)));
        botEndpoints.evaluateBezierEndpoints(leftRect, rightRect, botBezier.middleOffset(0, .7f * view.getDiff() * Math.abs(t)));
    }

    public RectF leftCircle() {
        return leftRect;
    }

    public RectF rightCircle() {
        return rightRect;
    }

    public PointF topLeft() {
        return topBezier.getStart();
    }

    public PointF topRight() {
        return topBezier.getEnd();
    }

    public PointF botRight() {
        return botBezier.getStart();
    }

    public PointF botLeft() {
        return botBezier.getEnd();
    }

    public PointF topMiddle() {
        return topBezier.getMiddle();
    }

    public PointF botMiddle() {
        return botBezier.getMiddle();
    }

    public float sign() {
        return Math.signum(t);
    }

    public float abs() {
        return Math.abs(t);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        t = 2f * animation.getAnimatedFraction() - 1f;
        evaluateCoors();
        evaluateBezierPoints();
        view.invalidate();
    }
}
