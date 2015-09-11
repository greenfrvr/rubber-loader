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
public class IntroFragment extends BaseFragment {

    protected @Bind(R.id.loader) RubberLoaderView loader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loader.startLoading();
    }
}
