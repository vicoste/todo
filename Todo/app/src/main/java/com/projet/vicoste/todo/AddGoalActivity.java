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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.test.mock.MockApplication;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.projet.vicoste.todo.metier.Objectif;

import java.util.Calendar;
import java.util.Date;
import java.util.jar.Manifest;

/**
 * Created by Lou on 07/02/2017.
 * Activité basée sur l'ajout d'un objectif par l'utilisateur
 */

public class AddGoalActivity extends AppCompatActivity {

    private static final int  MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
    private static final int REQUEST_CODE_NOTIFICATION = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_layout);
        if (getIntent() != null) {

        }

        Button backButton = (Button)findViewById(R.id.bt_return_main_layout);
        Button addButton = (Button)findViewById(R.id.bt_valid_new_obj);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creerObjectif()) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    Toast.makeText(getBaseContext(), "Bon courage !", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    createValidateNotification();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
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
        EditText description = (EditText) findViewById(R.id.et_contenu_new_obj);
        EditText nom = (EditText) findViewById(R.id.titre_new_obj);
        DatePicker datePicker = (DatePicker) findViewById(R.id.dp_date_new_obj);
        Date date = new Date(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());

        if(!validationTitre(nom.getText().toString())){ Toast.makeText(getBaseContext(), "Titre invalide", Toast.LENGTH_SHORT).show(); return false;}
        if(date.getDate() < (new Date()).getDate()){ Toast.makeText(getBaseContext(), "Date invalide", Toast.LENGTH_SHORT).show(); return false; }

        Objectif o = new Objectif(nom.getText().toString(),description.getText().toString(), date, null);
        addEvent(o);
        Log.d("OBJECTIF :",o.toString());
        ((BaseApplication) getApplication()).add(o);
        return true;

    }


    /**return true si le titre est correct
     *  @return false si il est vide
     */
    private boolean validationTitre(String titre){
        return (!titre.isEmpty() && (titre.replace(" ", "").length() > 1));
    }


    /**
     * Methode qui va ajouter un evenement dans le calendrier google
     */
    private void addEvent(Objectif objectif){
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_CALENDAR)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
            }
        } else {
            long calID = 3;
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(objectif.getDateDebut().getYear(), objectif.getDateDebut().getMonth(), objectif.getDateDebut().getDay(), objectif.getDateDebut().getHours(), objectif.getDateDebut().getMinutes());
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(objectif.getDateFin().getYear(), objectif.getDateFin().getMonth(), objectif.getDateFin().getDay(), objectif.getDateFin().getHours(), objectif.getDateFin().getMinutes());
            endMillis = endTime.getTimeInMillis();

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, objectif.getNom());
            values.put(CalendarContract.Events.DESCRIPTION, objectif.getDescription());
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //permission refusée
                }
                return;
            }
        }
    }

}
