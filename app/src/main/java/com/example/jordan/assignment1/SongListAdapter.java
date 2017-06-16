package com.example.jordan.assignment1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    }

    public void updateView(int position){
        SongItem song = data.get(position);
        song.setClears();
    }

    public SongListAdapter(ArrayList<SongItem> data, Context context) {
        super(context, R.layout.song_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongItem song = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView  == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.song_item_name);
            viewHolder.level = (TextView) convertView.findViewById(R.id.song_item_level);
            viewHolder.clear = (TextView) convertView.findViewById(R.id.song_item_clear);
            viewHolder.clearColor = convertView.findViewById(R.id.clearColor);

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

        song.setClearText(viewHolder.clear);
        song.setClearColorView(viewHolder.clearColor);

        return convertView;
    }
}
