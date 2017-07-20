package com.example.jordan.assignment1;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Jordan on 4/22/2017.
 */

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {
    private Menu menu;
    HashMap<String, TextView> clearTexts = new HashMap<String, TextView>();
    private homeFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);

        databaseHelper.getInstance(this);

        //for tabs
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerHome);
        adapter = new homeFragmentPagerAdapter(getSupportFragmentManager(), HomeActivity.this);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsHome);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void updateGoalList() {
        HomeFragment hf = (HomeFragment) adapter.getCurrentFragment();
        hf.updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar_layout, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            if (adapter.getCurrentFragment() != null) {
                ((HomeFragment)adapter.getCurrentFragment()).updateList();
            }
        }
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


