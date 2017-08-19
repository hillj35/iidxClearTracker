package com.jordan_hill.iidx.clear_tracker;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Jordan on 4/22/2017.
 */

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener {
    private Menu menu;
    HashMap<String, TextView> clearTexts = new HashMap<String, TextView>();
    private homeFragmentPagerAdapter adapter;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);

        databaseHelper.getInstance(this);


        adapter = new homeFragmentPagerAdapter(getSupportFragmentManager(), HomeActivity.this);

        checkIfUpdate();
    }

    private int getVersionNumber(Context context) {
        try {
            ComponentName cn = new ComponentName(context, HomeActivity.class);
            PackageInfo pi = context.getPackageManager().getPackageInfo(cn.getPackageName(), 0);
            return pi.versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            Log.w("exception", "smh");
            return -1;

        }
    }

    private void checkIfUpdate() {
        int version = getVersionNumber(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int storedVersion = prefs.getInt("version", -1);
        if (version > storedVersion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("What's New?");
            builder.setMessage(getResources().getString(R.string.update_notes));
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog ad = builder.create();
            ad.show();
        }

        prefs.edit().putInt("version", version).apply();
    }

    public void updateGoalList() {
        HomeFragment hf = (HomeFragment) adapter.getCurrentFragment();
        hf.updateList();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar_layout, menu);
        if (adapter.getCurrentPage() == 4)
            menu.getItem(1).setVisible(true);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        //for tabs
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerHome);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (position == 4) {
                    //goals page
                    menu.getItem(1).setVisible(true);
                }
                else
                    menu.getItem(1).setVisible(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (adapter != null) {
            if (adapter.getCurrentFragment() != null) {
                ((HomeFragment)adapter.getCurrentFragment()).updateList();
            }
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsHome);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId) {
            case R.id.app_bar_search:
                intent = new Intent(this, AddToGoalActivity.class);
                intent.putExtra("list", false);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.app_bar_add:
                NewGoalDialogFragment ngdf = new NewGoalDialogFragment();
                ngdf.show(getFragmentManager(), "NewGoalListFragment");
                break;
        }
        return true;
    }
}


