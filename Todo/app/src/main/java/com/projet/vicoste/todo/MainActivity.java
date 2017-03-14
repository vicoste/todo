package com.projet.vicoste.todo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.projet.vicoste.todo.adaptateurs.RecyclerViewObjectifAdaptateur;
import com.projet.vicoste.todo.metier.Objectif;
import com.projet.vicoste.todo.metier.ObjectifManager;


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
    private static final int  MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
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


        setContentView(R.layout.main_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (checkForAddEvent()) {
            ObjectifManager.getObjectifs(this);
            mAdapter = new RecyclerViewObjectifAdaptateur(ObjectifManager.getObjectifs(this));
        }
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

    /**
     * verifie que l'utilisateur a la permission requise pour lire le calendrier
     * @return true si la permission est accordée
     */
    private boolean checkForAddEvent(){
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return askPermission();

        } else {
            return true;
        }
    }

    private boolean askPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_CALENDAR},
                MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }



}





