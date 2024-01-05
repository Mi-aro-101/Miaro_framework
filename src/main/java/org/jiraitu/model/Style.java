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
@Table (name="style_meuble")
public class Style {
    @Column (name="id_style")
    @Pk(generation = GenerationType.AUTO)
    Integer styleMeuble;
    @Column (name="des_style")
    String desStyleMeuble;

    public Style() {
    }

    public Style(Integer styleMeuble, String desStyleMeuble) {
        this.styleMeuble = styleMeuble;
        this.desStyleMeuble = desStyleMeuble;
    }

    public Integer getStyleMeuble() {
        return styleMeuble;
    }

    public void setStyleMeuble(Integer styleMeuble) {
        this.styleMeuble = styleMeuble;
    }

    public String getDesStyleMeuble() {
        return desStyleMeuble;
    }

    public void setDesStyleMeuble(String desStyleMeuble) {
        this.desStyleMeuble = desStyleMeuble;
    }
    
}
