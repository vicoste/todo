package com.projet.vicoste.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.projet.vicoste.todo.metier.Objectif;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lou on 07/02/2017.
 * Activité basée sur l'ajout d'un objectif par l'utilisateur
 */

public class AddGoalActivity extends AppCompatActivity {

    private static final int  MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
    private static final int REQUEST_CODE_NOTIFICATION = 0;

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private EditText description, nom;
    private DatePicker datePicker;
    private Objectif objectif;


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
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Bonne chance")
                        .setContentText("Clapiticlapiticlap")
                .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(42, mBuilder.build());
    }



    /**
     *methode permettant de creer et d'ajouter l'objectif defini par l'utilisateur a la liste de tout les objectifs
     * @return true si l'objectif a pû etre cree, false sinon
     */
    public boolean creerObjectif(){
        Date date = new Date(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());

        if(!validationTitre(nom.getText().toString())){ Toast.makeText(getBaseContext(), "Titre invalide", Toast.LENGTH_SHORT).show(); return false;}
        if(date.getDate() < (new Date()).getDate()){ Toast.makeText(getBaseContext(), "Date invalide", Toast.LENGTH_SHORT).show(); return false; }

        objectif= new Objectif(nom.getText().toString(),description.getText().toString(), date, null);
        checkForAddEvent();
        ((BaseApplication)getApplication()).add(objectif);
        return true;

    }




    /**return true si le titre est correct
     *  @return false si il est vide
     */
    private boolean validationTitre(String titre){
        return (!titre.isEmpty() && (titre.replace(" ", "").length() > 1));
    }




    /**
     * Methode qui va verifier les autorisations pour ajouter un evenement dans le calendrier google
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
     * Methode d'insertion dans le calendar principal
     */
    private void insertIntoCalendar() {
        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(objectif.getDateDebut().getYear(), objectif.getDateDebut().getMonth(), objectif.getDateDebut().getDate(), objectif.getDateDebut().getHours() + 1, objectif.getDateDebut().getMinutes());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(objectif.getDateFin().getYear(), objectif.getDateFin().getMonth(), objectif.getDateFin().getDate(), objectif.getDateFin().getHours()+2, objectif.getDateFin().getMinutes());
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, objectif.getNom());
        values.put(CalendarContract.Events.DESCRIPTION, objectif.getDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe");
        @SuppressWarnings("MissingPermission") Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }


    /**
     * Query the calendar
     */
    private void queryCalendar() {
        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
        String[] selectionArgs = new String[]{"inf63.iut.uda@gmail.com", "com.gmail"};
        // Submit the query and get a Cursor object back.
        //noinspection MissingPermission
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
        }
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
