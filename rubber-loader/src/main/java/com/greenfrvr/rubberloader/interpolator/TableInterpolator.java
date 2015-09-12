package com.greenfrvr.rubberloader.interpolator;


import android.view.animation.Interpolator;

/**
 * Created by greenfrvr
 */
public class TableInterpolator implements Interpolator {

    private final float[] mValues;
    private final float mStepSize;

    public TableInterpolator(float[] values) {
        mValues = values;
        mStepSize = 1f / (mValues.length - 1);
    }

    @Override
    public float getInterpolation(float input) {
        if (input >= 1.0f) {
            return 1.0f;
        }
        if (input <= 0f) {
            return 0f;
        }

        // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
        // we lerp (linearly interpolate) in the return statement
        int position = Math.min((int) (input * (mValues.length - 1)), mValues.length - 2);

        // Calculate values to account for small offsets as the lookup table has discrete values
        float quantized = position * mStepSize;
        float diff = input - quantized;
        float weight = diff / mStepSize;

        // Linearly interpolate between the table values
        return mValues[position] + weight * (mValues[position + 1] - mValues[position]);
    }


}
