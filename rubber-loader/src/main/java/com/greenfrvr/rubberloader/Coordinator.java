package com.greenfrvr.rubberloader;

import android.animation.ValueAnimator;
import android.graphics.PointF;

import com.greenfrvr.rubberloader.calculation.BezierEndpoints;
import com.greenfrvr.rubberloader.calculation.Intersection;
import com.greenfrvr.rubberloader.internal.BezierQ;
import com.greenfrvr.rubberloader.internal.Circle;
import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;

/**
 * Created by greenfrvr
 */
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    public static final long DEFAULT_DURATION = 700;

    private RubberLoaderView view;

    private Circle leftCircle;
    private Circle rightCircle;
    private BezierQ topBezier = new BezierQ();
    private BezierQ botBezier = new BezierQ();

    private Intersection intersection;
    private BezierEndpoints topEndpoints;
    private BezierEndpoints botEndpoints;

    private float t = -1f;

    public Coordinator(RubberLoaderView view) {
        init();
        this.view = view;
        this.intersection = Intersection.newInstance();
        this.topEndpoints = BezierEndpoints.top();
        this.botEndpoints = BezierEndpoints.bot();
        this.leftCircle = new Circle();
        this.rightCircle = new Circle();
        this.topBezier = new BezierQ();
        this.botBezier = new BezierQ();
    }

    public Circle leftCircle() {
        return leftCircle;
    }

    public Circle rightCircle() {
        return rightCircle;
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

    private void init(){
        setFloatValues(-1, 1);
        setDuration(DEFAULT_DURATION);
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        addUpdateListener(this);
        setInterpolator(new PulseInterpolator());
    }

    private void evaluateCircleCoors() {
        float value1 = Math.signum(t) < 0 ? Math.abs(t) : 0;
        float value2 = Math.signum(t) > 0 ? Math.abs(t) : 0;

        leftCircle.set(-Math.abs(t) * 4 * view.getDiff(), 0, -view.getDiff() * value1);
        rightCircle.set(Math.abs(t) * 4 * view.getDiff(), 0, -view.getDiff() * value2);

        leftCircle.offset(view.getWidth() / 2, view.getHeight() / 2, view.getRadius());
        rightCircle.offset(view.getWidth() / 2, view.getHeight() / 2, view.getRadius());
    }

    private void evaluateBezierCoors() {
        intersection.circlesIntersection(leftCircle, rightCircle, topBezier.getMiddle(), botBezier.getMiddle());

        topEndpoints.evaluateBezierEndpoints(leftCircle, rightCircle, topBezier.middleOffset(0, -.7f * view.getDiff() * Math.abs(t)));
        botEndpoints.evaluateBezierEndpoints(leftCircle, rightCircle, botBezier.middleOffset(0, .7f * view.getDiff() * Math.abs(t)));
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        t = 2f * animation.getAnimatedFraction() - 1f;
        evaluateCircleCoors();
        evaluateBezierCoors();
        view.invalidate();
    }
}
