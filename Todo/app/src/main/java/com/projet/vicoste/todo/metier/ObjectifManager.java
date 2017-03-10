package com.projet.vicoste.todo.metier;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lou on 10/03/2017.
 * Singleton qui va gérer la liste des objectifs et l'initialiser afin d'éviter tout conflit
 */

public class ObjectifManager {

    /**
     * - Liste des objectifs de l'application
     * - Instance unique non préinitialisée
     */
    private static List<Objectif> objectifs = null;

    private static Context context;


    /**
     * Point d'accès pour l'instance unique d'Objectif Manager [Singleton]
     * @return liste des objectifs que connait l'application
     */
        public static List<Objectif> getObjectifs(Context c) {
            if (objectifs == null){
                context = c;
                objectifs = new ArrayList<>();
                objectifInitialization();
            }
            return objectifs;
        }

    /**
     * constructeur privé
     */
    private ObjectifManager()
    {};

    //#!!!!! Problème sur l'affichaque des dates à corriger ici !!!!!!#
    /**
     * Methode qui va initialiser les objectifs de l'application avec tout les événements présents dans le calendrier principal de l'utilisateur
     */
    private static void objectifInitialization() {
        String selection = "((" + CalendarContract.Instances.CALENDAR_ID + " = ?))"; //construct the query
        String[] selectionArgs = new String[]{"1"};
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.android.calendar/events"), new String[]{"_id", "title", "description", "dtstart", "dtend", "eventLocation"}, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            Date d1 = new Date(cursor.getLong(3));
            d1.setMonth(d1.getMonth()+1);
            Date d2 = new Date(cursor.getLong(4));
            d2.setMonth(d2.getMonth()+1);
            objectifs.add(new Objectif(cursor.getInt(0), cursor.getString(1), cursor.getString(2),d1 ,d2));
        }
        cursor.close();
    }

}
