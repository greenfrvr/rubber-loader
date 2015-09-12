package com.greenfrvr.rubberloader.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.greenfrvr.rubberloader.RubberLoaderView;
import com.greenfrvr.rubberloader.interpolator.PulseInterpolator;
import com.greenfrvr.rubberloader.interpolator.PulseInverseInterpolator;
import com.greenfrvr.rubberloader.sample.R;

import butterknife.Bind;

/**
 * Created by greenfrvr
 */
public class InterpolatorSampleFragment extends BaseFragment {

    protected @Bind(R.id.loader1) RubberLoaderView loaderView1;
    protected @Bind(R.id.loader2) RubberLoaderView loaderView2;
    protected @Bind(R.id.loader3) RubberLoaderView loaderView3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interpolator_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderView1.setInterpolator(new PulseInterpolator());
        loaderView1.startLoading();

        loaderView2.setInterpolator(new LinearInterpolator());
        loaderView2.startLoading();

        loaderView3.setInterpolator(new PulseInverseInterpolator());
        loaderView3.startLoading();
    }
}
