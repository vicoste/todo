package com.projet.vicoste.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.projet.vicoste.todo.modele.Objectif;
import com.projet.vicoste.todo.metier.ObjectifManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Lou on 07/02/2017.
 * Activité basée sur l'ajout d'un objectif par l'utilisateur
 */

public class AddGoalActivity extends AppCompatActivity {

    //***********************************PARAMS********************************
    /**
     * Id du calendrier que l'utilisateur utilise
     */
    private static int PRINCIPAL_CALENDAR_ID;

    /**
     * numéro caractérisant la permission d'écrire dans le calendrier
     */
    private static final int  MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;

    /**
     * code requis pour l'envoit de notifications
     */
    private static final int REQUEST_CODE_NOTIFICATION = 0;

    /**
     * description et nom sont des editText qui vont respectivement correspondre aux valeurs suivante : la description/contenu de l'objectif que
     * l'utilisateur souhaite se fixer, et le nom qu'il aura donné à cet objectif
     */
    private EditText description, nom;

    /**
     * DatePicker sur lequel on va récupérer la date où l'utilisateur souhaite placer son événement
     */
    private DatePicker datePicker;



    //**********************************METHODS***************************

    /**
     * Methode de création de l'activite
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_layout);
        FloatingActionButton addButton = (FloatingActionButton)findViewById(R.id.bt_valid_new_obj);
        description = (EditText) findViewById(R.id.et_contenu_new_obj);
        nom = (EditText) findViewById(R.id.titre_new_obj);
        datePicker = (DatePicker) findViewById(R.id.dp_date_new_obj);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForAddEvent();
            }
        });
        PRINCIPAL_CALENDAR_ID = (Integer)(getIntent().getExtras().get("CALENDAR"));
    }

    /**
     * Methode qui cree une notification lors de la creation d'un objectif
     */
    private void createValidateNotification(){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_NOTIFICATION,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.clock)
                        .setContentTitle("Bonne chance")
                        .setContentText("Clapiticlapiticlap")
                .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(42, mBuilder.build());
    }


    /**return true si le titre est correct
     *  @return false si il est vide, true si ok
     */
    private boolean validationTitre(String titre){
        return (!titre.isEmpty() && (titre.replace(" ", "").length() > 1));
    }

    /**
     * Methode qui va verifier les autorisations pour ajouter un evenement dans le calendrier google
     * Si celles si sont à jour, alors on va insérer l'événement dans le calendrier, afficher un toast d'encouragement et envoyer une notification à l'utilisateur
     * pour ensuite revenir sur la vue principale
     */
    private void checkForAddEvent(){
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        } else {
            doJob();
        }
    }

    /**
     * Methode d'insertion dans le calendar principal du mobile
     */
    private void insertIntoCalendar(Objectif objectif) {
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, objectif.getNom());
        values.put(CalendarContract.Events.DESCRIPTION, objectif.getDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, PRINCIPAL_CALENDAR_ID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe");
        @SuppressWarnings("MissingPermission") Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        objectif.setId(Integer.valueOf(uri.getPathSegments().get(1)));
    }

    /**
     * Methode de vérification des permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doJob();
                }
                return;
            }
        }
    }

    private void doJob() {
        Date date = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth()).getTime();
        if(!validationTitre(nom.getText().toString())){ Toast.makeText(getBaseContext(), "Titre invalide", Toast.LENGTH_SHORT).show(); return;}
        if(date.getTime() < (new Date()).getTime()){ Toast.makeText(getBaseContext(), "Date invalide", Toast.LENGTH_SHORT).show(); return; }
        Objectif objectif= new Objectif( -1,nom.getText().toString(),description.getText().toString(), date, null);
        insertIntoCalendar(objectif);
        ObjectifManager.getObjectifs(this, PRINCIPAL_CALENDAR_ID).add(objectif);
        Toast.makeText(getBaseContext(), "Bon courage !", Toast.LENGTH_SHORT).show();
        createValidateNotification();
        finish();
    }



}
