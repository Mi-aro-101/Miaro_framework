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
@Table (name="style_matiere_premiere")
public class StyleMatierePremiere {
    
    @Column (name="id_style_matiere_premiere")
    @Pk(generation = GenerationType.AUTO)
    Integer idStyleMatierePremiere;
    
    @Fk (join = "id_matiere_premiere")
    MatierePremiere idMatiere;
    
    @Fk (join = "id_style_meuble")
    Style idStyle;

    public StyleMatierePremiere(Integer idStyleMatierePremiere, MatierePremiere idMatiere, Style idStyle) {
        this.idStyleMatierePremiere = idStyleMatierePremiere;
        this.idMatiere = idMatiere;
        this.idStyle = idStyle;
    }
    
    public StyleMatierePremiere() {
    }

    public Integer getIdStyleMatierePremiere() {
        return idStyleMatierePremiere;
    }

    public void setIdStyleMatierePremiere(Integer idStyleMatierePremiere) {
        this.idStyleMatierePremiere = idStyleMatierePremiere;
    }

    public MatierePremiere getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(MatierePremiere idMatiere) {
        this.idMatiere = idMatiere;
    }

    public Style getIdStyle() {
        return idStyle;
    }

    public void setIdStyle(Style idStyle) {
        this.idStyle = idStyle;
    }
    
}
