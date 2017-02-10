package com.projet.vicoste.todo.modele;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.projet.vicoste.todo.R;

/**
 * Created by Lou on 07/02/2017.
 */

public class DescriptionActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.definition_layout);
    }

    /**
     * Methode permettant de se rediriger vers la vue d'acceuil sans rien faire d'autre
     * @param sender
     */
    public void onClick_return (View sender){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Methode permettant de se diriger vers l'écran d'acceuil en validant l'objectif
     * @param sender
     */
    public void onClick_valid_and_return (View sender){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Methode permettant de se diriger vers l'écran d'acceuil en supprimant l'objectif
     * @param sender
     */
    public void onClick_delete_and_return (View sender){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
