package com.projet.vicoste.todo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projet.vicoste.todo.metier.Objectif;

import java.util.List;

/**
 * Created by vicoste on 17/02/17.
 * Adaptateur de la RecyclerView
 */
public class MainRecyclerViewAdapteur extends RecyclerView.Adapter<MainRecyclerViewAdapteur.ViewHolder> {

    private List<Objectif> objectifs;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainRecyclerViewAdapteur(List<Objectif> myDataset) {
        objectifs = myDataset;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MainRecyclerViewAdapteur.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textview.setText(objectifs.get(position).getNom() + " à faire avant le " + objectifs.get(position).getDate().getDay() + " " + objectifs.get(position).getDate().getMonth() + " " + objectifs.get(position).getDate().getYear());
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return objectifs.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // each data item is just a string in this case
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textview;

        public ViewHolder(LinearLayout v) {
            super(v);
            textview = (TextView) v.findViewById(R.id.tv_recycler_view);
        }
    }

}
