package com.example.jordan.assignment1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Jordan on 5/30/2017.
 */

public class NewGoalDialogFragment extends DialogFragment {
    EditText et;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_goal_name, null);
        builder.setView(view);
        et = (EditText) view.findViewById(R.id.edt_goalName);
        builder.setTitle("New List");
        builder.setMessage("Enter a name for your new list")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (et.getText().toString().trim().equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "No list name entered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            databaseHelper.addList(et.getText().toString());
                            ((HomeActivity) getActivity()).updateGoalList();
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}
