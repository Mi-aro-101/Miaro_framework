/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jiraitu.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.etu2020.ModelView;
import org.etu2020.annotation.WebMethod;
import org.etu2020.annotation.WebService;
import org.jiraitu.model.MatierePremiere;
import org.jiraitu.model.Style;
import org.miframework.generic.GenericDac;
import org.miframework.generic.GenericDao;

/**
 *
 * @author pc
 */
@WebService
public class StyleController {
    @WebMethod(url="/nouveaustyle.do", args = true)
    public ModelView insererStyle()throws Exception{
        Map<String,Object> hash = new HashMap<>();
        try(Connection c = GenericDac.generate()){
            List<MatierePremiere> liste = GenericDao.getAll(c, MatierePremiere.class);
            hash.put("liste",liste);
        }catch(Exception e){
            throw e;
        }
        return new ModelView("NewStyle.jsp",hash);
    }
}
