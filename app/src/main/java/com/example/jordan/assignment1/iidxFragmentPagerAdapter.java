package com.example.jordan.assignment1;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jordan on 6/11/2017.
 */

public class iidxFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"NORMAL", "HYPER", "ANOTHER"};
    private Context context;
    private String version;
    private int type;

    public iidxFragmentPagerAdapter(FragmentManager fm, Context context, String version, int type) {
        super(fm);
        this.context = context;
        this.version = version;
        this.type = type;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return folderfragment.newInstance(version, position, type);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
