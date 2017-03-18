package com.projet.vicoste.todo.metier;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

import com.projet.vicoste.todo.R;
import com.projet.vicoste.todo.modele.Objectif;

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

    /**
     * Context
     */
    private static Context context;

    /**
     * ID du calendrier choisit par l'utilisateur
     */
    private static int calendarID;

    /**
     * projection de ce que l'on va vouloir récupérer pour les evenements dans le calendrier
     */
    private static String[] eventProjection = new String[]{CalendarContract.Events._ID, CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};

    /**
     * Point d'accès pour l'instance unique d'Objectif Manager [Singleton]
     * @return liste des objectifs que connait l'application
     */
    public static List<Objectif> getObjectifs(Context c, int IDcalendar) {
            if (objectifs == null){
                context = c;
                objectifs = new ArrayList<>();
                calendarID = IDcalendar;
                objectifInitialization();
            }
            return objectifs;
        }


    /**
     * constructeur privé
     */
    private ObjectifManager()
    {};


    /**
     * Methode qui va initialiser les objectifs de l'application avec tout les événements présents dans le calendrier principal de l'utilisateur
     */
    private static void objectifInitialization() {
        String selection = "((" + CalendarContract.Instances.CALENDAR_ID + " = ?))"; //construct the query
        String[] selectionArgs = new String[]{String.valueOf(calendarID)};
        @SuppressWarnings("MissingPermission") Cursor cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, eventProjection, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            Date d1 = new Date(cursor.getLong(3));
            Date d2 = new Date(cursor.getLong(4));
            objectifs.add(new Objectif(cursor.getInt(0), cursor.getString(1), cursor.getString(2),d1 ,d2));
        }
        cursor.close();
    }

    /**
     * Supprime un objectif de la liste
     * @param o objectif a supprimer
     * @return true si l'objectif a bien ete supprime, false sinon
     */
    public static boolean deleteObjectif(Objectif o){
        return objectifs.remove(o);
    }

}
