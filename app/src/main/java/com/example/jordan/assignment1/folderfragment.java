package com.example.jordan.assignment1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import layout.SongFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link folderfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link folderfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class folderfragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "listName";
    private static final String ARG_PARAM2 = "search";
    private static final String ARG_PARAM3 = "type";
    private static final String ARG_PARAM4 = "list";

    // TODO: Rename and change types of parameters
    private String version;
    private String listName;
    private int difficulty;
    private int type;
    private View view;
    private ArrayList<SongItem> songItems = new ArrayList<SongItem>();
    private int currentItem = 0;
    private SongListAdapter adapter;
    private boolean listAdd;
    private boolean search;
    private Cursor cursor;

    private OnFragmentInteractionListener mListener;

    public folderfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment folderfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static folderfragment newInstance(boolean list, String listName, boolean search, int type) {
        folderfragment fragment = new folderfragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM4, list);
        args.putBoolean(ARG_PARAM2, search);
        args.putString(ARG_PARAM1, listName);
        args.putInt(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listAdd = getArguments().getBoolean(ARG_PARAM4);
            listName = getArguments().getString(ARG_PARAM1);
            search = getArguments().getBoolean(ARG_PARAM2);
            type = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_folderfragment, container, false);
        buildSongItemList(cursor);

        //get listview
        final ListView lv = (ListView)view.findViewById(R.id.lst_songs);
        adapter = new SongListAdapter(songItems, getContext(), listName);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongItem song = songItems.get(position);
                String songName = song.getName();
                int songDifficulty = song.getDifficulty();
                int songLevel = Integer.parseInt(song.getLevel());
                int clearValue = song.getClearNum();
                SongFragment sf = SongFragment.newInstance(songName, songDifficulty, songLevel, clearValue);
                sf.show(getActivity().getFragmentManager(), "SongFragment");
                currentItem = position;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //scroll if activity was restarted
        ListView lv = (ListView)view.findViewById(R.id.lst_songs);
        Log.w("Scroll", Integer.toString(currentItem));
        lv.setSelection(currentItem);
    }

    public void updateClear(int newClear) {
        SongItem update = songItems.get(currentItem);
        update.setClearNum(newClear);
        update.setClearText(getResources().getStringArray(R.array.clear_types)[newClear]);
        databaseHelper.updateSongClear(update.getName(), update.getDifficulty(), newClear);
        adapter.updateView(currentItem);
        adapter.notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void Resort() {
        buildSongItemList(cursor);
        ListView lv = (ListView)view.findViewById(R.id.lst_songs);
        adapter = new SongListAdapter(songItems, getContext(), listName);
        lv.setAdapter(adapter);
    }

    private void buildSongItemList(Cursor cursor) {
        songItems.clear();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SongItem item;
            String name = cursor.getString(0);
            String level = Integer.toString(cursor.getInt(2));
            int clearNum = cursor.getInt(1);
            int difficulty = cursor.getInt(3);
            String clearText = getResources().getStringArray(R.array.clear_types)[clearNum];

            item = new SongItem(name, level, clearText, clearNum, difficulty, listAdd);
            songItems.add(item);
            cursor.moveToNext();
        }

        TextView infoTxt = (TextView)view.findViewById(R.id.txt_none_folder);
        ImageView infoImg = (ImageView)view.findViewById(R.id.img_info_folder);

        if (search)
            infoTxt.setText("No results for this search. Please try again.");
        if (cursor.getCount() > 0 || type < 3) {
            infoTxt.setVisibility(View.INVISIBLE);
            infoImg.setVisibility(View.INVISIBLE);
        }
        else {
            infoTxt.setVisibility(View.VISIBLE);
            infoImg.setVisibility(View.VISIBLE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
