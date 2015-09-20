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
public class RippleSampleFragment extends BaseFragment {

    @Bind(R.id.loader1) RubberLoaderView rubberLoader1;
    @Bind(R.id.loader2) RubberLoaderView rubberLoader2;
    @Bind(R.id.loader3) RubberLoaderView rubberLoader3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_ripple, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rubberLoader1.startLoading();
        rubberLoader2.startLoading();
        rubberLoader3.startLoading();
    }
}
