package layout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jordan_hill.iidx.clear_tracker.ClearTracker;
import com.jordan_hill.iidx.clear_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_DIFFICULTY = "difficulty";
    private static final String ARG_CLEAR = "clear";
    private static final String ARG_LEVEL = "level";

    // TODO: Rename and change types of parameters
    private String songName;
    private int songDifficulty;
    private int clearType;
    private int level;
    private ClearTracker clearTracker;
    private View view;

    private OnFragmentInteractionListener mListener;

    public SongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongFragment newInstance(String name, int difficulty, int level, int clear) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_DIFFICULTY, difficulty);
        args.putInt(ARG_CLEAR, clear);
        args.putInt(ARG_LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            Log.w("create", "onCreate: ");
            songName = getArguments().getString(ARG_NAME);
            songDifficulty = getArguments().getInt(ARG_DIFFICULTY);
            clearType = getArguments().getInt(ARG_CLEAR);
            level = getArguments().getInt(ARG_LEVEL);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_song, null);
        builder.setView(view);
        builder.setTitle("Edit Clear")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Spinner spn = (Spinner)view.findViewById(R.id.spn_clear);
                        mListener.onFragmentInteraction(spn.getSelectedItemPosition());
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
        clearTracker = ClearTracker.getInstance();
        //array values
        String[] clearValues = getResources().getStringArray(R.array.clear_types);
        //init spinner values
        Spinner clearSpinner = (Spinner)view.findViewById(R.id.spn_clear);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.clear_types, R.layout.clear_spinner_item);
        clearSpinner.setAdapter(adapter);
        clearSpinner.setSelection(clearType);

        TextView name = (TextView)view.findViewById(R.id.txt_SongName);
        name.setText(songName);

        TextView difficultytxt = (TextView)view.findViewById(R.id.txt_Difficulty);
        String difficultyString = "";

        //TextView clearTxt = (TextView)view.findViewById(R.id.txt_ClearType);
        //clearTxt.setText(clearValues[clearType]);
        switch (songDifficulty) {
            case 0:
                difficultyString = "NORMAL " + level;
                difficultytxt.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorNormal));
                break;
            case 1:
                difficultyString = "HYPER " + level;
                difficultytxt.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorHyper));
                break;
            default:
                difficultyString = "ANOTHER " + level;
                difficultytxt.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorAnother));
                break;
        }
        difficultytxt.setText(difficultyString);

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
        void onFragmentInteraction(int newClear);
    }
}
