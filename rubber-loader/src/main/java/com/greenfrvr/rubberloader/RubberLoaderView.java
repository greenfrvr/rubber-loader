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

    //debug
    public static final int MODE_2_CUBIC = 0;
    public static final int MODE_4_CUBIC = 1;
    public static final int MODE_2_QUADS = 2;
    int algorythm;

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

    public void algorythm(int a) {
        algorythm = a;
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
        gradient = new LinearGradient(leftRect.centerX() + 2 * diff, 0, rightRect.centerX() - 2 * diff, 0, color1, color2, Shader.TileMode.CLAMP);
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

        switch (algorythm) {
            case MODE_2_CUBIC:
                drawPath2Cubics(canvas);
                break;
            case MODE_2_QUADS:
                draw2Quads(canvas);
                break;
            case MODE_4_CUBIC:
                drawPath4Cubics(canvas);
                break;
        }
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

    private void drawPath2Cubics(Canvas canvas) {
        path.rewind();

        path.addArc(leftRect, 90, 180);
        path.addArc(rightRect, 90, -180);

        float middle = (leftRect.centerX() + rightRect.centerX()) / 2;

        path.moveTo(leftRect.centerX(), leftRect.top);
        path.cubicTo(
                middle, leftRect.top,
                middle, rightRect.top,
                rightRect.centerX(), rightRect.top);
        path.lineTo(rightRect.centerX(), rightRect.bottom);

        path.cubicTo(
                centerX, rightRect.bottom,
                centerX, leftRect.bottom,
                leftRect.centerX(), leftRect.bottom);
        path.lineTo(leftRect.centerX(), leftRect.top);

        canvas.drawPath(path, pathPaint);
    }

    private void drawPath4Cubics(Canvas canvas) {
        path.rewind();

        path.addArc(leftRect, 90, 180);
        path.addArc(rightRect, 90, -180);

        float middle = (leftRect.centerX() + rightRect.centerX()) / 2;

        path.moveTo(leftRect.centerX(), leftRect.top);
        //top left curve half
        path.cubicTo(
                (leftRect.centerX() + middle) / 2, leftRect.top,
                (leftRect.centerX() + middle) / 2, centerY - radius + diff * Math.abs(t) * (t > 0 ? 1.0f : 1.1f),
                centerX, centerY - radius + 1.1f * diff * Math.abs(t));
        //top right curve half
        path.cubicTo(
                (rightRect.centerX() + middle) / 2, centerY - radius + diff * Math.abs(t) * (t < 0 ? 1.0f : 1.1f),
                (rightRect.centerX() + middle) / 2, rightRect.top,
                rightRect.centerX(), rightRect.top);
        path.lineTo(rightRect.centerX(), rightRect.bottom);

        //bottom right curve half
        path.cubicTo(
                (rightRect.centerX() + middle) / 2, rightRect.bottom,
                (rightRect.centerX() + middle) / 2, centerY + radius - diff * Math.abs(t) * (t < 0 ? 1.0f : 1.1f),
                centerX, centerY + radius - 1.1f * diff * Math.abs(t));
        //bottom left curve half
        path.cubicTo(
                (leftRect.centerX() + middle) / 2, centerY + radius - diff * Math.abs(t) * (t > 0 ? 1.0f : 1.1f),
                (leftRect.centerX() + middle) / 2, leftRect.bottom,
                leftRect.centerX(), leftRect.bottom);
        path.lineTo(leftRect.centerX(), leftRect.top);

        canvas.drawPath(path, pathPaint);
    }

    private void draw2Quads(Canvas canvas) {
        path.rewind();

        path.addCircle(leftRect.centerX(), leftRect.centerY(), leftRect.width() / 2, Path.Direction.CW);
        path.addCircle(rightRect.centerX(), rightRect.centerY(), rightRect.width() / 2, Path.Direction.CW);

        float middle = (leftRect.centerX() + rightRect.centerX()) / 2;

        path.moveTo(leftRect.centerX(), leftRect.top);
        path.quadTo(
                middle, centerY - radius + 1.2f * diff * Math.abs(t),
                rightRect.centerX(), rightRect.top);
        path.lineTo(rightRect.centerX(), rightRect.bottom);

        path.quadTo(
                middle, centerY + radius - 1.2f * diff * Math.abs(t),
                leftRect.centerX(), leftRect.bottom);
        path.lineTo(leftRect.centerX(), leftRect.top);

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
