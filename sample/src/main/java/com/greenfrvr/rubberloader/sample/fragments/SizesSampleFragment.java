package com.greenfrvr.rubberloader.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenfrvr.rubberloader.RubberLoaderView;
import com.greenfrvr.rubberloader.sample.R;

import butterknife.Bind;

/**
 * Created by greenfrvr
 */
public class SizesSampleFragment extends BaseFragment {

    protected @Bind(R.id.loader0) RubberLoaderView loaderView0;
    protected @Bind(R.id.loader1) RubberLoaderView loaderView1;
    protected @Bind(R.id.loader2) RubberLoaderView loaderView2;
    protected @Bind(R.id.loader3) RubberLoaderView loaderView3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_sizes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderView0.startLoading();
        loaderView1.startLoading();
        loaderView2.startLoading();
        loaderView3.startLoading();
    }
}
