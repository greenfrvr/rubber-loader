package com.greenfrvr.rubberloader.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    protected @Bind(R.id.viewpager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SamplesAdapter adapter = new SamplesAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
    }

}
