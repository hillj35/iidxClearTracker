package com.example.jordan.assignment1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import layout.SongFragment;

public class ClearTracker {
    private SQLiteDatabase iidxdb;
    private HashMap<View, Song> songsByLayout = new HashMap<View, Song>();
    private int[] Colors = {Color.TRANSPARENT, Color.GRAY, Color.MAGENTA,
            Color.GREEN, Color.CYAN, Color.RED, Color.YELLOW, Color.BLUE};

    private Song currentSong;

    private static ClearTracker instance;

    public static ClearTracker getInstance() {
        if (instance == null)
            instance = new ClearTracker();
        return instance;
    }

    private ClearTracker() {
        iidxdb = databaseHelper.dbInstance;
    }

    public static void populateDatabase(Context context, SQLiteDatabase db) {
        Resources res = context.getResources();
        String[] songArray = res.getStringArray(R.array.songs);
        //create an entry in songs for each version

        for (String s : songArray) {
            Log.w("Line", s);
            String[] attributes = s.split(",");
            ContentValues values = new ContentValues();
            int difNormal = 0;
            int difHyper = 0;
            int difAnother = 0;
            try {
                difNormal = Integer.parseInt(attributes[2]);
                values.put(iidxContract.songEntry.COLUMN_NAME_SONGNAME, attributes[1]);
                values.put(iidxContract.songEntry.COLUMN_NAME_VERSION, attributes[0]);
                values.put(iidxContract.songEntry.COLUMN_NAME_LEVEL, difNormal);
                values.put(iidxContract.songEntry.COLUMN_NAME_DIFFICULTY, 0);
                values.put(iidxContract.songEntry.COLUMN_NAME_CLEAR, 0);
                db.insert(iidxContract.songEntry.TABLE_NAME, null, values);
            } catch (NumberFormatException e) {
            }
            try {
                difHyper = Integer.parseInt(attributes[3]);
                values.put(iidxContract.songEntry.COLUMN_NAME_SONGNAME, attributes[1]);
                values.put(iidxContract.songEntry.COLUMN_NAME_VERSION, attributes[0]);
                values.put(iidxContract.songEntry.COLUMN_NAME_LEVEL, difHyper);
                values.put(iidxContract.songEntry.COLUMN_NAME_DIFFICULTY, 1);
                values.put(iidxContract.songEntry.COLUMN_NAME_CLEAR, 0);
                db.insert(iidxContract.songEntry.TABLE_NAME, null, values);
            } catch (NumberFormatException e) {}
            try {
                difAnother = Integer.parseInt(attributes[4]);
                values.put(iidxContract.songEntry.COLUMN_NAME_SONGNAME, attributes[1]);
                values.put(iidxContract.songEntry.COLUMN_NAME_VERSION, attributes[0]);
                values.put(iidxContract.songEntry.COLUMN_NAME_LEVEL, difAnother);
                values.put(iidxContract.songEntry.COLUMN_NAME_DIFFICULTY, 2);
                values.put(iidxContract.songEntry.COLUMN_NAME_CLEAR, 0);
                db.insert(iidxContract.songEntry.TABLE_NAME, null, values);
            } catch (NumberFormatException e) {}
        }
    }

    public void updateSongClear(String songName, int difficulty, int newClear) {
        Log.w("updating", Integer.toString(newClear));

        ContentValues values = new ContentValues();
        values.put(iidxContract.songEntry.COLUMN_NAME_CLEAR, newClear);

        String selection = iidxContract.songEntry.COLUMN_NAME_SONGNAME + " LIKE ? AND " + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + " = ?";
        String[] selectionArgs = {songName, Integer.toString(difficulty)};

        int count = iidxdb.update(iidxContract.songEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateClearView(int newClear) {
        currentSong.updateClear(newClear);
    }

    public void clearHashmap() {
        songsByLayout.clear();
    }


    public void showSongs(final Cursor cursor, final LinearLayout ll, final Context context) {
        //log
        Log.w("showingsongs", "showSongs: ");

        //navigate cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //song elements
            final String songName = cursor.getString(0);
            final int songDifficulty = cursor.getInt(3);
            final int songLevel = cursor.getInt(2);
            final int clear = cursor.getInt(1);

            //layout object instantiations
            View clearColor = new View(context);
            TextView level = new TextView(context);
            TextView songNameText = new TextView(context);
            TextView clearText = new TextView(context);
            LinearLayout ln = new LinearLayout(context);
            final LinearLayout horizontalLayout = new LinearLayout(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lpMatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            String[] clears = context.getResources().getStringArray(R.array.clear_types);

            //general padding
            int pad = dpToPxl(6, context);

            //clear color setup
            clearColor.setLayoutParams(new ViewGroup.LayoutParams(25, ViewGroup.LayoutParams.MATCH_PARENT));
            clearColor.setBackgroundColor(Colors[clear]);

            //level set setup
            level.setText(Integer.toString(songLevel));
            level.setTextSize(35);
            level.setWidth(dpToPxl(50, context));
            level.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            level.setGravity(Gravity.CENTER_VERTICAL);
            level.setLayoutParams(lpMatch);

            //song name text setup
            songNameText.setText(cursor.getString(0));
            songNameText.setTextSize(20);
            songNameText.setLayoutParams(lp);
            songNameText.setGravity(Gravity.CENTER_VERTICAL);
            songNameText.setPadding(pad, pad, pad, 0);

            //clear text setup
            clearText.setText(clears[clear]);
            clearText.setTextSize(16);
            clearText.setPadding(pad, 0, pad, pad);

            //layout for name/clear
            ln.setOrientation(LinearLayout.VERTICAL);
            ln.setLayoutParams(lp);
            ln.addView(songNameText);
            ln.addView(clearText);


            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.addView(clearColor);
            horizontalLayout.addView(level);
            horizontalLayout.addView(ln);

            //song object to hold important stuff
            Song song = new Song(clearText, clear, clearColor, context);

            songsByLayout.put(horizontalLayout, song);
            ll.addView(horizontalLayout);

            horizontalLayout.setOnTouchListener(new AdapterView.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent e) {
                    switch(e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            horizontalLayout.setBackgroundColor(Color.LTGRAY);
                            return true;
                        case MotionEvent.ACTION_UP:
                            //clicked, open fragment
                            currentSong = songsByLayout.get(view);
                            SongFragment sf = SongFragment.newInstance(songName, songDifficulty, songLevel, currentSong.getClearValue());
                            sf.show(((Activity)context).getFragmentManager(), "SongFragment");
                        default:
                            horizontalLayout.setBackgroundColor(Color.TRANSPARENT);
                            return true;
                    }
                }
            });

            cursor.moveToNext();
        }
    }


    private int dpToPxl(int dp, Context c) {
        float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
