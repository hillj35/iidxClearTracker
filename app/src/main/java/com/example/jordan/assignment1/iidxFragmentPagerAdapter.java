package com.example.jordan.assignment1;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Jordan on 6/11/2017.
 */

public class iidxFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"NORMAL", "HYPER", "ANOTHER"};
    private Context context;
    private String version;
    private int type;
    private int sortValue;
    private folderfragment currentFragment;

    public iidxFragmentPagerAdapter(FragmentManager fm, Context context, String version, int type, int sortValue) {
        super(fm);
        this.context = context;
        this.version = version;
        this.type = type;
        this.sortValue = sortValue;
    }

    public folderfragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = (folderfragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        folderfragment fragment = folderfragment.newInstance(false, "", false);
        Cursor cursor = databaseHelper.getSongsFromVersion(version, position, sortValue);
        fragment.setCursor(cursor);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
