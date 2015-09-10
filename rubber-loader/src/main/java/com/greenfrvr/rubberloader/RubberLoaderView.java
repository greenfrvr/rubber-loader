package com.greenfrvr.rubberloader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by greenfrvr
 */
public class RubberLoaderView extends View {

    private float radius;
    private float diff;

    private int color1;
    private int color2;

    private int centerX;
    private int centerY;

    private Path path = new Path();
    private RectF leftRect = new RectF();
    private RectF rightRect = new RectF();
    private Paint pathPaint = new Paint();

    private Shader gradient;
    private Matrix gradMatrix = new Matrix();

    private Float t = -1f;

    private Coordinator coordinator;

    private double[][] intersection = new double[2][2];
    private double[][] topBezier = new double[2][2];
    private double[][] botBezier = new double[2][2];

    public RubberLoaderView(Context context) {
        super(context);
    }

    public RubberLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        extractAttrs(attrs);
        prepare();
    }

    public void startLoading() {
        coordinator.start();
    }

    public void startLoading(long delay) {
        coordinator.setStartDelay(delay);
        coordinator.start();
    }

    private void extractAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RubberLoaderView, 0, 0);
        try {
            radius = a.getDimension(R.styleable.RubberLoaderView_maxCircleRadius, getResources().getDimension(R.dimen.default_radius));
            diff = a.getDimension(R.styleable.RubberLoaderView_minCircleRadius, getResources().getDimension(R.dimen.default_diff));

            color1 = a.getColor(R.styleable.RubberLoaderView_minCircleColor, Color.BLACK);
            color2 = a.getColor(R.styleable.RubberLoaderView_maxCircleColor, Color.BLACK);
        } finally {
            a.recycle();
        }
    }

    private void prepare() {
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(color2);

        pathPaint.setDither(true);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);

        coordinator = new Coordinator(this);
    }

    private void prepareGradient() {
        gradient = new LinearGradient(leftRect.centerX(), 0, rightRect.centerX(), 0, color1, color2, Shader.TileMode.CLAMP);
        pathPaint.setShader(gradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        evaluateCenter();
        evaluateCoors();
        prepare();
        prepareGradient();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        evaluateCoors();
        updatePaint();
        draw2Quads(canvas);
    }

    private void evaluateCenter() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    private void evaluateCoors() {
        float value1 = Math.signum(t) < 0 ? Math.abs(t) : 0;
        float value2 = Math.signum(t) > 0 ? Math.abs(t) : 0;

        leftRect.set(
                -Math.abs(t) * 4 * diff - (radius - diff * value1), -(radius - diff * value1),
                -Math.abs(t) * 4 * diff + (radius - diff * value1), radius - diff * value1
        );
        rightRect.set(
                Math.abs(t) * 4 * diff - (radius - diff * value2), -(radius - diff * value2),
                Math.abs(t) * 4 * diff + (radius - diff * value2), radius - diff * value2
        );

        leftRect.offset(centerX, centerY);
        rightRect.offset(centerX, centerY);
    }

    private void draw2Quads(Canvas canvas) {
        path.rewind();

        path.addCircle(leftRect.centerX(), leftRect.centerY(), leftRect.width() / 2, Path.Direction.CW);
        path.addCircle(rightRect.centerX(), rightRect.centerY(), rightRect.width() / 2, Path.Direction.CW);

        Calculator.circlesIntersection(leftRect, rightRect, intersection);
        Calculator.evaluateBezierEndpoints(leftRect, rightRect, (float) intersection[0][0], (float) intersection[0][1] - .7f * diff * Math.abs(t), topBezier, true);
        Calculator.evaluateBezierEndpoints(leftRect, rightRect, (float) intersection[1][0], (float) intersection[1][1] + .7f * diff * Math.abs(t), botBezier, false);

        path.moveTo((float) topBezier[0][0], (float) topBezier[0][1]);
        path.quadTo((float) intersection[0][0], (float) intersection[0][1] - .7f * diff * Math.abs(t), (float) topBezier[1][0], (float) topBezier[1][1]);

        path.lineTo((float) botBezier[1][0], (float) botBezier[1][1]);
        path.quadTo((float) intersection[1][0], (float) intersection[1][1] + .7f * diff * Math.abs(t), (float) botBezier[0][0], (float) botBezier[0][1]);

        path.lineTo((float) topBezier[0][0], (float) topBezier[0][1]);

        canvas.drawPath(path, pathPaint);
    }

    private void updatePaint() {
        gradMatrix.reset();
        gradMatrix.setTranslate(2.5f * radius * (1 - Math.abs(t)) * (1 - Math.abs(t)), 0);
        gradMatrix.postRotate(Math.signum(t) > 0 ? 0 : 180, centerX, centerY);

        gradient.setLocalMatrix(gradMatrix);
    }

    protected void invalidate(Float value) {
        t = value;
        invalidate();
    }
}
