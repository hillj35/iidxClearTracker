package com.example.jordan.assignment1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import layout.DeleteFragment;
import layout.SongFragment;
import layout.SortFragment;

public class FolderActivity extends AppCompatActivity implements SongFragment.OnFragmentInteractionListener, folderfragment.OnFragmentInteractionListener,
                                                                        SortFragment.OnFragmentInteractionListener, DeleteFragment.OnFragmentInteractionListener,
                                                                        StatsFragment.OnFragmentInteractionListener {
    private ClearTracker clearTracker;
    private String search;
    private int type;
    private folderfragment fragment;
    private iidxFragmentPagerAdapter adapter;
    private Menu menu;
    private int sortValue;
    private boolean goal = false;
    private Cursor cursor;

    private String[] clearTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        clearTypes = getResources().getStringArray(R.array.clear_types);

        search = getIntent().getStringExtra("search");
        type = getIntent().getIntExtra("type", 0);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        clearTracker = ClearTracker.getInstance();
        clearTracker.clearHashmap();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sortValue = Integer.parseInt(sp.getString("default_sort", "0"));

        switch (type) {
            case 0:
                ab.setTitle(search);
                cursor = null;
                break;
            case 1:
                //level
                cursor = databaseHelper.getSongsFromLevel(search, sortValue);
                ab.setTitle("Level " + search);
                break;
            case 2:
                //clear
                cursor = databaseHelper.getSongsFromClear(search, sortValue);
                int index = Integer.parseInt(search);
                if (index > 1) {
                    ab.setTitle(clearTypes[index] + " Clear");
                }
                else
                    ab.setTitle(clearTypes[index]);
                break;
            default:
                //goals
                cursor = databaseHelper.getSongsFromGoalList(search, sortValue);
                ab.setTitle(search);
                goal = true;
        }



        //for tabs
        if (type == 0) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            adapter = new iidxFragmentPagerAdapter(getSupportFragmentManager(), FolderActivity.this, search, type, sortValue);
            viewPager.setAdapter(adapter);
            //get preference
            int defaultDifficulty = Integer.parseInt(sp.getString("default_difficulty", "0"));
            viewPager.setCurrentItem(defaultDifficulty);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
        else {
            LinearLayout ll = (LinearLayout)findViewById(R.id.lyt_folder);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            fragment = folderfragment.newInstance(false, "", false, type);
            fragment.setCursor(cursor);
            getSupportFragmentManager().beginTransaction().add(ll.getId(), fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onSortInteraction(sortValue);
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
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        else if (id == R.id.menu_list_sort) {
            SortFragment sf = SortFragment.newInstance(sortValue);
            sf.show(this.getSupportFragmentManager(), "SortFragment");
        }
        else if (id == R.id.menu_list_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this list? This cannot be undone!");
            builder.setTitle("Delete List?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onDeleteInteraction();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog ad = builder.create();
            ad.show();
        }
        else if (id == R.id.menu_list_edit) {
            Intent i = new Intent(this, AddToGoalActivity.class);
            i.putExtra("listname", search);
            i.putExtra("list", true);
            startActivity(i);
        }
        else if(id == R.id.menu_list_stats) {
            onSortInteraction(sortValue);
            StatsFragment sf = new StatsFragment();
            if (type == 0) {
                folderfragment ff = adapter.getCurrentFragment();
                cursor = ff.getCursor();
            }
            else {
                cursor = fragment.getCursor();
            }
            sf.setCursor(cursor);
            sf.show(getSupportFragmentManager(), "StatsFragment");
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
        //sorted by clear, update
        if (sortValue == 2)
            onSortInteraction(sortValue);
    }

    @Override
    public void onDeleteInteraction() {
        databaseHelper.deleteList(search);
        onBackPressed();
    }

    @Override
    public void onSortInteraction(int sort) {
        sortValue = sort;
        Cursor cursor;

        if (adapter != null)
            adapter.resortFragments(sortValue);

        switch (type) {
            case 0:
                //version
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                cursor = databaseHelper.getSongsFromVersion(search, viewPager.getCurrentItem(), sortValue);
                break;
            case 1:
                //level
                cursor = databaseHelper.getSongsFromLevel(search, sortValue);
                break;
            case 2:
                //clear
                cursor = databaseHelper.getSongsFromClear(search, sortValue);
                break;
            default:
                //goals
                cursor = databaseHelper.getSongsFromGoalList(search, sortValue);
        }

        if (type > 0) {
            fragment.setCursor(cursor);
            fragment.Resort();
        }
        else {
            if (adapter.getCurrentFragment() != null) {
                adapter.getCurrentFragment().setCursor(cursor);
                adapter.getCurrentFragment().Resort();
            }
        }
    }
}
