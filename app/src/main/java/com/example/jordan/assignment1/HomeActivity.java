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
    private static HomeActivity instance;

    HashMap<String, TextView> clearTexts = new HashMap<String, TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instance == null)
            instance = this;
        setContentView(R.layout.home_activity);

        databaseHelper.getInstance(this);

        //for tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerHome);
        viewPager.setAdapter(new homeFragmentPagerAdapter(getSupportFragmentManager(), HomeActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsHome);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static HomeActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar_layout, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.app_bar_search:
                Intent intent = new Intent(this, AddToGoalActivity.class);
                startActivity(intent);
                break;

        }

        return true;
    }
}


