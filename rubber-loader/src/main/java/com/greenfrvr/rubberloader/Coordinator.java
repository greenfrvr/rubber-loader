package com.greenfrvr.rubberloader;

import android.animation.Animator;
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
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

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
    private boolean isForward = false;
    private boolean isBackward = false;

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

    public boolean ripple() {
        return isBackward;
    }

    public boolean rippleReverse() {
        return isForward;
    }

    private void init() {
        setFloatValues(-1, 1);
        setDuration(DEFAULT_DURATION);
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        addUpdateListener(this);
        addListener(this);
        setInterpolator(new PulseInterpolator());
    }

    private void evaluateCircleCoors() {
        leftCircle.set(-Math.abs(t) * 4 * view.getDiff(), 0, leftRadiusDiff());
        rightCircle.set(Math.abs(t) * 4 * view.getDiff(), 0, rightRadiusDiff());

        leftCircle.offset(view.getWidth() / 2 + offsetX(), view.getHeight() / 2, view.getRadius());
        rightCircle.offset(view.getWidth() / 2 + offsetX(), view.getHeight() / 2, view.getRadius());
    }

    private float leftRadiusDiff() {
        if (view.getMode() == RubberLoaderView.MODE_EQUAL)
            return -view.getDiff() * abs();
        else
            return -view.getDiff() * (sign() < 0 ? abs() : 0);
    }

    private float rightRadiusDiff() {
        if (view.getMode() == RubberLoaderView.MODE_EQUAL)
            return -view.getDiff() * abs();
        else
            return -view.getDiff() * (sign() > 0 ? abs() : 0);
    }

    private float offsetX() {
        return view.getMode() != RubberLoaderView.MODE_CENTERED ? 0 : Math.abs(t) * 4 * view.getDiff() * sign();
    }

    private void evaluateBezierCoors() {
        intersection.circlesIntersection(leftCircle, rightCircle, topBezier.getMiddle(), botBezier.getMiddle());

        topEndpoints.evaluateBezierEndpoints(leftCircle, rightCircle, topBezier.middleOffset(0, -middleOffset()));
        botEndpoints.evaluateBezierEndpoints(leftCircle, rightCircle, botBezier.middleOffset(0, middleOffset()));
    }

    private float middleOffset() {
        return .8f * view.getDiff() * Math.abs(t);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        t = 2f * animation.getAnimatedFraction() - 1f;
        evaluateCircleCoors();
        evaluateBezierCoors();
        view.invalidate();
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        isBackward = sign() < 0 && !isBackward;
        isForward = sign() > 0 && !isForward;
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }
}
