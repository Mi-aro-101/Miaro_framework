/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.miframework.generic;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.DriverManager;
import java.util.Map;

/**
 *
 * @author miaro
 */
public class GenericDac {
    
    static final Map<String, String> dbClassDriver = new HashMap<>();
    static{
        dbClassDriver.put("psql", "org.postgresql.Driver");
        dbClassDriver.put("orcl", "oracle.jdbc.OracleDriver");
    }
    
    /**
     * Load the db.properties file and put them in a String[]
     * @return
     * @throws Exception 
     */
    public static String[] getDatasFromFile()throws Exception{
        Connection connection = null;
        
        InputStream dbFile = GenericDac.class.getClassLoader().getResourceAsStream("db.properties");
        String[] datas;
        try (Scanner scanner = new Scanner(dbFile)) {
            datas = new String[4];
            int i = 0;
            while(scanner.hasNextLine()){
                datas[i] = scanner.nextLine();
                i++;
            }
        }
        
        return datas;
    }
    
    /**
     * Extract the db properties from the file and put it in HashMap 
     * db_props_name = value_props
     * @return
     * @throws Exception
     */
    public static Map<String, String> extract()throws Exception{
        String[] datas = getDatasFromFile();
        Map<String, String> results = new HashMap<>();
        
        for(String props : datas){
            String[] keyvalue = props.split("=");
            results.put(keyvalue[0], keyvalue[1]);
        }
        
        return results;
    }    
    /**
     * Generate a Connection based on Hashmap => < db_props_name, value_db_props >
     * @return Connection having the properties you gave
     * @throws Exception 
     */
    public static Connection generate()throws Exception{
        Map<String, String> dbprops = extract();
        Connection connection = null;
       
        String dbTypeProps = dbprops.get("db.type");
        
        String driver = dbClassDriver.get(dbTypeProps);
        String url = dbprops.get("db.url");
        String username = dbprops.get("db.username");
        String password = dbprops.get("db.password");
        
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw e;
        }
        
        return connection;
    }
    
}
