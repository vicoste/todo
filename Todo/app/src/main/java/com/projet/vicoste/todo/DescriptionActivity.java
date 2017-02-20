package com.projet.vicoste.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Lou on 07/02/2017.
 * Page descriptive d'un objectif précis
 */

public class DescriptionActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.definition_layout);
        setListerners();
    }

    /**
     * Methode qui va mettre en place tout les listeners des buttons de la vue
     */
    private void setListerners(){
        Button buttonReturn = (Button) findViewById(R.id.bt_return_main_layout);
        Button buttonValidReturn = (Button) findViewById(R.id.bt_valid_obj);
        Button buttonDeleteReturn = (Button) findViewById(R.id.bt_delete_obj);
        buttonDeleteReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        buttonValidReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

