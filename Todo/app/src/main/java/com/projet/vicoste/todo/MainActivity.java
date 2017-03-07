package com.projet.vicoste.todo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.projet.vicoste.todo.adaptateurs.RecyclerViewObjectifAdaptateur;
import com.projet.vicoste.todo.metier.Objectif;

import java.util.Calendar;

/**
 * Created by Lou on 05/02/2017.
 * Activité basée sur la visualisation de tout les objectifs de l'utilisateur
 */

public class MainActivity extends AppCompatActivity {

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };
    private static final int PROJECTION_TITLE= 2;
    private static final int WRITE_CALENDAR_REQUEST = 1;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.lv_todolist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewObjectifAdaptateur(((BaseApplication)getApplication()).getObjectifs());
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton button_add = (FloatingActionButton)findViewById(R.id.fab_main_go_add_goal);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddGoalActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_CALENDAR_REQUEST) {
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * Initialise l'application avec tout les evements trouvés dans le calendar de maintenant jusque dans 3 ans
     */
   /* private void eventInitialization(){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set();
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set();
        long endMillis = endTime.getTimeInMillis();
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);
        cur =  cr.query(builder.build(), //submit the query
                INSTANCE_PROJECTION,
                null,
                null,
                null);
        while (cur.moveToNext()) {
            ((BaseApplication)getApplication()).add(new Objectif(cur.getString(PROJECTION_TITLE), null,null ,null));
        }
    }*/

}


