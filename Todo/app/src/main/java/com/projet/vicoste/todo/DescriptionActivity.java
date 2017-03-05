package com.projet.vicoste.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.projet.vicoste.todo.metier.Objectif;

/**
 * Created by Lou on 07/02/2017.
 * Page descriptive d'un objectif précis
 */

public class DescriptionActivity extends AppCompatActivity {

    Objectif objectif;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectif = ((BaseApplication)getApplication()).getObjectifs().get((Integer)(getIntent().getExtras().get("position")));
        setContentView(R.layout.description_layout);
        EditText description = (EditText)findViewById(R.id.et_description_objectif_contenu);
        description.setText(objectif.getDescription());
        TextView date = (TextView) findViewById(R.id.tv_description_objectif_date);
        date.setText(objectif.getDateDebut().getDate() + "/" + objectif.getDateDebut().getMonth() + "/" + objectif.getDateDebut().getYear());
        TextView titre = (TextView)findViewById(R.id.tv_description_objectif_titre);
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
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonValidReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

