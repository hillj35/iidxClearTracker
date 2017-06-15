package com.example.jordan.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jordan on 4/8/2017.
 */

public class Song {
    private TextView clear;
    private int clearInt;
    private View view;
    private Context context;
    private int[] Colors = {Color.TRANSPARENT, Color.GRAY, Color.MAGENTA,
            Color.GREEN, Color.CYAN, Color.RED, Color.YELLOW, Color.BLUE};

    public Song(TextView clearText, int clear, View color, Context context) {
        this.clear = clearText;
        clearInt = clear;
        view = color;
        this.context = context;
    }

    public void updateClear(int newClear) {
        String[] clearValues = context.getResources().getStringArray(R.array.clear_types);
        view.setBackgroundColor(Colors[newClear]);
        clear.setText(clearValues[newClear]);
        clearInt = newClear;
    }

    public int getClearValue() {
        return clearInt;
    }
}
