package com.projet.vicoste.todo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.projet.vicoste.todo.adaptateurs.RecyclerViewObjectifAdaptateur;
import com.projet.vicoste.todo.metier.Objectif;
import com.projet.vicoste.todo.metier.ObjectifManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lou on 05/02/2017.
 * Activité basée sur la visualisation de tout les objectifs de l'utilisateur
 */

public class MainActivity extends AppCompatActivity {

    //*******************************PARAMS**********************
    /**
     * code pour l'écriture dans le calendrier
     */
    private static final int WRITE_CALENDAR_REQUEST = 1;

    /**
     * Recycler view où sont affichés tout les objectifs de l'application (master)
     */
    private RecyclerView mRecyclerView;

    /**
     * adaptateur de la recycler view
     */
    private RecyclerView.Adapter mAdapter;

    //*********************************METHODS*********************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectifManager.getObjectifs(this);
        setContentView(R.layout.main_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewObjectifAdaptateur(ObjectifManager.getObjectifs(this));
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton button_add = (FloatingActionButton)findViewById(R.id.fab_main_go_add_goal);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddGoalActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_CALENDAR_REQUEST) {
            mAdapter.notifyDataSetChanged();
        }
    }
}


