package com.projet.vicoste.todo.adaptateurs;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projet.vicoste.todo.R;
import com.projet.vicoste.todo.modele.Objectif;

import java.util.Calendar;
import java.util.List;

/**
 * Created by logrunner on 14/03/17.
 */


public class ChooseCalendarAdaptater  extends RecyclerView.Adapter< ChooseCalendarAdaptater.ViewHolder> {

    /**
     * Liste de tout les objectifs
     */
    private List<Calendar> calendars;

    /** Provide a suitable constructor (depends on the kind of dataset)*/
    public ChooseCalendarAdaptater(List<Calendar> myDataset) {
        calendars = myDataset;
    }



    /** Create new views (invoked by the layout manager) */
    @Override
    public  ChooseCalendarAdaptater.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        View contactView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_recycler_view_main, parent, false);
        ViewHolder vh = new ViewHolder(contactView);
        return vh;
    }


    /**
     * Replace the contents of a view (invoked by the layout manager)
     * get element from your dataset at this position
     * replace the contents of the view with that element
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitreCalendar.setText(calendars.get(position).toString());
    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return calendars.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // each data item is just a string in this case
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvTitreCalendar;
        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            tvTitreCalendar = (TextView) itemView.findViewById(R.id.tv_title_calendar_cell_recycler_view);
        }
    }

}

