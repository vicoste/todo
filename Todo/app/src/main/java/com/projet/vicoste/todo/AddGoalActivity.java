package com.projet.vicoste.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.test.mock.MockApplication;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.projet.vicoste.todo.metier.Objectif;

import java.util.Date;

/**
 * Created by Lou on 07/02/2017.
 * Activité basée sur l'ajout d'un objectif par l'utilisateur
 */

public class AddGoalActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOTIFICATION = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_layout);

        if (getIntent() != null) {

        }
    }

    /**
     * Methode permettant de de revenir à l'écran d'acceuil sans prendre compte des informations qui ont été saisie (ou non)
     * @param sender
     */
    public void onClick_return (View sender){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Methode permettant de valider le nouvel objectif saisie par l'utilisateur et le faire revenir à l'écran d'acceuil suite à un enregistrement de ses nouvelles données
     * @param sender
     */
    public void onClick_valid_new_obj (View sender){

        if (creerObjectif()) {
            Intent intent = new Intent(this,MainActivity.class);
            Toast.makeText(getBaseContext(), "Bon courage !", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            createValidateNotification();
        }
    }

    /*
        Methode qui cree une notification lors de la creation d'un objectif
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
        //if(date.getDay() < (new Date()).getDay()){ Toast.makeText(getBaseContext(), "Date invalide", Toast.LENGTH_SHORT).show(); return false; }

        Objectif o = new Objectif(nom.getText().toString(),description.getText().toString(), date);
        Log.d("OBJECTIF :",o.toString());
        ((BaseApplication) getApplication()).add(o);
        return true;

    }


    /*return true si le titre est correct
        return false si il est vide
     */
    public boolean validationTitre(String titre){

        return (!titre.isEmpty() && (titre.replace(" ", "").length() > 1));
    }
}
