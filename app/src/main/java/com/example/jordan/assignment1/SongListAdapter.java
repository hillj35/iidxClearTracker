package com.example.jordan.assignment1;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jordan.assignment1.R;
import com.example.jordan.assignment1.SongItem;

import java.util.ArrayList;

/**
 * Created by Jordan on 6/15/2017.
 */

public class SongListAdapter extends ArrayAdapter<SongItem> implements View.OnClickListener {
    private ArrayList<SongItem> data;

    Context context;
    int colors[] = {Color.TRANSPARENT, Color.GRAY, Color.MAGENTA,
            Color.GREEN, Color.CYAN, Color.RED, Color.YELLOW, Color.BLUE};

    private static class ViewHolder {
        TextView name;
        TextView level;
        TextView clear;
        View clearColor;
        Button addToList;
    }

    private String listName;

    public void updateView(int position){
        SongItem song = data.get(position);
        song.setClears();
    }

    public SongListAdapter(ArrayList<SongItem> data, Context context, String listName) {
        super(context, R.layout.song_item, data);
        this.data = data;
        this.context = context;
        this.listName = listName;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SongItem song = getItem(position);
        final ViewHolder viewHolder;
        final View result;

        if (convertView  == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.song_item_name);
            viewHolder.level = (TextView) convertView.findViewById(R.id.song_item_level);
            viewHolder.clear = (TextView) convertView.findViewById(R.id.song_item_clear);
            viewHolder.clearColor = convertView.findViewById(R.id.clearColor);
            viewHolder.addToList = (Button) convertView.findViewById(R.id.btn_list_add);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText(song.getName());
        viewHolder.level.setText(song.getLevel());
        viewHolder.clear.setText(song.getClearText());
        viewHolder.clearColor.setBackgroundColor(colors[song.getClearNum()]);

        if (song.getList()) {
            viewHolder.addToList.setVisibility(View.VISIBLE);
            if (databaseHelper.isInList(song.getName(), song.getDifficulty(), listName)) {
                viewHolder.addToList.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_delete));
                viewHolder.addToList.setTag(android.R.drawable.ic_delete);
            }
            else {
                viewHolder.addToList.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_input_add));
                viewHolder.addToList.setTag(android.R.drawable.ic_input_add);
            }

            viewHolder.addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((int)v.getTag() == android.R.drawable.ic_input_add) {
                        databaseHelper.addGoalItem(song.getName(), Integer.toString(song.getDifficulty()), listName);
                        v.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_delete));
                    }
                    else {
                        databaseHelper.deleteGoalItem(song.getName(), Integer.toString(song.getDifficulty()), listName);
                        v.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_input_add));
                    }
                }
            });
        }
        else
            viewHolder.addToList.setVisibility(View.INVISIBLE);

        song.setClearText(viewHolder.clear);
        song.setClearColorView(viewHolder.clearColor);

        return convertView;
    }
}
