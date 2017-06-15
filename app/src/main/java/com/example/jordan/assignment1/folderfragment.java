package com.example.jordan.assignment1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


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
    private static final String ARG_PARAM1 = "version";
    private static final String ARG_PARAM2 = "difficulty";
    private static final String ARG_PARAM3 = "type";

    // TODO: Rename and change types of parameters
    private String version;
    private int difficulty;
    private int type;
    private ClearTracker clearTracker = ClearTracker.getInstance();

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
    public static folderfragment newInstance(String version, int difficulty, int type) {
        folderfragment fragment = new folderfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, version);
        args.putInt(ARG_PARAM2, difficulty);
        args.putInt(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            version = getArguments().getString(ARG_PARAM1);
            difficulty = getArguments().getInt(ARG_PARAM2);
            type = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folderfragment, container, false);
        Cursor cursor;
        switch (type) {
            case 0:
                //version
                cursor = databaseHelper.getSongsFromVersion(version, difficulty);
                break;
            case 1:
                //level
                cursor = databaseHelper.getSongsFromLevel(version);
                break;
            case 2:
                //clear
                cursor = databaseHelper.getSongsFromClear(version);
                break;
            default:
                //goals
                cursor = databaseHelper.getSongsFromGoalList(version);
        }
        clearTracker.showSongs(cursor, (LinearLayout)view.findViewById(R.id.lyt_songs), getActivity());
        return view;
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
