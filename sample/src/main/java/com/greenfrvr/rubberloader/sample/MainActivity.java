package com.greenfrvr.rubberloader.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    protected @Bind(R.id.viewpager) ViewPager viewPager;
    private SamplesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        adapter = new SamplesAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
    }

}
