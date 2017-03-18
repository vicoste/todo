package com.projet.vicoste.todo.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.projet.vicoste.todo.R;
/**
 * Created by vicoste on 15/03/17.
 */
public class ValidateDeleteFragment extends DialogFragment {
    private int state =0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.app_name)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        state =1;
                    }
                })
                .setNegativeButton("retour", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        state = 2;
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public int getState() {
        return state;

    }
}
