package com.jordan_hill.iidx.clear_tracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jordan_hill.iidx.clear_tracker.ClearTracker;
import com.jordan_hill.iidx.clear_tracker.R;

public class MultiEditFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";

    private View view;

    private OnFragmentInteractionListener mListener;

    public MultiEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultiEditFragment newInstance() {
        MultiEditFragment fragment = new MultiEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_multi_edit, null);
        builder.setView(view);
        builder.setTitle("Multi Edit Clears")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Spinner spn = (Spinner)view.findViewById(R.id.spn_clear_multi);
                        Spinner spnScore = (Spinner)view.findViewById(R.id.spn_score_multi);
                        CheckBox chkClear = (CheckBox)view.findViewById(R.id.chk_clear);
                        CheckBox chkScore = (CheckBox)view.findViewById(R.id.chk_score);

                        int newClear = -1;
                        int newScore = -1;
                        if (chkClear.isChecked())
                            newClear = spn.getSelectedItemPosition();
                        if (chkScore.isChecked())
                            newScore = spnScore.getSelectedItemPosition();
                        mListener.onMultiInteraction(newClear, newScore);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        Log.w("Test", "On start");
        //array values
        String[] clearValues = getResources().getStringArray(R.array.clear_types);
        //init spinner values
        final Spinner clearSpinner = (Spinner)view.findViewById(R.id.spn_clear_multi);
        final Spinner scoreSpinner = (Spinner)view.findViewById(R.id.spn_score_multi);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.clear_types, R.layout.clear_spinner_item);
        clearSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.scores, R.layout.clear_spinner_item);
        scoreSpinner.setAdapter(adapter);

        CheckBox chkClear = (CheckBox)view.findViewById(R.id.chk_clear);
        CheckBox chkScore = (CheckBox)view.findViewById(R.id.chk_score);

        chkClear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    clearSpinner.setVisibility(View.VISIBLE);
                else
                    clearSpinner.setVisibility(View.INVISIBLE);
            }
        });

        chkScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    scoreSpinner.setVisibility(View.VISIBLE);
                else
                    scoreSpinner.setVisibility(View.INVISIBLE);
            }
        });

        super.onStart();

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
        void onMultiInteraction(int newClear, int newScore);
    }
}
