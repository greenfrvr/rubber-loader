package com.greenfrvr.rubberloader.sample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    protected @Bind(R.id.viewpager) ViewPager viewPager;
    protected @Bind(R.id.pager_indicator) TextView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SamplesAdapter adapter = new SamplesAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        indicator.setText("1/" + viewPager.getAdapter().getCount());
        indicator.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    public void onPageSelected(int position) {
        indicator.setText((position + 1) + "/" + viewPager.getAdapter().getCount());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
