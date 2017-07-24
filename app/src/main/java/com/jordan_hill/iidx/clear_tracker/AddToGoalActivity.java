package com.jordan_hill.iidx.clear_tracker;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


import layout.SongFragment;

public class AddToGoalActivity extends AppCompatActivity implements SongFragment.OnFragmentInteractionListener, folderfragment.OnFragmentInteractionListener {
    private String selectedVersion = "ALL";
    private String selectedLevel = "ALL";
    private String listName;
    boolean listSearch;
    private folderfragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_goal);

        try {
            listName = getIntent().getStringExtra("listname");
        }
        catch (Exception e) {
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        if (listName == null) {
            ab.setTitle("Search");
        }
        else {
            ab.setTitle(listName);
        }

        listSearch = getIntent().getBooleanExtra("list", false);

        final Spinner levelSpinner = (Spinner) findViewById(R.id.spn_lvl);
        final Spinner styleSpinner = (Spinner) findViewById(R.id.spn_style);
        final Button searchButton = (Button) findViewById(R.id.btn_search);

        levelSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.levelsAll, R.layout.clear_spinner_item));
        styleSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.VersionsAll, R.layout.clear_spinner_item));

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLevel = (String)levelSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVersion = (String) styleSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean normal = ((CheckBox) findViewById(R.id.chk_normal)).isChecked();
                Boolean hyper = ((CheckBox) findViewById(R.id.chk_hyper)).isChecked();
                Boolean another = ((CheckBox) findViewById(R.id.chk_another)).isChecked();
                String match = ((EditText) findViewById(R.id.edt_search)).getText().toString();
                Cursor cursor = databaseHelper.searchSongs(selectedVersion, selectedLevel, normal, hyper, another, match);

                LinearLayout searchResults = (LinearLayout)findViewById(R.id.lyt_search);
                searchResults.removeAllViews();

                displayResults(cursor);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayResults(Cursor cursor) {
        LinearLayout ll = (LinearLayout)findViewById(R.id.lyt_search);
        fragment = folderfragment.newInstance(listSearch, listName, true, 4);
        fragment.setCursor(cursor);
        getSupportFragmentManager().beginTransaction().add(ll.getId(), fragment).commit();
    }


    @Override
    public void onFragmentInteraction(int newClear){
        fragment.updateClear(newClear);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}