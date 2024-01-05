/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jiraitu.controller;

import org.etu2020.ModelView;
import org.etu2020.annotation.WebMethod;
import org.etu2020.annotation.WebService;
import org.jiraitu.model.Categorie;
import org.miframework.generic.GenericDac;
import org.miframework.generic.GenericDao;
import java.sql.Connection;
import java.util.*;
/**
 *
 * @author pc
 */
@WebService
public class CategorieController {
    @WebMethod(url="/nouveaucategorie.do", args = true)
    public ModelView insererCategorie(String designation)throws Exception{
        Categorie categorie = new Categorie(null, designation);
        Map<String,Object> hash = new HashMap<>();
        try(Connection c = GenericDac.generate()){
            GenericDao.insert(c, categorie);
        }catch(Exception e){
            throw e;
        }
        return new ModelView("Newcategorie.html",hash);
    }
}
