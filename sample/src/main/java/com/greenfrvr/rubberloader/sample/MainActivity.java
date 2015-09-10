package com.greenfrvr.rubberloader.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.greenfrvr.rubberloader.RubberLoaderView;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.loader) RubberLoaderView loaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        loaderView.startLoading();
    }

}
