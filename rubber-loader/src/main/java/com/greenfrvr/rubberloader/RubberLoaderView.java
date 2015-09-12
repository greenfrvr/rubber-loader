package com.greenfrvr.rubberloader;

import android.animation.TimeInterpolator;
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
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by greenfrvr
 */
public class RubberLoaderView extends View {

    @IntDef({EXTRA_TINY, TINY, SMALL, NORMAL, MEDIUM, LARGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoaderSize {
    }

    public static final int EXTRA_TINY = 0;
    public static final int TINY = 1;
    public static final int SMALL = 2;
    public static final int NORMAL = 3;
    public static final int MEDIUM = 4;
    public static final int LARGE = 5;

    private static final SparseIntArray radiusMap = new SparseIntArray(4);

    static {
        radiusMap.put(EXTRA_TINY, R.dimen.extra_tiny_radius);
        radiusMap.put(TINY, R.dimen.tiny_radius);
        radiusMap.put(SMALL, R.dimen.default_radius);
        radiusMap.put(NORMAL, R.dimen.normal_radius);
        radiusMap.put(MEDIUM, R.dimen.medium_radius);
        radiusMap.put(LARGE, R.dimen.large_radius);
    }

    private int size = SMALL;
    private float radius;
    private float diff;
    private int primeColor;
    private int extraColor;

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
        preparePaint();
        prepareMetrics();
    }

    /**
     * Sets the size of a loader. Only pre-defined values are available.
     * Available flags: <i>EXTRA_TINY, TINY, SMALL (default), NORMAL, MEDIUM, LARGE</i>
     *
     * @param size Size flag representing one of pre-defined size values.
     */
    public void setSize(@LoaderSize int size) {
        this.size = size;
        prepareMetrics();
    }

    /**
     * Sets loader prime and extra color.
     *
     * @param primeColor Prime color value
     * @param extraColor Extra color value
     */
    public void setPalette(@ColorInt int primeColor, @ColorInt int extraColor) {
        this.primeColor = primeColor;
        this.extraColor = extraColor == 0 ? primeColor : extraColor;
        pathPaint.setColor(primeColor);
        gradient = null;
    }

    /**
     * Sets loader prime and extra color.
     *
     * @param primeId Prime color resId
     * @param extraId Extra color resId
     */
    public void setPaletteRes(@ColorRes int primeId, @ColorRes int extraId) {
        this.primeColor = getResources().getColor(primeId);
        this.extraColor = getResources().getColor(extraId);
        pathPaint.setColor(primeColor);
        gradient = null;
    }


    /**
     * Sets animation interpolator.
     *
     * @param interpolator Interpolator to be used by loader animation
     */
    public void setInterpolator(TimeInterpolator interpolator) {
        coordinator.setInterpolator(interpolator);
    }

    /**
     * Sets the length of the animation. Default duration is 700 millis.
     *
     * @param duration Length of the animation, in milliseconds.
     */
    public void setDuration(long duration) {
        coordinator.setDuration(duration);
    }

    /**
     * The amount of time to delay animation start.
     *
     * @param delay Delay, in millis
     */
    public void setDelay(long delay) {
        coordinator.setStartDelay(delay);
    }


    /**
     * Starts loader animation
     */
    public void startLoading() {
        coordinator.start();
    }

    /**
     * Starts loader animation with defined delay
     *
     * @param delay Delay, in millis
     */
    public void startLoading(long delay) {
        coordinator.setStartDelay(delay);
        coordinator.start();
    }

    private void extractAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RubberLoaderView, 0, 0);
        try {
            primeColor = a.getColor(R.styleable.RubberLoaderView_loaderPrimeColor, Color.BLACK);
            extraColor = a.getColor(R.styleable.RubberLoaderView_loaderExtraColor, primeColor);

            size = a.getInt(R.styleable.RubberLoaderView_loaderSize, SMALL);
        } finally {
            a.recycle();
        }
    }

    private void preparePaint() {
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(primeColor);

        pathPaint.setDither(true);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);

        coordinator = new Coordinator(this);
    }

    private void prepareMetrics() {
        radius = getResources().getDimension(radiusMap.get(size));
        diff = radius / 6;
    }

    private void prepareGradient() {
        gradient = new LinearGradient(leftRect.centerX(), 0, rightRect.centerX(), 0, primeColor, extraColor, Shader.TileMode.CLAMP);
        pathPaint.setShader(gradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec((int) (4 * radius), MeasureSpec.EXACTLY) + getPaddingLeft() + getPaddingRight();
        int height = MeasureSpec.makeMeasureSpec((int) (2 * radius), MeasureSpec.EXACTLY) + getPaddingTop() + getPaddingBottom();
        super.onMeasure(width, height);

        evaluateCenter();
        evaluateCoors();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        evaluateCenter();
        evaluateCoors();
        updatePaint();
        drawLoader(canvas);
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

    private void drawLoader(Canvas canvas) {
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
        if (gradient == null) {
            prepareGradient();
        }
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
