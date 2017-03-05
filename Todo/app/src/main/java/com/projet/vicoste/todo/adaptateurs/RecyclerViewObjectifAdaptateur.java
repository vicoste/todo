package com.projet.vicoste.todo.adaptateurs;

/**
 * Created by Lou on 04/03/2017.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projet.vicoste.todo.BaseApplication;
import com.projet.vicoste.todo.DescriptionActivity;
import com.projet.vicoste.todo.R;
import com.projet.vicoste.todo.metier.Objectif;

import java.util.List;

public class RecyclerViewObjectifAdaptateur extends RecyclerView.Adapter< RecyclerViewObjectifAdaptateur.ViewHolder> {

    /**
     * Liste de tout les objectifs
     */
    private List<Objectif> objectifs;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewObjectifAdaptateur(List<Objectif> myDataset)
    {
        objectifs = myDataset;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public  RecyclerViewObjectifAdaptateur.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View contactView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_recycler_view_main, parent, false);
        ViewHolder vh = new ViewHolder(contactView);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitreObj.setText(objectifs.get(position).getNom());
        holder.tvDateObj.setText(objectifs.get(position).getDateDebut().getDate() + "/" + objectifs.get(position).getDateDebut().getMonth() + "/" + objectifs.get(position).getDateDebut().getYear());
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
        public final TextView tvTitreObj;
        public final TextView tvDateObj;
        public final ImageButton ibGoToDescription;
        private Context mContext;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Changement activite", "OK");
                mContext.startActivity(new Intent(mContext, DescriptionActivity.class));
            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            tvTitreObj = (TextView) itemView.findViewById(R.id.tv_title_obj_cell_recycler_view);
            tvDateObj = (TextView) itemView.findViewById(R.id.tv_date_obj_cell_recycler_view);
            ibGoToDescription = (ImageButton) itemView.findViewById(R.id.ib_go_description);
            ibGoToDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(v.getContext(),DescriptionActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
