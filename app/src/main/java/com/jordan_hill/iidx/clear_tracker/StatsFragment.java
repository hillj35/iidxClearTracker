package com.jordan_hill.iidx.clear_tracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private View view;
    private Cursor stats;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
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
        view = inflater.inflate(R.layout.fragment_stats, null);
        builder.setView(view);
        builder.setTitle("Stats")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        calculateStats();
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
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
    
    public void setCursor(Cursor c) {
        stats = c;
    }
    
    private void calculateStats() {
        int[] nums = new int[]{0,0,0,0,0,0,0,0};
        double[] percents = new double[8];
        int total = stats.getCount();
        if (stats != null) {
            stats.moveToFirst();
            while (!stats.isAfterLast()) {
                int clear = stats.getInt(1);
                nums[clear]++;
                stats.moveToNext();
            }
        }

        //calculate percents
        for (int i = 0; i < 8; i++) {
            percents[i] = ((double)nums[i] / (double)total) * 100;
        }
        
        //set all text views to values
        TextView fcNum = (TextView)view.findViewById(R.id.txt_fc_num);
        TextView exNum = (TextView)view.findViewById(R.id.txt_ex_num);
        TextView hardNum = (TextView)view.findViewById(R.id.txt_hard_num);
        TextView normalNum = (TextView)view.findViewById(R.id.txt_normal_num);
        TextView easyNum = (TextView)view.findViewById(R.id.txt_ec_num);
        TextView aeNum = (TextView)view.findViewById(R.id.txt_ae_num);
        TextView failNum = (TextView)view.findViewById(R.id.txt_fail_num);
        TextView npNum = (TextView)view.findViewById(R.id.txt_np_num);

        TextView fcPct = (TextView)view.findViewById(R.id.txt_fc_pct);
        TextView exPct = (TextView)view.findViewById(R.id.txt_ex_pct);
        TextView hardPct = (TextView)view.findViewById(R.id.txt_hard_pct);
        TextView normalPct = (TextView)view.findViewById(R.id.txt_normal_pct);
        TextView easyPct = (TextView)view.findViewById(R.id.txt_ec_pct);
        TextView aePct = (TextView)view.findViewById(R.id.txt_ae_pct);
        TextView failPct = (TextView)view.findViewById(R.id.txt_fail_pct);
        TextView npPct = (TextView)view.findViewById(R.id.txt_np_pct);

        fcNum.setText(Integer.toString(nums[7]));
        fcPct.setText(String.format("%1$.2f%%", percents[7]));
        exNum.setText(Integer.toString(nums[6]));
        exPct.setText(String.format("%1$.2f%%", percents[6]));
        hardNum.setText(Integer.toString(nums[5]));
        hardPct.setText(String.format("%1$.2f%%", percents[5]));
        normalNum.setText(Integer.toString(nums[4]));
        normalPct.setText(String.format("%1$.2f%%", percents[4]));
        easyNum.setText(Integer.toString(nums[3]));
        easyPct.setText(String.format("%1$.2f%%", percents[3]));
        aeNum.setText(Integer.toString(nums[2]));
        aePct.setText(String.format("%1$.2f%%", percents[2]));
        failNum.setText(Integer.toString(nums[1]));
        failPct.setText(String.format("%1$.2f%%", percents[1]));
        npNum.setText(Integer.toString(nums[0]));
        npPct.setText(String.format("%1$.2f%%", percents[0]));

        TextView txtTotal = (TextView)view.findViewById(R.id.txt_total_num);
        txtTotal.setText(Integer.toString(total));

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
