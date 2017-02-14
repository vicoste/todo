package com.projet.vicoste.todo.modele;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.projet.vicoste.todo.R;

/**
 * Created by Lou on 07/02/2017.
 * Activité basée sur l'ajout d'un objectif par l'utilisateur
 */

public class AddGoalActivity extends AppCompatActivity {
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.add_goal_layout);
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_layout);
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
        Intent intent = new Intent(this,MainActivity.class);
        Toast.makeText(getBaseContext(), "Bon courage !", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


    private void createNotification(){
        //Notification notification = new Notification.Builder().setContentTitle("Vous avait fini un objectif !").setContentText("Felicitation !").build();
    }
}
