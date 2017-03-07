package com.projet.vicoste.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.projet.vicoste.todo.metier.Objectif;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lou on 07/02/2017.
 * Page descriptive d'un objectif précis
 */

public class DescriptionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NOTIFICATION = 0;
    private static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private Objectif objectif;
    private EditText description;
    private TextView date, titre;
    private long eventID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectif = ((BaseApplication)getApplication()).getObjectifs().get((Integer)(getIntent().getExtras().get("position")));
        setContentView(R.layout.description_layout);
        description = (EditText)findViewById(R.id.et_description_objectif_contenu);
        description.setText(objectif.getDescription());
        date = (TextView) findViewById(R.id.tv_description_objectif_date);
        date.setText(objectif.getDateDebut().getDate() + "/" + objectif.getDateDebut().getMonth() + "/" + objectif.getDateDebut().getYear());
        titre = (TextView)findViewById(R.id.tv_description_objectif_titre);
        titre.setText(objectif.getNom());
        researchCorrespondingEventID();
        setListerners();
        Log.d("EVENT ID", toString().valueOf(eventID));
    }

    /**
     * Methode qui va mettre en place tout les listeners des buttons de la vue
     */
    private void setListerners(){
        FloatingActionButton buttonValidReturn = (FloatingActionButton) findViewById(R.id.bt_description_valid_obj);
        FloatingActionButton buttonDeleteReturn = (FloatingActionButton) findViewById(R.id.bt_description_delete_obj);
        buttonDeleteReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteObjInCalendar();
                createValidateNotification();
                finish();
            }
        });
        buttonValidReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateObjInCalendar();
                finish();
            }
        });
    }

    /**
     * Methode qui va supprimer l'evenement correspondant à l'objectif dans le calendrier principal
     */
    private void deleteObjInCalendar(){
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = getContentResolver().delete(deleteUri, null, null);
        Log.d("Rows deleted: ", toString().valueOf(rows));
    }

    /**
     * Methode qui va mettre à jour l'evenement correspondant a l'objectif dans le calendrier principal
     */
    private void updateObjInCalendar(){
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri = null;
        values.put(CalendarContract.Events.DESCRIPTION, description.getText().toString());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        getContentResolver().update(updateUri, values, null, null); //retourne le nombre de rows updated
    }


    /**
     * Methode qui va rechercher l'evenement dans le calendrier afin d'en retirer son ID
     */
    private void researchCorrespondingEventID(){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(objectif.getDateDebut().getYear(), objectif.getDateDebut().getMonth(), objectif.getDateDebut().getDate(), objectif.getDateDebut().getHours()+1, objectif.getDateDebut().getMinutes());
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(objectif.getDateFin().getYear(), objectif.getDateFin().getMonth(), objectif.getDateFin().getDate(), objectif.getDateFin().getHours()+2, objectif.getDateFin().getMinutes());
        long endMillis = endTime.getTimeInMillis();
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        String selection = CalendarContract.Instances.TITLE + " = ?"; //construct the query
        String[] selectionArgs = new String[] {objectif.getNom() };
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);
        cur =  cr.query(builder.build(), //submit the query
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                null);
        while (cur.moveToNext()) {
            eventID = cur.getLong(PROJECTION_ID_INDEX);
        }
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
                        .setContentTitle("Quel talent !")
                        .setContentText("Encore un objectif de réussit ? Tu ne t'arrêtes plus !")
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(42, mBuilder.build());
    }


}

