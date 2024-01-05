/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jiraitu.model;

import org.miframework.annotation.Column;
import org.miframework.annotation.GenerationType;
import org.miframework.annotation.Pk;
import org.miframework.annotation.Table;

/**
 *
 * @author pc
 */
@Table (name="matiere_premiere")
public class MatierePremiere {
    @Column (name="id_matiere_premiere")
    @Pk(generation = GenerationType.AUTO)
    Integer idMatierePremiere;
    @Column (name="des_matiere_premiere")
    String desMatierePremiere;

    public MatierePremiere(Integer idMatierePremiere, String desMatierePremiere) {
        this.idMatierePremiere = idMatierePremiere;
        this.desMatierePremiere = desMatierePremiere;
    }

    public int getIdMatierePremiere() {
        return idMatierePremiere;
    }

    public void setIdMatierePremiere(Integer idMatierePremiere) {
        this.idMatierePremiere = idMatierePremiere;
    }

    public String getDesMatierePremiere() {
        return desMatierePremiere;
    }

    public void setDesMatierePremiere(String desMatierePremiere) {
        this.desMatierePremiere = desMatierePremiere;
    }

    public MatierePremiere() {
    }
    
}
