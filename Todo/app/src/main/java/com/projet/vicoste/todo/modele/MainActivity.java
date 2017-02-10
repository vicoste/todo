package com.projet.vicoste.todo.modele;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.projet.vicoste.todo.modele.AddGoalActivity;
import com.projet.vicoste.todo.R;

/**
 * Created by Lou on 05/02/2017.
 * Activité basée sur la visualisation de tout les objectifs de l'utilisateur
 */

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.main_layout);
    }

    /**
     * Methode permettant de se diriger vers l'écran d'ajout d'un nouvel objectif
     * @param sender
     */
    public void onClick_AddGoal (View sender){
        Intent intent = new Intent(this,AddGoalActivity.class);
        startActivity(intent);
    }
}


