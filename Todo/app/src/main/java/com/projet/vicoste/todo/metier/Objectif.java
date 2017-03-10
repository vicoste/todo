package com.projet.vicoste.todo.metier;

import java.util.Date;

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
            dateDebut.setHours(dateDebut.getHours() + 5);
            this.dateFin = dateDebut;
        }
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

    /**
     * redefinition de la méthode equals spécifique aux objectifs
     * @param o
     * @return
     */
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
        return dateFin != null ? dateFin.equals(objectif.dateFin) : objectif.dateFin == null;
    }

    /**
     * Redefinition du hashCode spécifique aux objectifs
     * @return
     */
    @Override
    public int hashCode() {
        int result = nom != null ? nom.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateDebut != null ? dateDebut.hashCode() : 0);
        result = 31 * result + (dateFin != null ? dateFin.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}

