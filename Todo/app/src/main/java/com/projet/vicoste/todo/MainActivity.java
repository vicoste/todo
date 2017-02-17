package com.projet.vicoste.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.projet.vicoste.todo.metier.Objectif;

import java.util.ArrayList;

/**
 * Created by Lou on 05/02/2017.
 * Activité basée sur la visualisation de tout les objectifs de l'utilisateur
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MainRecyclerViewAdapteur (((BaseApplication) getApplication()).getObjectifs());
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Methode permettant de se diriger vers l'écran d'ajout d'un nouvel objectif
     * @param sender
     */
    public void onClick_AddGoal (View sender){
        Intent intent = new Intent(this,AddGoalActivity.class);
        startActivity(intent);
    }

    /*methode qui va remplir la recyclerListe( ou listView) avec les objectifs. (CODE INCOMPLET)*/
    public void gestionListe(){

    }
}


