package com.greenfrvr.rubberloader;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.RectF;

import com.greenfrvr.rubberloader.calculation.BezierEndpoints;
import com.greenfrvr.rubberloader.calculation.Intersection;
import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;

/**
 * Created by greenfrvr
 */
public class Coordinator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    public static final long DEFAULT_DURATION = 700;

    private RubberLoaderView view;

    private RectF leftRect = new RectF();
    private RectF rightRect = new RectF();

    private PointF[] interPts = new PointF[]{new PointF(), new PointF()};
    private PointF[] topPts = new PointF[]{new PointF(), new PointF()};
    private PointF[] botPts = new PointF[]{new PointF(), new PointF()};

    private RectF[] topTangents = new RectF[]{new RectF(), new RectF()};
    private RectF[] bottomTangents = new RectF[]{new RectF(), new RectF()};

    private Intersection intersection;
    private BezierEndpoints topEndpoints;
    private BezierEndpoints botEndpoints;

    private Float t = -1f;

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
        intersection.circlesIntersection(leftRect, rightRect, interPts);
        topEndpoints.evaluateBezierEndpoints(leftRect, rightRect, interPts[0].x, interPts[0].y - .7f * view.getDiff() * Math.abs(t), topPts, topTangents);
        botEndpoints.evaluateBezierEndpoints(leftRect, rightRect, interPts[1].x, interPts[1].y + .7f * view.getDiff() * Math.abs(t), botPts, bottomTangents);
    }

    public RectF leftCircle() {
        return leftRect;
    }

    public RectF rightCircle() {
        return rightRect;
    }

    public PointF topLeft() {
        return topPts[0];
    }

    public PointF topRight() {
        return topPts[1];
    }

    public PointF botRight() {
        return botPts[1];
    }

    public PointF botLeft() {
        return botPts[0];
    }

    public PointF topInter() {
        return interPts[0];
    }

    public PointF botInter() {
        return interPts[1];
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
