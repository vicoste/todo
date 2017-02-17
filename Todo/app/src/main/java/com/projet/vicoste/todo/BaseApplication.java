package com.projet.vicoste.todo;

import android.app.Application;

import com.projet.vicoste.todo.metier.Objectif;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicoste on 17/02/17.
 */
public class BaseApplication extends Application {

    //    //liste de tout les objectifs
   private List<Objectif> listeObjectifs = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void add(Objectif o) {
        listeObjectifs.add(o);
    }

    public List<Objectif> getObjectifs(){
        return listeObjectifs;
    }
}
