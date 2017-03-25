//PETIT BUG : LA PREMIERE FOIS QUE L'APPLICATION EST LANCÉE, L'ACCES AU CALENDRIER EST DEMANDÉ, ET LA LISTE DES OBJECTIF NE SE CHARGE PAS
package com.projet.vicoste.todo;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.NotificationCompat;
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

import java.util.Date;


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
     * code requis pour l'envoit de notifications
     */
    private static final int REQUEST_CODE_NOTIFICATION = 0;

    /**
     * Recycler view où sont affichés tout les objectifs de l'application (master)
     */
    private RecyclerView mRecyclerView;

    /**
     * adaptateur de la recycler view
     */
    private RecyclerView.Adapter mAdapter;

    /**
     * ID du calendrier enregistré dans les sharedPreferences // ID choisit par l'utilisateur
     */
    private int calendarID;

    /**
     * Variable permettant de retenir si la permission a été accordé ou non.
     * Variable mise en place suite à un bug d'android.
     * http://stackoverflow.com/questions/33264031/calling-dialogfragments-show-from-within-onrequestpermissionsresult-causes
     */
    private Boolean hasPermission = false;


    //*********************************METHODS*********************

    /**
     * OnCreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarID = getPreferences(MODE_PRIVATE).getInt(getString(R.string.calendarPreferences), -1);
        setContentView(R.layout.main_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
        FloatingActionButton button_add = (FloatingActionButton)findViewById(R.id.fab_main_go_add_goal);
        FloatingActionButton button_settings = (FloatingActionButton) findViewById(R.id.fab_main_settings);
        askValidation();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForAddEvent()) {
                    Intent intent = new Intent(v.getContext(), AddGoalActivity.class);
                    intent.putExtra("CALENDAR", calendarID);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getBaseContext(), "Il va peut-être falloir accepter les droits mon petit.", Toast.LENGTH_LONG).show();
                }
            }
        });
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForAddEvent()){
                    DialogFragment dialogSelectCalendar = new SelectCalendarDialogFragment();
                    dialogSelectCalendar.show(getSupportFragmentManager(), "SelectCalendarFragment");
                }
            }
        });

        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                askValidation();
            }
        });

    }

    /**
     * OnResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission && calendarID == -1){
            DialogFragment dialogSelectCalendar = new SelectCalendarDialogFragment();
            dialogSelectCalendar.show(getSupportFragmentManager(), "SelectCalendarFragment");
        }
    }

    /**
     * Met a jour les objectifs si les droits le permettent
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (checkForAddEvent()) {
                //ObjectifManager.updateObjectifs(this, calendarID);
                mAdapter = new RecyclerViewObjectifAdaptater(ObjectifManager.getObjectifs(this, calendarID), this);
                mRecyclerView.setAdapter(mAdapter);
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
        //Log.e("Request code ::", String.valueOf(requestCode));
        //Log.e("Result code ::", String.valueOf(resultCode) + " Egal : " + String.valueOf(resultCode == Activity.RESULT_OK));
        if (requestCode == CONSTANT_DESCRIPTION_ACTIVITY && resultCode == Activity.RESULT_OK) {
                ObjectifManager.updateObjectifs(this, calendarID);
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

    /**
     * Methode donnant les resultats de la permission pour ecrire/lire dans le calendrier
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermission = true;
//                    if (calendarID == -1) {
//                        DialogFragment dialogSelectCalendar = new SelectCalendarDialogFragment();
//                        dialogSelectCalendar.show(getSupportFragmentManager(), "SelectCalendarFragment");
//                    }
                }
                break;
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
     * @param calendar calendar cliqué par l'utilisateur
     */
    @Override
    public void onDialogCalendarClick(Calendar calendar) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.calendarPreferences), calendar.getId());
        editor.commit();
        calendarID = getPreferences(MODE_PRIVATE).getInt(getString(R.string.calendarPreferences), -1);
        if (checkForAddEvent()) {
            ObjectifManager.updateObjectifs(this, calendarID);
            mAdapter = new RecyclerViewObjectifAdaptater(ObjectifManager.getObjectifs(this, calendarID), this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * demande a l'utilisateur si il a validé son obectif
     */
    private void askValidation(){
        if (checkForAddEvent()) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            int i = 0;
            for (Objectif objectif : ObjectifManager.getObjectifs(this, calendarID)) {
                if (objectif.isEnded()) {
                    i++;
                    createNotif(objectif, "cet objectif est terminé, l'avez-vous reussi ?", i);
                }
            }
        }

    }

    /**
     * Methode qui creer une notification qui renverra sur la description de l'objection concerné
     * @param objectif Objectif concerné par la notification
     * @param textContent Contenu que va contenir la notification
     * @param id ID de la notification
     */
    private void createNotif(Objectif objectif,String textContent,int id){
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("position", ObjectifManager.getObjectifs(this, calendarID).indexOf(objectif));
        intent.putExtra("CALENDAR", calendarID);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_NOTIFICATION,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.clock)
                        .setContentTitle(objectif.getNom())
                        .setContentText(textContent)
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }
}




