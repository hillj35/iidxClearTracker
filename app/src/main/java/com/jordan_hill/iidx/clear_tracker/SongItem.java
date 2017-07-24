package com.jordan_hill.iidx.clear_tracker;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jordan on 6/15/2017.
 */

public class SongItem {
    private String name;
    private String level;
    private int clearNum;
    private String clearText;
    private int difficulty;
    private boolean listAdd;
    private Context context;

    private TextView clear;
    private View clearColor;

    private int[] colors = {Color.TRANSPARENT, Color.GRAY, Color.MAGENTA,
            Color.GREEN, Color.CYAN, Color.RED, Color.YELLOW, Color.BLUE};

    public SongItem(String name, String level, String clearText, int clearNum, int difficulty, boolean listAdd) {
        this.name = name;
        this.level = level;
        this.clearText = clearText;
        this.clearNum = clearNum;
        this.difficulty = difficulty;
        this.listAdd = listAdd;
        this.context = context;
    }

    public String getName() {
        return name;
    }
    public String getLevel() {
        return level;
    }
    public String getClearText() {
        return clearText;
    }
    public int getClearNum() {
        return clearNum;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public boolean getList() { return listAdd; }

    public void setClearText(String value) {
        clearText = value;
    }
    public void setClearNum(int value) {
        clearNum = value;
    }
    public void setClearText(TextView value) {
        clear = value;
    }
    public void setClearColorView(View value) {
        clearColor = value;
    }

    public void setClears() {
        clear.setText(getClearText());
        clearColor.setBackgroundColor(colors[getClearNum()]);
    }
}