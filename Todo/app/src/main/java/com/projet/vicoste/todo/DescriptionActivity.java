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
import com.projet.vicoste.todo.metier.ObjectifManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lou on 07/02/2017.
 * Page descriptive d'un objectif précis
 */

public class DescriptionActivity extends AppCompatActivity {

    //***************************PARAMS**********************************
    /**
     * représente le code requis pour l'envoit de notifications
     */
    private static final int REQUEST_CODE_NOTIFICATION = 0;

    /**
     * Objectif vu en détail sur cette vue
     */
    private Objectif objectif;

    /**
     * EditText qui va contenir la description de l'objectif sur laquelle est basée la vue.
     * C'est ici que pourra être modifié le contenu de l'objectif
     */
    private EditText description;

    /**
     * TextView qui vont simplement contenir la date de début de l'événement et le titre de celui-ci
     */
    private TextView date, titre;


    //****************************METHODS*******************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectif = (ObjectifManager.getObjectifs(this).get((Integer)(getIntent().getExtras().get("position"))));
        setContentView(R.layout.description_layout);
        description = (EditText)findViewById(R.id.et_description_objectif_contenu);
        description.setText(objectif.getDescription());
        date = (TextView) findViewById(R.id.tv_description_objectif_date);
        date.setText(objectif.getDateDebut().getDate() + "/" + objectif.getDateDebut().getMonth() + "/" + objectif.getDateDebut().getYear());
        titre = (TextView)findViewById(R.id.tv_description_objectif_titre);
        titre.setText(objectif.getNom());
        setListerners();
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
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, objectif.getId());
        int rows = getContentResolver().delete(deleteUri, null, null);
        //Log.e("SUPPRESION DE ", objectif.toString());
        //boolean x = ObjectifManager.getObjectifs(this).remove(objectif);
        //Log.e("SUPPRESION DE ", String.valueOf(x));
    }

    /**
     * Methode qui va mettre à jour l'evenement correspondant a l'objectif dans le calendrier principal
     */
    private void updateObjInCalendar(){
        objectif.setDescription(description.getText().toString());
        ContentValues values = new ContentValues();
        Uri updateUri = null;
        values.put(CalendarContract.Events.DESCRIPTION, description.getText().toString());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, objectif.getId());
        getContentResolver().update(updateUri, values, null, null); //retourne le nombre de rows updated
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

