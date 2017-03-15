package com.projet.vicoste.todo.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import com.projet.vicoste.todo.R;

/**
 * Created by logrunner on 14/03/17.
 */
public class SelectCalendarDialogFragment extends DialogFragment {
    private static String[] test = new String[]{"test1", "test2", "test3"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Instanciation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.select_calendar_dialog, null))
                .setTitle("Choisir son calendrier.")
                .setPositiveButton("Valider.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.e("Validation", "true");
                    }
                });
                /*
                .setItems(test, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               Log.e("Dans le On Clickkkkk", "true");
           }})*/

        //get from create()
        return builder.create();
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = ()
        }
    }*/
}
