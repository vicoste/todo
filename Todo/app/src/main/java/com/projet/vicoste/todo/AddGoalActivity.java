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

/**
 * Created by Lou on 07/02/2017.
 * Activité basée sur l'ajout d'un objectif par l'utilisateur
 */

public class AddGoalActivity extends AppCompatActivity {

    //***********************************PARAMS********************************
    /**
     * Id du calendrier principal du téléphone
     */
    private static final int PRINCIPAL_CALENDAR_ID = 1;

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

    /**
     * Objectif qui va être ajouté par l'utilisateur
     */
    private Objectif objectif;



    //**********************************METHODS***************************
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
                creerObjectif();
            }
        });
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

    /**
     *methode permettant de creer et d'ajouter l'objectif definit par l'utilisateur à la liste de tout les objectifs.
     *  - Si l'objectif est jugé comme acceptable ( si le titre est bien validé et que la date n'est pas inférieure à celle du jour) alors il est ajouté en tant
     *    qu'évenement dans le calendrier principal de l'utilisateur
     *  - Sinon, un message informatif sur l'erreur apparaît et on revient là où on en était avant l'essai de la création d'un objectif
     */
    public void creerObjectif(){
        Date date = new Date(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
        if(!validationTitre(nom.getText().toString())){ Toast.makeText(getBaseContext(), "Titre invalide", Toast.LENGTH_SHORT).show(); return;}
        if(date.getTime() < (new Date()).getTime()){ Toast.makeText(getBaseContext(), "Date invalide", Toast.LENGTH_SHORT).show(); return; }
        objectif= new Objectif( -1,nom.getText().toString(),description.getText().toString(), date, null);
        checkForAddEvent();
        //objectif.getDateDebut().setMonth(objectif.getDateDebut().getMonth()+1);
        //objectif.getDateFin().setMonth(objectif.getDateFin().getMonth()+1);
        ObjectifManager.getObjectifs(this).add(objectif);
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
            insertIntoCalendar();
            Toast.makeText(getBaseContext(), "Bon courage !", Toast.LENGTH_SHORT).show();
            createValidateNotification();
            finish();
        }
    }

    /**
     * Methode d'insertion dans le calendar principal du mobile
     */
    private void insertIntoCalendar() {
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(objectif.getDateDebut().getYear(), objectif.getDateDebut().getMonth(), objectif.getDateDebut().getDate(), objectif.getDateDebut().getHours(), objectif.getDateDebut().getMinutes());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(objectif.getDateFin().getYear(), objectif.getDateFin().getMonth(), objectif.getDateFin().getDate(), objectif.getDateFin().getHours(), objectif.getDateFin().getMinutes());
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
        Log.e("INSERTION ", String.valueOf(uri.getPathSegments().get(1)));
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    insertIntoCalendar();
                    Toast.makeText(getBaseContext(), "Bon courage !", Toast.LENGTH_SHORT).show();
                    createValidateNotification();
                    finish();
                }
                return;
            }
        }
    }

}
