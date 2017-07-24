package com.jordan_hill.iidx.clear_tracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Jordan on 6/12/2017.
 */

public class homeFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] {"Version", "Level", "Clear", "Lists"};
    private Context context;
    private Fragment currentFragment;
    private int curPage;

    public homeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public int getCurrentPage() {
        return curPage;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = (Fragment) object;
        }
        curPage = position;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        int id;
        switch (position) {
            case 0:
                id = R.array.Versions;
                break;
            case 1:
                id = R.array.levels;
                break;
            case 2:
                id = R.array.clear_types;
                break;
            default:
                id = -1;
                break;
        }
        return HomeFragment.newInstance(id, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
