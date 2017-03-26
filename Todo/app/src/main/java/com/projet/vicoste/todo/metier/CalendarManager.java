package com.projet.vicoste.todo.metier;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.projet.vicoste.todo.modele.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lou on 17/03/2017.
 * Classe qui va contrôler la gestion des calendriers de l'application
 */

public class CalendarManager {
    /**
     * - Liste des objectifs de l'application
     * - Instance unique non préinitialisée
     */
    private static List<Calendar> calendars = null;

    /**
     * Context
     */
    private static Context context;

    /**
     * projection de ce que l'on va vouloir récupérer pour les calendriers de l'utilisateur
     */
    private static String[] eventProjection = new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME};

    /**
     * Point d'accès pour l'instance unique d'Objectif Manager [Singleton]
     * @return liste des objectifs que connait l'application
     */
    public static List<Calendar> getCalendars(Context c) {
        if (calendars == null){
            context = c;
            calendars = new ArrayList<>();
            calendarInitialization();
        }
        return calendars;
    }


    /**
     * constructeur privé
     */
    private CalendarManager()
    {};


    /**
     * Methode qui va initialiser les objectifs de l'application avec tout les événements présents dans le calendrier principal de l'utilisateur
     */
    private static void calendarInitialization() {
        @SuppressWarnings("MissingPermission") Cursor cursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, eventProjection, null, null, null);
        while (cursor.moveToNext()) {
            calendars.add(new Calendar(cursor.getInt(0), cursor.getString(1)));
        }
        cursor.close();
    }


}
