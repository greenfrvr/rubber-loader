package com.greenfrvr.rubberloader.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.greenfrvr.rubberloader.sample.fragments.ContactsFragment;
import com.greenfrvr.rubberloader.sample.fragments.GradientSampleFragment;
import com.greenfrvr.rubberloader.sample.fragments.InterpolatorSampleFragment;
import com.greenfrvr.rubberloader.sample.fragments.IntroSampleFragment;
import com.greenfrvr.rubberloader.sample.fragments.RippleSampleFragment;
import com.greenfrvr.rubberloader.sample.fragments.SizesSampleFragment;

/**
 * Created by greenfrvr
 */
public class SamplesAdapter extends FragmentStatePagerAdapter {

    public static final int COUNT = 6;

    private static final SparseArray<String> fragments = new SparseArray<>(COUNT);

    static {
        fragments.put(0, IntroSampleFragment.class.getName());
        fragments.put(1, SizesSampleFragment.class.getName());
        fragments.put(2, GradientSampleFragment.class.getName());
        fragments.put(3, InterpolatorSampleFragment.class.getName());
        fragments.put(4, RippleSampleFragment.class.getName());
        fragments.put(5, ContactsFragment.class.getName());
    }

    private Context context;

    public SamplesAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(context, fragments.get(position));
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
