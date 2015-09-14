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

    private Path path = new Path();
    private Paint pathPaint = new Paint();

    private Shader gradient;
    private Matrix gradMatrix = new Matrix();

    private Coordinator coors;

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

        coors = new Coordinator(this);
    }

    private void prepareMetrics() {
        radius = getResources().getDimension(radiusMap.get(size));
        diff = radius / 6;
    }

    private void prepareGradient() {
        gradient = new LinearGradient(coors.leftCircle().centerX(), 0, coors.rightCircle().centerX(), 0,
                primeColor, extraColor, Shader.TileMode.CLAMP);
        pathPaint.setShader(gradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec((int) (4 * radius), MeasureSpec.EXACTLY) + getPaddingLeft() + getPaddingRight();
        int height = MeasureSpec.makeMeasureSpec((int) (2 * radius), MeasureSpec.EXACTLY) + getPaddingTop() + getPaddingBottom();
        super.onMeasure(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updatePaint();
        drawLoader(canvas);
    }

    private void drawLoader(Canvas canvas) {
        path.rewind();

        path.addCircle(coors.leftCircle().centerX(), coors.leftCircle().centerY(), coors.leftCircle().width() / 2, Path.Direction.CW);
        path.addCircle(coors.rightCircle().centerX(), coors.rightCircle().centerY(), coors.rightCircle().width() / 2, Path.Direction.CW);

        path.moveTo(coors.topLeft().x, coors.topLeft().y);

        path.quadTo(coors.topInter().x, coors.topInter().y - .7f * diff * coors.abs(),
                coors.topRight().x, coors.topRight().y);
        path.lineTo(coors.botRight().x, coors.botRight().y);

        path.quadTo(coors.botInter().x, coors.botInter().y + .7f * diff * coors.abs(),
                coors.botLeft().x, coors.botLeft().y);
        path.lineTo(coors.topLeft().x, coors.topLeft().y);

        canvas.drawPath(path, pathPaint);
    }

    private void updatePaint() {
        if (gradient == null) {
            prepareGradient();
        }
        gradMatrix.reset();
        gradMatrix.setTranslate(2.5f * radius * (1 - coors.abs()) * (1 - coors.abs()), 0);
        gradMatrix.postRotate(coors.sign() > 0 ? 0 : 180, getWidth() / 2, getHeight() / 2);

        gradient.setLocalMatrix(gradMatrix);
    }
}
