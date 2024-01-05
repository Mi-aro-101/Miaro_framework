/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jiraitu.controller;

import org.etu2020.ModelView;
import org.etu2020.annotation.WebMethod;
import org.etu2020.annotation.WebService;
import org.jiraitu.model.MatierePremiere;
import org.miframework.generic.GenericDac;
import org.miframework.generic.GenericDao;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author pc
 */
@WebService
public class MatierePremiereController {
    @WebMethod(url="/nouveaumatiere.do", args = true)
    public ModelView insererMatierePremiere(String designation)throws Exception{
        MatierePremiere matiere = new MatierePremiere(null, designation);
        Map<String,Object> hash = new HashMap<>();
        try(Connection c = GenericDac.generate()){
            GenericDao.insert(c, matiere);
        }catch(Exception e){
            throw e;
        }
        return new ModelView("NewMatierePremiere.jsp",hash);
    }
}
