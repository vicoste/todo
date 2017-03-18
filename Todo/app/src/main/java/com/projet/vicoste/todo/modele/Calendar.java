package com.projet.vicoste.todo.modele;

/**
 * Created by Lou on 17/03/2017.
 */

public class Calendar {
    //***************************************PARAMS*******************
    /**
     * Id du calendar
     */
    private int id;
        public int getId() {return id;}
        public void setId(int id) { this.id = id; }

    /**
     * Nom du calendar
     */
    private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

    //***************************************CONSTRUCTEUR*******************
    /**
     * Constructeur du calendar
     * @param id id du calendar
     * @param name name du calendar
     */
    public Calendar(int id, String name){
        this.id = id;
        this.name = name;
    }

    //***************************************METHODES*******************

    /**
     * Redefinition de la méthode toString, presentant le calendar sous forme de son id et de son nom
     * @return une présentation du calendar
     */
    @Override
    public String toString() {
        return "Calendrier: " + name;
    }
}
