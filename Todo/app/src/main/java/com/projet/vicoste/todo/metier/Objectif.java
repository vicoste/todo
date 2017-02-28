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

    private Date dateDebut;
        public Date getDateDebut() {return dateDebut;}
        public void setDateDebut(Date date) {this.dateDebut = date;}

    private Date dateFin;
        public Date getDateFin() {return dateFin;}
        public void setDateFin(Date date) {this.dateFin = date;}


    public Objectif(String nom, String description, Date dateDebut, Date dateFin) {
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        if (dateFin == null){
            this.dateFin = dateDebut;
        }
    }


    @Override
    public String toString() {
        return nom + " se deroule le" + dateDebut.toString()+". " + description;
    }
}

