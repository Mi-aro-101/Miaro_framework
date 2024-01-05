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
@Table (name="categorie_meuble")
public class Categorie {
    @Column (name="id_categorie")
    @Pk(generation = GenerationType.AUTO)
    Integer idCategore;
    @Column (name="des_categorie_meuble")
    String desCategorieMeuble;

    public Categorie(Integer idCategore, String desCategorieMeuble) {
        this.idCategore = idCategore;
        this.desCategorieMeuble = desCategorieMeuble;
    }

    public Categorie() {
    }

    public Integer getIdCategore() {
        return idCategore;
    }

    public void setIdCategore(Integer idCategore) {
        this.idCategore = idCategore;
    }

    public String getDesCategorieMeuble() {
        return desCategorieMeuble;
    }

    public void setDesCategorieMeuble(String desCategorieMeuble) {
        this.desCategorieMeuble = desCategorieMeuble;
    }
    
}
