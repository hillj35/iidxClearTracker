package com.example.jordan.assignment1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import layout.SongFragment;

public class GoalActivity extends AppCompatActivity implements SongFragment.OnFragmentInteractionListener{
    private ClearTracker clearTracker;
    String listname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        clearTracker = ClearTracker.getInstance();
        final Spinner goalSpinner = (Spinner) findViewById(R.id.spn_goalList);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Goals");

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listname = (String)goalSpinner.getSelectedItem();
                LinearLayout layout = (LinearLayout) findViewById(R.id.lyt_goalList);
                layout.removeAllViews();
                Cursor cursor = databaseHelper.getSongsFromGoalList(listname, 1);
                clearTracker.showSongs(cursor, layout, GoalActivity.this);
                TextView clearText = (TextView) findViewById(R.id.clearedText);
                clearText.setText(databaseHelper.getClearCount(cursor) + " Cleared");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        databaseHelper.updateListSpinner(goalSpinner, this);
    }

    public void goalListAddClicked(View view) {
        //show dialog to enter name
        NewGoalDialogFragment ngdf = new NewGoalDialogFragment();
        ngdf.show(getFragmentManager(), "NewGoalListFragment");
        databaseHelper.updateListSpinner((Spinner)findViewById(R.id.spn_goalList), this);
    }

    public void goalListDeleteClicked(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spn_goalList);
        String selected = (String)spinner.getSelectedItem();
        databaseHelper.deleteList(selected);
        databaseHelper.updateListSpinner(spinner, this);
    }

    public void goalAddSongsClicked(View view) {
        Spinner goalSpinner = (Spinner) findViewById(R.id.spn_goalList);
        String selectedList = (String)goalSpinner.getSelectedItem();
        //create intent for adding to list
        Intent intent = new Intent(this, AddToGoalActivity.class);
        intent.putExtra("listname", listname);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(int newClear){
        //you can leave it empty
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
