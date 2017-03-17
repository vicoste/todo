//PETIT BUG : LA PREMIERE FOIS QUE L'APPLICATION EST LANCÉE, L'ACCES AU CALENDRIER EST DEMANDÉ, ET LA LISTE DES OBJECTIF NE SE CHARGE PAS
package com.projet.vicoste.todo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.projet.vicoste.todo.fragment.SelectCalendarDialogFragment;
import com.projet.vicoste.todo.adaptateurs.RecyclerViewObjectifAdaptater;
import com.projet.vicoste.todo.modele.Objectif;
import com.projet.vicoste.todo.metier.ObjectifManager;


/**
 * Created by Lou on 05/02/2017.
 * Activité basée sur la visualisation de tout les objectifs de l'utilisateur
 */

public class MainActivity extends AppCompatActivity implements RecyclerViewObjectifAdaptater.RecyclerViewObjectifAdaptaterCallback {

    //*******************************PARAMS**********************
    /**
     * constante pour l'écriture dans le calendrier
     */
    private static final int  MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;

    /**
     * Constante pour la recupération du resultat d'une fin d'activite
     */
    private static final int CONSTANT_DESCRIPTION_ACTIVITY = 2;

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
        //sharedPreferences pour savoir si un calendar a été choisi
        //si calendrier choisi, alors l'utiliser. sinon, choisir un nouveau calendrier


        DialogFragment test = new SelectCalendarDialogFragment();
        test.show(getSupportFragmentManager(), "test");


        setContentView(R.layout.main_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (checkForAddEvent()) {
            ObjectifManager.getObjectifs(this);
            mAdapter = new RecyclerViewObjectifAdaptater(ObjectifManager.getObjectifs(this), this);
            getAllCalendars();
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
     * Methode pour quand l'activite reprend la main suite a la fermeture d'une autre
     * @param requestCode
     * @param resultCode code de fin de l'activite
     * @param data donnes complementaires
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONSTANT_DESCRIPTION_ACTIVITY && resultCode == Activity.RESULT_OK) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * verifie que l'utilisateur a la permission requise pour lire le calendrier
     * @return true si la permission est accordée
     */
    private boolean checkForAddEvent(){
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            //DEMANDE DE  : DANS QUEL CALENDRIER L'APPLICATION VA AGIR ?
            return askPermission();
        }
        return true;
    }

    /**
     * Demande la permission pour la lecture dans le calendrier
     * @return
     */
    private boolean askPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_CALENDAR},
                MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    /**
     * Action qui envoit l'id d'un objectif à la descriptionActivity lors du changement de vue
     * @param objectif objectif en question
     */
    @Override
    public void onItemClicked(Objectif objectif) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("position", ObjectifManager.getObjectifs(this).indexOf(objectif));
        startActivityForResult(intent, CONSTANT_DESCRIPTION_ACTIVITY);
    }


    private static String[] eventProjection = new String[]{ CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.CALENDAR_DISPLAY_NAME};
    private void getAllCalendars(){
            @SuppressWarnings("MissingPermission") Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, eventProjection, null, null, CalendarContract.Events.CALENDAR_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                    Log.e("ID CALENDAR ", cursor.getString(0) + "   " + cursor.getString(1));
            }
            cursor.close();
    }

}





