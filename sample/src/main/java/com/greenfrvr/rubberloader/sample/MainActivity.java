package com.greenfrvr.rubberloader.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.greenfrvr.rubberloader.RubberLoaderView;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.loader1) RubberLoaderView loaderView1;
    @Bind(R.id.loader2) RubberLoaderView loaderView2;
    @Bind(R.id.loader3) RubberLoaderView loaderView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        loaderView1.algorythm(RubberLoaderView.MODE_2_CUBIC);
        loaderView1.startLoading(500);

        loaderView2.algorythm(RubberLoaderView.MODE_4_CUBIC);
        loaderView2.startLoading(500);

        loaderView3.algorythm(RubberLoaderView.MODE_2_QUADS);
        loaderView3.startLoading(500);
    }

}
