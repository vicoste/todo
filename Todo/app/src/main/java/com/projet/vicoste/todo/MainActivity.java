//PETIT BUG : LA PREMIERE FOIS QUE L'APPLICATION EST LANCÉE, L'ACCES AU CALENDRIER EST DEMANDÉ, ET LA LISTE DES OBJECTIF NE SE CHARGE PAS
package com.projet.vicoste.todo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.projet.vicoste.todo.fragment.SelectCalendarDialogFragment;
import com.projet.vicoste.todo.adaptateurs.RecyclerViewObjectifAdaptater;
import com.projet.vicoste.todo.modele.Calendar;
import com.projet.vicoste.todo.modele.Objectif;
import com.projet.vicoste.todo.metier.ObjectifManager;


/**
 * Created by Lou on 05/02/2017.
 * Activité basée sur la visualisation de tout les objectifs de l'utilisateur
 */

public class MainActivity extends AppCompatActivity implements RecyclerViewObjectifAdaptater.RecyclerViewObjectifAdaptaterCallback, SelectCalendarDialogFragment.NoticeDialogListener {

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

    private int calendarID;

    private  boolean hasPermission = false;

    //*********************************METHODS*********************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarID = getPreferences(MODE_PRIVATE).getInt(getString(R.string.calendarPreferences), -1);

        //Log.e("ID sharedPref MAIN", String.valueOf(calendarID));
        //sharedPreferences pour savoir si un calendar a été choisi
        //si calendrier choisi, alors l'utiliser. sinon, choisir un nouveau calendrier
        setContentView(R.layout.main_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
        if (checkForAddEvent()) {
            ObjectifManager.getObjectifs(this, calendarID);
            mAdapter = new RecyclerViewObjectifAdaptater(ObjectifManager.getObjectifs(this, calendarID), this);
            mRecyclerView.setAdapter(mAdapter);
        }
        FloatingActionButton button_add = (FloatingActionButton)findViewById(R.id.fab_main_go_add_goal);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForAddEvent()) {
                    Intent intent = new Intent(v.getContext(), AddGoalActivity.class);
                    intent.putExtra("CALENDAR", calendarID);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getBaseContext(), "Il va peut-être falloir accepter les droits mon petit.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * OnResume (android bug in request permission)
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("OK", "OKKKK");
        if(hasPermission && calendarID == -1){
            DialogFragment dialogSelectCalendar = new SelectCalendarDialogFragment();
            dialogSelectCalendar.show(getSupportFragmentManager(), "SelectCalendarFragment");
        }

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
            return askPermission();
        }
        return true;
    }

    /**
     * Demande la permission pour la lecture dans le calendrier
     * @return true si permission accordée, false sinon
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i=0 ; i<permissions.length ; ++i){
            if (permissions[i].equals(android.Manifest.permission.WRITE_CALENDAR) &&
                    grantResults[i] == 0 &&
                    calendarID == -1){
                hasPermission = true;
            }
        }
    }

    /**
     * Action qui envoit l'id d'un objectif à la descriptionActivity lors du changement de vue
     * @param objectif objectif en question
     */
    @Override
    public void onItemClicked(Objectif objectif) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("position", ObjectifManager.getObjectifs(this, calendarID).indexOf(objectif));
        intent.putExtra("CALENDAR", calendarID);
        startActivityForResult(intent, CONSTANT_DESCRIPTION_ACTIVITY);
    }


    /**
     * Methode appelée lors d'un clic sur un calendrier proposé dans le dialog de sélection
     * @param calendar
     */
    @Override
    public void onDialogCalendarClick(Calendar calendar) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.calendarPreferences), calendar.getId());
        boolean result = editor.commit();
        calendarID = getPreferences(MODE_PRIVATE).getInt(getString(R.string.calendarPreferences), -1);
        if (checkForAddEvent()) {
            ObjectifManager.getObjectifs(this, calendarID);
            mAdapter = new RecyclerViewObjectifAdaptater(ObjectifManager.getObjectifs(this, calendarID), this);
            mRecyclerView.setAdapter(mAdapter);
        }
        //System.out.println(calendarID);
    }
}





