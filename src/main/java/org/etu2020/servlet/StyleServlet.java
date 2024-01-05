/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package org.etu2020.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jiraitu.model.MatierePremiere;
import org.jiraitu.model.Style;
import org.miframework.generic.GenericDao;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.jiraitu.model.StyleMatierePremiere;
import org.miframework.generic.GenericDac;
/**
 *
 * @author pc
 */
public class StyleServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try(Connection c = GenericDac.generate()){
            String style = request.getParameter("style");
            String[] tableau = request.getParameterValues("liste");
            
            Style s = new Style(null,style);
            try {
                GenericDao.insert(c, s);
            } catch (Exception e) {
                e.printStackTrace(out);
            }
            
            Map<String, String> conditions = new HashMap<>();
            conditions.put("des_style", style);
            
            Style styleObject = GenericDao.findOneBy(c, Style.class, conditions);
                        
            int[] tab = conversion(tableau);
            
            for (int i : tab) {
              
                MatierePremiere mat = GenericDao.findById(c, MatierePremiere.class, i);
                
                StyleMatierePremiere styleMatiere = new StyleMatierePremiere(null, mat, styleObject);
                
                GenericDao.insert(c, styleMatiere);
            }
        }catch(Exception e){
            out.print(e);
            e.printStackTrace(out);
        }
    }
    public int[] conversion(String[] chaine){
        int[] rep = new int[chaine.length];
        int k=0;
        for (String i : chaine) {
            rep[k] = Integer.parseInt(i);
            k++;
        }
        return rep;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
