package com.projet.vicoste.todo.metier;

import java.util.Date;

/**
 * Created by vicoste on 10/02/17.
 */
public class Objectif {
    private String nom;
        public String getNom() {return nom;}
        public void setNom(String nom) {this.nom = nom;}

    private String description;
        public String getDescription() {return description;}
        public void setDescription(String contenue) {this.description = contenue;}

    private Date date;
        public Date getDate() {return date;}
        public void setDate(Date date) {this.date = date;}


    public Objectif(String nom, String description, Date date) {
        this.nom = nom;
        this.description = description;
        this.date = date;
    }


    @Override
    public String toString() {
        return nom + " se deroule le" + date.toString()+". " + description;
    }
}

