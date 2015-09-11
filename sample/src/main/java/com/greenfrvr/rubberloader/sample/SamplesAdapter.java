package com.greenfrvr.rubberloader.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.greenfrvr.rubberloader.sample.fragments.IntroFragment;

/**
 * Created by greenfrvr
 */
public class SamplesAdapter extends FragmentStatePagerAdapter {

    public static final int COUNT = 1;

    private static final SparseArray<String> fragments = new SparseArray<>(COUNT);

    static {
        fragments.put(0, IntroFragment.class.getName());
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
