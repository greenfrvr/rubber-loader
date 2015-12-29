package com.greenfrvr.rubberloader;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.PointF;

import com.greenfrvr.rubberloader.calculation.BezierEndpoints;
import com.greenfrvr.rubberloader.calculation.Intersection;
import com.greenfrvr.rubberloader.internal.LoaderAnimatorListener;
import com.greenfrvr.rubberloader.internal.BezierQ;
import com.greenfrvr.rubberloader.internal.Circle;
import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;

/**
 * Created by greenfrvr
 */
public class Coordinator implements ValueAnimator.AnimatorUpdateListener {

    private static final float NORMAL_MULTI = 2.0f;
    private static final float NORMAL_OFFSET = -1.0f;
    private static final float START_MULTI = -1.0f;
    private static final float START_OFFSET = 0.0f;

    public static final long DEFAULT_DURATION = 700;

    private RubberLoaderView view;

    private Circle leftCircle;
    private Circle rightCircle;
    private BezierQ topBezier;
    private BezierQ botBezier;

    private Intersection intersection;
    private BezierEndpoints topEndpoints;
    private BezierEndpoints botEndpoints;

    private float t = -0f;
    private float lastMoment;
    private long delay = 0;
    private boolean isForward = false;
    private boolean isBackward = false;
    private boolean isStarted = false;
    private boolean isDismissed = false;

    private ValueAnimator mainAnimator;
    private ValueAnimator optionalAnimator;

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

    void appear() {
        optionalAnimator.addListener(appearListener);
        optionalAnimator.setStartDelay(delay);
        optionalAnimator.start();
    }

    void disappear() {
        lastMoment = t;
        isDismissed = true;
        optionalAnimator.setStartDelay(0);
        optionalAnimator.addListener(disappearListener);
        optionalAnimator.start();
    }

    void update() {
        evaluateCircleCoors();
        evaluateBezierCoors();

        view.invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        t = multiplier() * animation.getAnimatedFraction() + offset();
        update();
    }

    Circle leftCircle() {
        return leftCircle;
    }

    Circle rightCircle() {
        return rightCircle;
    }

    PointF topLeft() {
        return topBezier.getStart();
    }

    PointF topRight() {
        return topBezier.getEnd();
    }

    PointF botRight() {
        return botBezier.getStart();
    }

    PointF botLeft() {
        return botBezier.getEnd();
    }

    PointF topMiddle() {
        return topBezier.getMiddle();
    }

    PointF botMiddle() {
        return botBezier.getMiddle();
    }

    float signum() {
        return Math.signum(t);
    }

    boolean sign() { return signum() > 0; }

    float abs() {
        return Math.abs(t);
    }

    private boolean ripple() {
        return isBackward;
    }

    private boolean rippleReverse() {
        return isForward;
    }

    float animatedFraction() {
        return mainAnimator.getAnimatedFraction();
    }

    void setInterpolator(TimeInterpolator interpolator) {
        mainAnimator.setInterpolator(interpolator);
    }

    void setDelay(long delay) {
        this.delay = delay;
    }

    void setDuration(long duration) {
        mainAnimator.setDuration(duration);
    }

    boolean isRunning() {
        return mainAnimator.isRunning();
    }

    boolean readyForRipple() {
        return (view.getRipple() == RubberLoaderView.RIPPLE_NORMAL && ripple())
                || (view.getRipple() == RubberLoaderView.RIPPLE_REVERSE && rippleReverse())
                || (view.getRipple() == RubberLoaderView.RIPPLE_CYCLE && (ripple() || rippleReverse()));
    }

    private void init() {
        mainAnimator = ValueAnimator.ofFloat(0, 2);
        mainAnimator.setDuration(DEFAULT_DURATION);
        mainAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mainAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mainAnimator.addUpdateListener(this);
        mainAnimator.addListener(listener);
        mainAnimator.setInterpolator(new PulseInterpolator());

        optionalAnimator = ValueAnimator.ofFloat(0, 1);
        optionalAnimator.addUpdateListener(this);
        optionalAnimator.setDuration(DEFAULT_DURATION / 2);
        optionalAnimator.setInterpolator(new PulseInterpolator());
    }

    private void evaluateCircleCoors() {
        leftCircle.set(-Math.abs(t) * 4 * view.getDiff(), 0, leftRadiusDiff());
        rightCircle.set(Math.abs(t) * 4 * view.getDiff(), 0, rightRadiusDiff());

        leftCircle.offset(view.getWidth() / 2 + offsetX(), view.getHeight() / 2, view.getRadius());
        rightCircle.offset(view.getWidth() / 2 + offsetX(), view.getHeight() / 2, view.getRadius());
    }

    private void evaluateBezierCoors() {
        intersection.circlesIntersection(leftCircle, rightCircle, topBezier.getMiddle(), botBezier.getMiddle());

        topEndpoints.evaluateBezierEndpoints(leftCircle, rightCircle, topBezier.middleOffset(0, -middleOffset()));
        botEndpoints.evaluateBezierEndpoints(leftCircle, rightCircle, botBezier.middleOffset(0, middleOffset()));
    }

    private float leftRadiusDiff() {
        if (view.getMode() == RubberLoaderView.MODE_EQUAL)
            return -view.getDiff() * abs();
        else
            return -view.getDiff() * (sign() ? 0 : abs());
    }

    private float rightRadiusDiff() {
        if (view.getMode() == RubberLoaderView.MODE_EQUAL)
            return -view.getDiff() * abs();
        else
            return -view.getDiff() * (sign() ? abs() : 0);
    }

    private float offsetX() {
        return view.getMode() != RubberLoaderView.MODE_CENTERED ? 0 : Math.abs(t) * 4 * view.getDiff() * signum();
    }

    private float middleOffset() {
        return .8f * view.getDiff() * Math.abs(t);
    }

    private float multiplier() {
        return isDismissed ? -lastMoment : isStarted ? NORMAL_MULTI : START_MULTI;
    }

    private float offset() {
        return isDismissed ? lastMoment : isStarted ? NORMAL_OFFSET : START_OFFSET;
    }

    private final Animator.AnimatorListener appearListener = new LoaderAnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isDismissed = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isStarted = true;
            mainAnimator.addUpdateListener(Coordinator.this);
            mainAnimator.start();

            optionalAnimator.removeListener(this);
        }
    };

    private final Animator.AnimatorListener disappearListener = new LoaderAnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isStarted = false;

            mainAnimator.removeUpdateListener(Coordinator.this);
            mainAnimator.end();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            optionalAnimator.removeListener(this);
        }
    };

    private final Animator.AnimatorListener listener = new LoaderAnimatorListener() {
        @Override
        public void onAnimationRepeat(Animator animation) {
            isBackward = !sign() && !isBackward;
            isForward = sign() && !isForward;
        }
    };
}
