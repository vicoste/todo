package com.projet.vicoste.todo.modele;

import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by vicoste on 10/02/17.
 */
public class Objectif {

    //*****************************PARAMS****************************
    /**
     * ID unique de l'objectif
     */
    private int id;
    public int getId() {return id;}
    public void setId(int id) { this.id = id; }

    /**
     * nom principal de l'objectif
     */
    private String nom;
    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}

    /**
     * description détaillée de l'objectif
     */
    private String description;
    public String getDescription() {return description;}
    public void setDescription(String contenue) {this.description = contenue;}

    /**
     * date de commencement de l'objectif
     */
    private Date dateDebut;
    public Date getDateDebut() {return dateDebut;}
    public void setDateDebut(Date date) {this.dateDebut = date;}

    /**
     * date de fin de l'objectif
     */
    private Date dateFin;
    public Date getDateFin() {return dateFin;}
    public void setDateFin(Date date) {this.dateFin = date;}

    private Boolean ended = false;
    public Boolean isEnded(){return ended;}



    //****************************CONSTRUCTORS***********************
    /**
     * Constructeur de la classe Objectif
     * @param id id de l'objectif (mettre -1 si pas précisé)
     * @param nom nom principal de l'objectif
     * @param description description détaillée de l'objectif
     * @param dateDebut date de début de l'objectif
     * @param dateFin date de fin de l'objectif. Si null, elle sera ramené à la date de début + 5 heures
     */
    public Objectif(int id, String nom, String description, Date dateDebut, Date dateFin) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        if (dateFin == null){
            this.dateFin = dateDebut;
        }
        if (dateDebut.compareTo(new GregorianCalendar().getTime()) <= 0)
            ended = true;
    }


    //****************************METHODS***********************
    /**
     * Redefinition de la methode toString spécifique aux objectifs
     * @return un message peu détaillé de présentation de l'objectif
     */
    @Override
    public String toString() {
        return nom + " se deroule le" + dateDebut.toString()+". " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Objectif objectif = (Objectif) o;

        if (id != objectif.id) return false;
        if (nom != null ? !nom.equals(objectif.nom) : objectif.nom != null) return false;
        if (description != null ? !description.equals(objectif.description) : objectif.description != null)
            return false;
        if (dateDebut != null ? !dateDebut.equals(objectif.dateDebut) : objectif.dateDebut != null)
            return false;
        if (dateFin != null ? !dateFin.equals(objectif.dateFin) : objectif.dateFin != null)
            return false;
        return ended != null ? ended.equals(objectif.ended) : objectif.ended == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateDebut != null ? dateDebut.hashCode() : 0);
        result = 31 * result + (dateFin != null ? dateFin.hashCode() : 0);
        result = 31 * result + (ended != null ? ended.hashCode() : 0);
        return result;
    }
}
