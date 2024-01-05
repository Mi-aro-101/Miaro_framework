/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jiraitu.model;

import org.miframework.annotation.Column;
import org.miframework.annotation.Fk;
import org.miframework.annotation.GenerationType;
import org.miframework.annotation.Pk;
import org.miframework.annotation.Table;

/**
 *
 * @author pc
 */
@Table (name="meuble")
public class Meuble {
    @Column (name="id_meuble")
    @Pk(generation = GenerationType.AUTO)
    Integer idMeuble;
    @Column (name="designation")
    String desMeuble;
    @Fk (join = "id_categorie")
    Categorie idCategorie;

    public Meuble() {
    }

    public Integer getIdMeuble() {
        return idMeuble;
    }

    public void setIdMeuble(Integer idMeuble) {
        this.idMeuble = idMeuble;
    }

    public String getDesMeuble() {
        return desMeuble;
    }

    public void setDesMeuble(String desMeuble) {
        this.desMeuble = desMeuble;
    }

    public Categorie getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Categorie idCategorie) {
        this.idCategorie = idCategorie;
    }
    
}
