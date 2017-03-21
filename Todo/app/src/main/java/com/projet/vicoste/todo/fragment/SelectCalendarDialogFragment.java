package com.projet.vicoste.todo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;


import com.projet.vicoste.todo.metier.CalendarManager;
import com.projet.vicoste.todo.modele.Calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by logrunner on 14/03/17.
 * Dialog pour le choix d'un calendrier
 */
public class SelectCalendarDialogFragment extends DialogFragment {
    private static String[] calendars_str;
    private static List<String> calendars_nonConvert = new ArrayList<>();

    public interface NoticeDialogListener{
        public void onDialogCalendarClick(Calendar calendar);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final List<Calendar> calendars = CalendarManager.getCalendars(getContext());
        for (Calendar c : calendars){
            if(!calendars_nonConvert.contains(c.toString())){
                calendars_nonConvert.add(c.toString());
            }
        }
        calendars_str = Arrays.copyOf(calendars_nonConvert.toArray(), calendars_nonConvert.toArray().length, String[].class);

        builder.setTitle("Choisir son calendrier.")
               .setItems( calendars_str, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogCalendarClick(calendars.get(which));
                     }});
        return builder.create();
    }

}
