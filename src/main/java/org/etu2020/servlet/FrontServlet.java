/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package org.etu2020.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import org.etu2020.Mapping;
import org.etu2020.ModelView;
import org.etu2020.utility.FrontServletUtils;

/**
 *
 * @author Miaro
 */
public class FrontServlet extends HttpServlet {
    
    Map<String, Mapping> urlMapping;
    
    @Override
    public void init(ServletConfig config)throws ServletException{
        String packageName = config.getInitParameter("root");
        
        this.setUrlMapping(new HashMap<>());
        
        try{
            FrontServletUtils.getClasses(packageName, this.getUrlMapping());
            
        }catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
        
        // retrieve the url
        String url = request.getRequestURI();
        // retrieve the url method only
        String urlMethod = this.getUrlMethod(url, out, request);
        // get the proper mapping class depending or the urlmethod
        Mapping map = this.getUrlMapping().get(urlMethod);

                
        try{
//            // Instanciate the class that contains the method to execute
            Object o = Class.forName(map.getClassName()).getConstructor().newInstance();
////            // Call the method and invoke it => as the method is forced to return a json
            ModelView result = (ModelView) FrontServletUtils.execute(request, map, o);
            
            for(String key : result.getDatas().keySet()){
                request.setAttribute(key, result.getDatas().get(key));
            }
            
            
            RequestDispatcher dispatcher = request.getRequestDispatcher(result.getView());
            dispatcher.forward(request, response);
//            
        }catch(Exception e){
            out.println(e);
            e.printStackTrace(out);
        }
        finally{
            out.close();
        }
    }
    
    /**
     * Retrieve the url method only
     * @param fullUrl
     * @param out
     * @return 
     */
    public String getUrlMethod(String fullUrl, PrintWriter out, HttpServletRequest request){
        String contextPath = request.getContextPath();
        String endpoint = request.getRequestURI().substring(contextPath.length());
        endpoint = endpoint.substring(1);
        
        return endpoint;
        
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

    public Map<String, Mapping> getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(Map<String, Mapping> urlMapping) {
        this.urlMapping = urlMapping;
    }
    
}
