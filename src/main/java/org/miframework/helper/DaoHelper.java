/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.miframework.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Time;

import org.miframework.annotation.Column;
import org.miframework.annotation.Fk;
import org.miframework.annotation.GenerationType;
import org.miframework.annotation.Pk;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.miframework.generic.GenericDao;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import org.miframework.generic.GenericDac;

/**
 *
 * @author miaro
 */
public class DaoHelper {
    
    public static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();
    static{
        PARSERS.put(int.class, Integer::parseInt);
        PARSERS.put(double.class, Double::parseDouble);
        PARSERS.put(String.class, Function.identity());
        PARSERS.put(long.class, Long::parseLong);
        PARSERS.put(LocalDate.class, LocalDate::parse);
        PARSERS.put(Timestamp.class, Timestamp::valueOf);
        PARSERS.put(Integer.class, Integer::parseInt);
        PARSERS.put(LocalTime.class, LocalTime::parse);
        PARSERS.put(LocalDateTime.class, LocalDateTime::parse);
    }
    
    /**
     * get the definitive class name and exclude the package name
     * @param className
     * @return
     * @throws Exception 
     */
    public static String getClassName(String className)throws Exception{
        String result = "";
        String[] classPackage = className.split("\\.");
        int size = classPackage.length;
        result = classPackage[size-1];
        
        return result;
    }
    
    /**
     * Take the first character of a string and make it uppercase
     * @param word
     * @return 
     */
    public static String toFirstUpperCase(String word){
        return word.substring(0,1).toUpperCase()+word.substring(1, word.length());
    }
    
    /**
     * Get all setters annotated in the object in assign the rs in each
     * @param obj
     * @param rs ResultSet of the select
     * @param c
     * @throws Exception 
     */
    public static void select(Object obj, ResultSet rs, Connection c)throws Exception{ 
    	// This function exists because getDeclaredFields() do not retrieve superclass field
    	Field[] fields = getAllFields(obj);
        for(Field field : fields){
            Class<?> fieldClassType = field.getType();
            String fieldType = fieldClassType.getName();
            String fieldClass = DaoHelper.getClassName(fieldType);
            String fieldClassName = DaoHelper.toFirstUpperCase(fieldClass);
            
            if(field.isAnnotationPresent(Column.class)){
                String setMethod = "set"+DaoHelper.toFirstUpperCase(field.getName());
                Method setter = obj.getClass().getMethod(setMethod, field.getType());
                
                // Get the column name from annotation Column
                String columnName = field.getAnnotation(Column.class).name();
                
                // Get the value of the column from the database as String
                Method getRS = rs.getClass().getMethod("getString", String.class);
                String columnCalueString = getRS.invoke(rs, columnName).toString();
                // Load parser function to parse the String from database to the proper type of the field
                Function<String, ?> parser = PARSERS.get(fieldClassType);
                
                setter.invoke(obj, parser.apply(columnCalueString));
            }
            else if(field.isAnnotationPresent(Fk.class)){
                // get the field type of the field of this object as this field is another object
                Class<?> fieldTypeObject = field.getType();
                
                // Instanciate an object of type of this field
                Object fk = fieldTypeObject.getDeclaredConstructor().newInstance();
                // Get the column name of the joined column
                String columnName = field.getAnnotation(Fk.class).join();
                
                // Get the primary key  (id) of the field of the object
                Field fieldPk = findPk(fieldTypeObject);
                // get the setId of the field (setter of the primary key of the field)
                String fieldPkNameMethod = toFirstUpperCase(fieldPk.getName());
                Method pkSetter = fieldTypeObject.getMethod("set"+fieldPkNameMethod, fieldPk.getType());
                
                // get the field value (FK values) from the databases
                Class<?> fieldPkTypeClass = fieldPk.getType();
                String fieldPkType = fieldPk.getType().getName();
                String fieldPkTypeName = DaoHelper.getClassName(fieldPkType);
                
                // Invoke the primary key setter of the field
                pkSetter.invoke(fk, rs.getObject(columnName, fieldPkTypeClass));
                
                // Getter of the primary key of the field
                Method pkGetter = fieldTypeObject.getMethod("get"+toFirstUpperCase(fieldPk.getName()), (Class<?>[]) null);
                
                // find the Object itself by id to set it as the field(Object) in obj
                fk = GenericDao.findById(c, fieldTypeObject, pkGetter.invoke(fk));
                
                Method setFieldObject = obj.getClass().getMethod("set"+toFirstUpperCase(field.getName()), fk.getClass());
                setFieldObject.invoke(obj, fk);
                
            }
        }
    }
    
    /**
     * Get all fields of object including the superclass field
     * @param obj
     * @return
     * @throws Exception
     */
    public static Field[] getAllFields(Object obj)throws Exception{
    	int fieldLength = obj.getClass().getDeclaredFields().length;
    	int superFieldLength = obj.getClass().getSuperclass().getDeclaredFields().length;
    	int resultSize = fieldLength+superFieldLength;
    	
    	Field[] results = new Field[resultSize];
    	
    	Field[] classesFields = obj.getClass().getDeclaredFields();
    	Field[] superClassFields = obj.getClass().getSuperclass().getDeclaredFields();
    	System.arraycopy(classesFields, 0, results, 0, fieldLength);
    	System.arraycopy(superClassFields, 0, results, fieldLength, superFieldLength);
    	
    	return results;
    }
    
    /**
     * Get the number of all field annotated as Column in an entity
     * @param fields Field[] all fields in an entity
     * @return
     * @throws Exception 
     */
    public static int getColumnLengths(Field[] fields)throws Exception{
        int i = 0;
        
        for(Field field : fields){
            if(field.isAnnotationPresent(Pk.class)){
                if(field.getAnnotation(Pk.class).generation() == GenerationType.AUTO){
                    continue;
                }
            }
            i++;
        }
        
        return i;
    }
    
    /**
     * Get all the column names of all fields annotated @Column in the entity (view param)
     * @param entity
     * @return
     * @throws Exception 
     */
    public static String[] getColumnNames(Object entity)throws Exception{
        int entityFieldNumber = getColumnLengths(entity.getClass().getDeclaredFields());
        String[] result = new String[entityFieldNumber];
        int i = 0;
        
        for(Field field : entity.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                if(field.isAnnotationPresent(Pk.class)){
                    if(field.getAnnotation(Pk.class).generation() == GenerationType.AUTO){
                        continue;
                    }
                }
                result[i] = field.getAnnotation(Column.class).name();
            }
            else if(field.isAnnotationPresent(Fk.class)){
                result[i] = field.getAnnotation(Fk.class).join();
            }
            i++;
        }
        
        
        
        return result;
    }
    
    /**
     * Get the column values by invoking getters
     * @param o
     * @return
     * @throws Exception 
     */
    public static String[] getColumnValues(Object o)throws Exception{
        int size = getColumnLengths(o.getClass().getDeclaredFields());
        String[] results = new String[size];
        int i = 0;
        Map<String, String> connectionProps = GenericDac.extract();
        
        for(Field field : o.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                if(field.isAnnotationPresent(Pk.class)){
                    if(field.getAnnotation(Pk.class).generation() == GenerationType.AUTO){
                        continue;
                    }
                    else if(field.getAnnotation(Pk.class).generation() == GenerationType.SEQ){
                        if("psql".equals(connectionProps.get("db.type"))){
                            String seqName = field.getAnnotation(Pk.class).sequence();
                            results[i] = "nextval('"+ seqName +"')";
                            i++;
                            continue;
                        }
                        else if("orcl".equals(connectionProps.get("db.type"))){
                            String seqName = field.getAnnotation(Pk.class).sequence();
                            results[i] = ""+ seqName +".nextval)";
                            i++;
                            continue;
                        }
                    }
                }
                String fieldName = field.getName();
                fieldName = toFirstUpperCase(fieldName);
                Method getter = o.getClass().getMethod("get"+fieldName, (Class<?>[]) null);
                String fieldValue = getter.invoke(o).toString();
                results[i] = "'"+fieldValue+"'";
                if(field.getType() == LocalDate.class || field.getType() == Date.class)
                    results[i] = " TO_DATE('yyyy-MM-dd', '"+fieldValue+"')";
                i++;
            }
            else if(field.isAnnotationPresent(Fk.class)){
                Class<?> fieldType = field.getType();
                Field fieldPk = findPk(fieldType);
                String fieldName = fieldPk.getName();
                String fieldPkName = getClassName(fieldName);
                Method pkGetter = fieldType.getMethod("get"+toFirstUpperCase(fieldPkName), (Class<?>[])null);
                
                // Set the field Object to accessible
                field.setAccessible(true);
                // Get the actual value as object off the field
                Object fieldAsOject = field.get(o);
                String pkValue = pkGetter.invoke(fieldAsOject).toString();
                results[i] = "'"+pkValue+"'";
                i++;
            }
        }
        
        return results;
    }
    
    /**
     * Concatenate the String[] and put "," comma as separator
     * @param strings
     * @return
     * @throws Exception 
     */
    public static String concatenate(String[] strings)throws Exception{
        String result = "";
        
        for(String s : strings){
            result += s+",";
        }
        
        return result.substring(0, result.length()-1);
    }
    
    /**
     * From all fields of this class, return the only one primary key (find by annotation having @Pk)
     * @param myclass
     * @return
     * @throws Exception 
     */
    public static Field findPk(Class<?> myclass)throws Exception{
        Field result = null;
        
        for(Field field : myclass.getDeclaredFields()){
            if(field.isAnnotationPresent(Pk.class)){
                result = field;
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Return fields and value as id='2' and name='Kris' amd so on
     * @param myclass
     * @return
     * @throws Exception 
     */
    public static String conactenateFieldValue(Object myclass, String separator)throws Exception{
        
        String result = "";
        
        for(Field field : myclass.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                String columnName = field.getAnnotation(Column.class).name();
                String fieldName = field.getName();
                Method getter = myclass.getClass().getMethod("get"+toFirstUpperCase(fieldName), (Class<?>[])null);
                String getterValue = getter.invoke(myclass).toString();
                result += columnName+"='"+ getterValue +"' "+separator+" ";
            }
            else if(field.isAnnotationPresent(Fk.class)){
                Class<?> fieldType = field.getType();
                Field fieldPk = findPk(fieldType);
                String fieldPkName = fieldPk.getName();
                String columnName = field.getAnnotation(Fk.class).join();
                fieldPkName = getClassName(fieldPkName);
                fieldPkName = toFirstUpperCase(fieldPkName);
                Method pkGetter = fieldType.getMethod("get"+fieldPkName, (Class<?>[])null);
                
                // Set the field Object to accessible
                field.setAccessible(true);
                // Get the actual value as object off the field
                Object fieldAsOject = field.get(myclass);
                String getterPkValue = pkGetter.invoke(fieldAsOject).toString();
                result += columnName+"='"+getterPkValue+"' "+separator+" ";
            }
        }
        
        int sepSize = separator.length()+1;
        return result.substring(0, result.length()-sepSize);
        
    }
    
    public static String concatenatePkValue(Object entity)throws Exception{
        String result = "";
        
        Field pk = findPk(entity.getClass());
        String columnNamePk = pk.getAnnotation(Column.class).name();
        Method pkGetter = entity.getClass().getMethod("get"+toFirstUpperCase(pk.getName()), (Class<?>[])null);
        result = columnNamePk+"='"+ pkGetter.invoke(entity) +"'";
        
        return result;
    }
    
    /**
     * Transform the Map condition to a query statement like "key=value and ..."
     * @param conditions
     * @return
     * @throws Exception
     */
    public static String reuniteCondition(Map<String, String> conditions)throws Exception{
    	String conditionValue = "";
    	
    	for(String key : conditions.keySet()) {
    		conditionValue += key+"='"+conditions.get(key)+"' and ";
    	}
    	
    	
    	return conditionValue.substring(0, conditionValue.length()-" and ".length());
    }
        
}
