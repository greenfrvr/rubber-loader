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

    @IntDef({MODE_NORMAL, MODE_EQUAL, MODE_CENTERED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoaderMode {
    }

    public static final int MODE_NORMAL = 0;
    public static final int MODE_EQUAL = 1;
    public static final int MODE_CENTERED = 2;

    @IntDef({RIPPLE_NONE, RIPPLE_NORMAL, RIPPLE_REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RippleMode {
    }

    public static final int RIPPLE_NONE = 0;
    public static final int RIPPLE_NORMAL = 1;
    public static final int RIPPLE_REVERSE = 2;
    public static final int RIPPLE_CYCLE = 3;

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
    private int ripple = RIPPLE_NONE;
    private int mode = MODE_NORMAL;
    private float radius;
    private float diff;
    private int primeColor;
    private int extraColor;
    private int rippleColor;

    private Path path = new Path();
    private Paint pathPaint = new Paint();
    private Paint ripplePaint = new Paint();

    private Shader gradient;
    private Matrix gradMatrix = new Matrix();

    private Coordinator coors;

    public RubberLoaderView(Context context) {
        super(context);
        extractAttrs(null);
        preparePaint();
        prepareMetrics();
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
     * Sets loader animation mode.
     * Available modes: <i>MODE_NORMAL, MODE_EQUAL, MODE_CENTERED</i>
     *
     * @param loaderMode Loader mode flag
     */
    public void setMode(@LoaderMode int loaderMode) {
        this.mode = loaderMode;
    }

    /**
     * Sets ripple animation mode.
     * Available flags: <i>RIPPLE_NONE, RIPPLE_NORMAL, RIPPLE_REVERSE, RIPPLE_CYCLE</i>
     *
     * @param rippleMode Ripple mode flag
     */
    public void setRippleMode(@RippleMode int rippleMode) {
        this.ripple = rippleMode;
    }

    /**
     * Sets ripple color.
     *
     * @param color Ripple color value
     */
    public void setRippleColor(@ColorInt int color) {
        this.rippleColor = color;
        ripplePaint.setColor(rippleColor);
    }

    /**
     * Sets ripple color.
     *
     * @param colorId Ripple color resId
     */
    public void setRippleRes(@ColorRes int colorId) {
        this.rippleColor = getResources().getColor(colorId);
        ripplePaint.setColor(rippleColor);
    }

    /**
     * Sets animation interpolator.
     *
     * @param interpolator Interpolator to be used by loader animation
     */
    public void setInterpolator(TimeInterpolator interpolator) {
        coors.setInterpolator(interpolator);
    }

    /**
     * Sets the length of the animation. Default duration is 700 millis.
     *
     * @param duration Length of the animation, in milliseconds.
     */
    public void setDuration(long duration) {
        coors.setDuration(duration);
    }

    /**
     * The amount of time to delay animation start.
     *
     * @param delay Delay, in millis
     */
    public void setDelay(long delay) {
        coors.setStartDelay(delay);
    }

    /**
     * Starts loader animation
     */
    public void startLoading() {
        coors.start();
    }

    /**
     * Starts loader animation with defined delay
     *
     * @param delay Delay, in millis
     */
    public void startLoading(long delay) {
        coors.setStartDelay(delay);
        coors.start();
    }

    protected int getMode() {
        return mode;
    }

    protected float getRadius() {
        return radius;
    }

    protected float getDiff() {
        return diff;
    }

    private void extractAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RubberLoaderView, 0, 0);
        try {
            primeColor = a.getColor(R.styleable.RubberLoaderView_loaderPrimeColor, Color.BLACK);
            extraColor = a.getColor(R.styleable.RubberLoaderView_loaderExtraColor, Color.GRAY);
            rippleColor = a.getColor(R.styleable.RubberLoaderView_rippleColor, Color.WHITE);

            size = a.getInt(R.styleable.RubberLoaderView_loaderSize, SMALL);
            ripple = a.getInt(R.styleable.RubberLoaderView_rippleMode, RIPPLE_NONE);
            mode = a.getInt(R.styleable.RubberLoaderView_loaderMode, MODE_NORMAL);
        } finally {
            a.recycle();
        }
    }

    private void preparePaint() {
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setColor(primeColor);

        ripplePaint.setAntiAlias(true);
        ripplePaint.setStyle(Paint.Style.FILL);
        ripplePaint.setColor(rippleColor);

        coors = new Coordinator(this);
    }

    private void prepareMetrics() {
        radius = getResources().getDimension(radiusMap.get(size));
        diff = radius / 6;
    }

    private void prepareGradient() {
        gradient = new LinearGradient(coors.leftCircle().getX(), 0, coors.rightCircle().getX(), 0, primeColor, extraColor, Shader.TileMode.CLAMP);
        pathPaint.setShader(gradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(widthValue(), MeasureSpec.EXACTLY) + getPaddingLeft() + getPaddingRight();
        int height = MeasureSpec.makeMeasureSpec(heightValue(), MeasureSpec.EXACTLY) + getPaddingTop() + getPaddingBottom();
        super.onMeasure(width, height);
    }

    private int widthValue() {
        return (int) (ripple == RIPPLE_NONE ? (4.5 * radius) : (6 * radius));
    }

    private int heightValue() {
        return (int) (ripple == RIPPLE_NONE ? (2 * radius) : (6 * radius));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gradient == null) {
            prepareGradient();
        }
        updatePaint();
        drawRipple(canvas);
        drawLoader(canvas);
    }

    private void drawRipple(Canvas canvas) {
        if (ripple != RIPPLE_NONE) {
            ripplePaint.setAlpha((int) (100 * (1 - coors.getAnimatedFraction())));
            if (ripple == RIPPLE_NORMAL && coors.ripple()) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 * coors.getAnimatedFraction(), ripplePaint);
            } else if (ripple == RIPPLE_REVERSE && coors.rippleReverse()) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 * coors.getAnimatedFraction(), ripplePaint);
            } else if (ripple == RIPPLE_CYCLE && (coors.ripple() || coors.rippleReverse())) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 * coors.getAnimatedFraction(), ripplePaint);
            }
        }
    }

    private void drawLoader(Canvas canvas) {
        path.rewind();

        path.addCircle(coors.leftCircle().getX(), coors.leftCircle().getY(), coors.leftCircle().getR(), Path.Direction.CW);
        path.addCircle(coors.rightCircle().getX(), coors.rightCircle().getY(), coors.rightCircle().getR(), Path.Direction.CW);

        path.moveTo(coors.topLeft().x, coors.topLeft().y);

        path.quadTo(coors.topMiddle().x, coors.topMiddle().y, coors.topRight().x, coors.topRight().y);
        path.lineTo(coors.botRight().x, coors.botRight().y);

        path.quadTo(coors.botMiddle().x, coors.botMiddle().y, coors.botLeft().x, coors.botLeft().y);
        path.lineTo(coors.topLeft().x, coors.topLeft().y);

        canvas.drawPath(path, pathPaint);
    }

    private void updatePaint() {
        gradMatrix.reset();
        gradMatrix.setTranslate(2.5f * radius * (1 - coors.abs()) * (1 - coors.abs()) + centeredOffset(), 0);
        gradMatrix.postRotate(coors.sign() > 0 ? 0 : 180, getWidth() / 2, getHeight() / 2);

        gradient.setLocalMatrix(gradMatrix);
    }

    private float centeredOffset() {
        return mode == MODE_CENTERED ? radius : 0.0f;
    }
}
