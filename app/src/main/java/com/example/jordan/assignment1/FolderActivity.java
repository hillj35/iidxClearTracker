package com.example.jordan.assignment1;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import layout.SongFragment;

public class FolderActivity extends AppCompatActivity implements SongFragment.OnFragmentInteractionListener, folderfragment.OnFragmentInteractionListener {
    private ClearTracker clearTracker;
    private String search;
    private int type;
    private folderfragment fragment;
    private iidxFragmentPagerAdapter adapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        search = getIntent().getStringExtra("search");
        type = getIntent().getIntExtra("type", 0);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(search);
        ab.setDisplayHomeAsUpEnabled(true);

        clearTracker = ClearTracker.getInstance();
        clearTracker.clearHashmap();

        Cursor cursor;

        switch (type) {
            case 1:
                //level
                cursor = databaseHelper.getSongsFromLevel(search);
                break;
            case 2:
                //clear
                cursor = databaseHelper.getSongsFromClear(search);
                break;
            default:
                //goals
                cursor = databaseHelper.getSongsFromGoalList(search);
        }

        //for tabs
        if (type == 0) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            adapter = new iidxFragmentPagerAdapter(getSupportFragmentManager(), FolderActivity.this, search, type);
            viewPager.setAdapter(adapter);
            //get preference
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int defaultDifficulty = Integer.parseInt(sp.getString("default_difficulty", "0"));
            viewPager.setCurrentItem(defaultDifficulty);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
        else {
            LinearLayout ll = (LinearLayout)findViewById(R.id.lyt_folder);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            fragment = folderfragment.newInstance();
            fragment.setCursor(cursor);
            getSupportFragmentManager().beginTransaction().add(ll.getId(), fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_action_bar_layout, menu);

        if (type == 3) {
            //goals
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void onFragmentInteraction(int newClear) {
        //get fragment and set new clear
        if (fragment != null) {
            fragment.updateClear(newClear);
        }
        else {
            adapter.getCurrentFragment().updateClear(newClear);
        }
    }


}
