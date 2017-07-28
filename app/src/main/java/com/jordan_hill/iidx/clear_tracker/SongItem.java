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
    private String scoreString;
    private int clearNum;
    private int score;
    private String clearText;
    private int difficulty;
    private boolean listAdd;

    private TextView clear;
    private TextView scoreTxt;
    private View clearColor;

    private int[] colors = {Color.TRANSPARENT, Color.GRAY, Color.MAGENTA,
            Color.GREEN, Color.CYAN, Color.RED, Color.YELLOW, Color.BLUE};

    public SongItem(String name, String level, String clearText, String scoreText, int clearNum, int difficulty, int score, boolean listAdd) {
        this.name = name;
        this.level = level;
        this.clearText = clearText;
        this.clearNum = clearNum;
        this.difficulty = difficulty;
        this.listAdd = listAdd;
        this.score = score;
        this.scoreString = scoreText;
    }

    public String getName() {
        return name;
    }
    public String getLevel() {
        return level;
    }
    public String getScoreText() {return scoreString;}
    public String getClearText() {
        return clearText;
    }
    public int getClearNum() {
        return clearNum;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public int getScore() {return score;}
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
    public void setScoreText(TextView value) { scoreTxt = value; }
    public void setScoreText(String value) {scoreString = value;}
    public void setClearColorView(View value) {
        clearColor = value;
    }
    public void setClearScore(int value) {score = value;}

    public void setClears() {
        clear.setText(getClearText());
        scoreTxt.setText(getScoreText());
        clearColor.setBackgroundColor(colors[getClearNum()]);
    }
}
