/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.miframework.generic;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Statement;
import java.sql.ResultSet;
import org.miframework.annotation.Column;
import org.miframework.annotation.Table;
import org.miframework.helper.DaoHelper;

/**
 *
 * @author miaro
 */
public class GenericDao {
        
    /**
     * Get all data rows of type T from database
     * @param <T>
     * @param myclass
     * @return List< T >
     * @throws Exception
     */
    public static <T> List<T> getAll(Connection c, Class<T> myclass)throws Exception{
        List<T> results = new ArrayList<>();
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        // Instanciating a new Connection
        
        String tableName = myclass.getSimpleName();
        if(myclass.isAnnotationPresent(Table.class)){
            if(!myclass.getAnnotation(Table.class).name().equals(""))
                tableName = myclass.getAnnotation(Table.class).name();
        }
        String query = "SELECT * FROM " + tableName;
        
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                T obj = (T)myclass.getDeclaredConstructor().newInstance();
                // Function that get all the fields annotated @Column in the class and put the rs in each
                DaoHelper.select(obj, rs, c);
                
                results.add(obj);
            }
            
        }finally{
            if(shallOpen) c.close();
        }

        return results;
    }
    
    /**
     * Find an Object from database by its Id
     * @param <T>
     * @param <K>
     * @param myclass
     * @param key
     * @return
     * @throws Exception 
     */
    public static <T,K> T findById(Connection c, Class<T> myclass, K key )throws Exception{
        T result = null;

        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        String tableName = myclass.getSimpleName();
        if(myclass.isAnnotationPresent(Table.class)){
            if(!myclass.getAnnotation(Table.class).name().equals(""))
                tableName = myclass.getAnnotation(Table.class).name();
        }
        
        Field pk = DaoHelper.findPk(myclass);
        
        String query = "SELECT * FROM "+tableName+" where "+pk.getAnnotation(Column.class).name()+"='"+key+"'";
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                T obj = (T)myclass.getDeclaredConstructor().newInstance();
                // Function that get all the fields annotated @Column in the class and put the rs in each
                DaoHelper.select(obj, rs, c);
                result = obj;
            }
        } finally{
            if(shallOpen) c.close();
        }
        
        return result;
    }
    
    /**
     * Insert the T entity into the database
     * @param <T>
     * @param entity
     * @throws Exception 
     */
    public static <T> void insert(Connection c, T entity)throws Exception{
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        // Load the column names and return them as "nom,prenom"
        String[] fieldNames = DaoHelper.getColumnNames(entity);
        String fieldNameConcat = DaoHelper.concatenate(fieldNames);
        
        //Load the value classes and return them as "'Rakoto','Rabe'"
        String[] fieldValues = DaoHelper.getColumnValues(entity);
        String fieldValuesConcat = DaoHelper.concatenate(fieldValues);
        
        String tableName = entity.getClass().getSimpleName();
        if(entity.getClass().isAnnotationPresent(Table.class)){
            if(!entity.getClass().getAnnotation(Table.class).name().equals(""))
                tableName = entity.getClass().getAnnotation(Table.class).name();
        }
        
        String query = "INSERT INTO "+tableName+" ( "+ fieldNameConcat +" ) values ("+ fieldValuesConcat +")" ;
        try {
            Statement stmt =  c.createStatement();
            stmt.executeUpdate(query);
        }finally{
            if(shallOpen) c.close();
        }
        
    }
    
    /**
     * Function that remove that row (in the param) in the database
     * @param <T>
     * @param entity
     * @throws Exception 
     */
    public static <T> void remove(Connection c, T entity)throws Exception{
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        String tableName = entity.getClass().getSimpleName();
        if(entity.getClass().isAnnotationPresent(Table.class)){
            if(!entity.getClass().getAnnotation(Table.class).name().equals(""))
                tableName = entity.getClass().getAnnotation(Table.class).name();
        }
        
        String fieldValueAnd = DaoHelper.conactenateFieldValue(entity, "AND");
        String query = "DELETE FROM "+tableName+" WHERE "+fieldValueAnd;
        
        try {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(query);
        } finally{
            if(shallOpen) c.close();
        }
        
    }
    
    /**
     * Function that update
     * @param <T>
     * @param c
     * @param entity
     * @throws Exception
     */
    public static <T> void update(Connection c, T entity)throws Exception{
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        String tableName = entity.getClass().getSimpleName();
        if(entity.getClass().isAnnotationPresent(Table.class)){
            if(!entity.getClass().getAnnotation(Table.class).name().equals(""))
                tableName = entity.getClass().getAnnotation(Table.class).name();
        }
        
        String fieldValueComma = DaoHelper.conactenateFieldValue(entity, ",");
        String pkValue = DaoHelper.concatenatePkValue(entity);
        
        String query = "UPDATE "+tableName+" SET "+fieldValueComma+" WHERE "+pkValue;
        
        try {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(query);
        } finally{
            if(shallOpen) c.close();
        }
    }
    
    /**
     * Find Objects from database by some conditions given in hashmap
     * key is the name of the column and the value the value to compare it
     * @param <T>
     * @param <K>
     * @param myclass
     * @param key
     * @return
     * @throws Exception 
     */
    public static <T> List<T> findBy(Connection c, Class<T> myclass, Map<String, String> conditions )throws Exception{
        List<T>result = new ArrayList<>();
        
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        String tableName = myclass.getSimpleName();
        if(myclass.isAnnotationPresent(Table.class)){
            if(!myclass.getAnnotation(Table.class).name().equals(""))
                tableName = myclass.getAnnotation(Table.class).name();
        }
        
        Field pk = DaoHelper.findPk(myclass);
        
        String queryConditions = DaoHelper.reuniteCondition(conditions);
        
        String query = "SELECT * FROM "+tableName+" where "+queryConditions;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                T obj = (T)myclass.getDeclaredConstructor().newInstance();
                // Function that get all the fields annotated @Column in the class and put the rs in each
                DaoHelper.select(obj, rs, c);
                result.add(obj);
            }
        } finally{
            if(shallOpen) c.close();
        }
        
        return result;
    }
    
    /**
     * Find lists of object from database by some conditions given in hashmap
     * key is the name of the column and the value the value to compare it and order the result by giving the column
     * @param <T>
     * @param <K>
     * @param myclass
     * @param key
     * @return
     * @throws Exception 
     */
    public static <T> List<T> findByOrdering(Connection c, Class<T> myclass, Map<String, String> conditions, String condition, String columns, String ascdesc)throws Exception{
        List<T>result = new ArrayList<>();
        
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        String tableName = myclass.getSimpleName();
        if(myclass.isAnnotationPresent(Table.class)){
            if(!myclass.getAnnotation(Table.class).name().equals(""))
                tableName = myclass.getAnnotation(Table.class).name();
        }
        
        Field pk = DaoHelper.findPk(myclass);
        
        String queryConditions = DaoHelper.reuniteCondition(conditions);
        
        String query = "SELECT * FROM "+tableName+" where "+queryConditions+" and "+condition+" order by "+columns+" "+ascdesc;
        
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                T obj = (T)myclass.getDeclaredConstructor().newInstance();
                // Function that get all the fields annotated @Column in the class and put the rs in each
                DaoHelper.select(obj, rs, c);
                result.add(obj);
            }
        } finally{
            if(shallOpen) c.close();
        }
        
        return result;
    }
    
    /**
     * Find an Object from database by some conditions given in hashmap
     * key is the name of the column and the value the value to compare it
     * @param <T>
     * @param <K>
     * @param myclass
     * @param key
     * @return
     * @throws Exception 
     */
    public static <T> T findOneBy(Connection c, Class<T> myclass, Map<String, String> conditions )throws Exception{
        T result = null;
        
        Boolean shallOpen = c == null;
        if(shallOpen) c = GenericDac.generate();
        
        String tableName = myclass.getSimpleName();
        if(myclass.isAnnotationPresent(Table.class)){
            if(!myclass.getAnnotation(Table.class).name().equals(""))
                tableName = myclass.getAnnotation(Table.class).name();
        }
        
        Field pk = DaoHelper.findPk(myclass);
        
        String queryConditions = DaoHelper.reuniteCondition(conditions);
        
        String query = "SELECT * FROM "+tableName+" where "+queryConditions;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                T obj = (T)myclass.getDeclaredConstructor().newInstance();
                // Function that get all the fields annotated @Column in the class and put the rs in each
                DaoHelper.select(obj, rs, c);
                result = obj;
            }
        } finally{
            if(shallOpen) c.close();
        }
        
        return result;
    }
        
}
