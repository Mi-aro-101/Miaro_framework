/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.etu2020.utility;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.etu2020.Mapping;
import org.etu2020.ModelView;
import org.etu2020.annotation.WebMethod;
import org.etu2020.annotation.WebService;

/**
 *
 * @author Miaro
 */
public class FrontServletUtils {
    
     // map that willcontains all parser method
    public static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();
    static{
        PARSERS.put(int.class, Integer::parseInt);
        PARSERS.put(double.class, Double::parseDouble);
        PARSERS.put(String.class, Function.identity());
        PARSERS.put(long.class, Long::parseLong);
    }
    
    /**
     * function that will scan all classes in a package
     * also calls get method to retrieve all the methods in that class that will present a method annotation
     * @param packageName is the package to scan
     * @param urlMapping is the HashMap to fill with the url and Mapping (sprint2)
     * @return an Arraylist of all the classes found
     * @throws java.lang.ClassNotFoundException in case there is non e such class
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    public static ArrayList<Class<?>> getClasses(String packageName, Map<String, Mapping> urlMapping) throws ClassNotFoundException, IOException, URISyntaxException, Exception {
        ArrayList<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        path = path.replace("%20", " ");
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
                for (File child : file.listFiles()) {
                    if(child.isFile()){
                        String className = packageName + "." + child.getName().split("\\.")[0];
                        classes.add(Class.forName(className));
                        
                        if(Class.forName(className).isAnnotationPresent(WebService.class)){
                            getMethods(Class.forName(className), urlMapping);
                        }
                    }
                    classes.addAll(getClasses(packageName + "." + child.getName().split("\\.")[0], urlMapping));
                }
        }
        return classes;
    }
    
    
    /**
     * also this function will be called above in order to : 
     * scan the classes and in the meantime scan the methods in it 
     * @param klass is the class that will have the methods to retrieve
     * @param urlMapping is the HashMap object that will be filled as requested by Mr Naina
     * @throws java.lang.Exception
     */
    public static void getMethods(Class<?> klass, Map<String, Mapping> urlMapping) throws Exception{
        for(Method method : klass.getDeclaredMethods()){
            if(method.isAnnotationPresent(WebMethod.class)){
                WebMethod annotation = method.getAnnotation(WebMethod.class);
                String url  = annotation.url();
                Mapping map = new Mapping(klass.getName(), method.getName());
                String key = url.substring(1, url.length());
                urlMapping.put(key, map);
            }
        }        
    }
    
    public static ModelView execute(HttpServletRequest request, Mapping map, Object o)throws Exception{
        // Json converter
        Gson jsonconverter = new Gson();
       // variables that will contains the result as json (String) 
        ModelView response = null;
        Method method = toMethod(map, o);
        if(method.getAnnotation(WebMethod.class).args()){
            response = (ModelView) invokeMethodWithArgs(request, map, o, method, jsonconverter);
        }
        else{
            response = (ModelView) o.getClass().getMethod(map.getMethodName(), (Class<?>[])null).invoke(o);
        }
        
        return response;
    }
    
    public static Method toMethod(Mapping map, Object o)throws Exception{
        Method result = null;
        for(Method method : o.getClass().getDeclaredMethods()){
            if(method.getName().equals(map.getMethodName())){
                result = method;
                break;
            }
        }
        
        return result;
    }
    
    public static ModelView invokeMethodWithArgs(HttpServletRequest request, Mapping map, Object o, Method method, Gson jsonconverter)throws Exception{
        Enumeration enumeration = request.getParameterNames();
        HashMap <String, Class<?>> argInfo = new HashMap<>();
        
        // Assingning field nameparameter with it's type in the HashMap arginfo
        for(Class<?> paramTypes :method.getParameterTypes() ){
            String paramName = (String)enumeration.nextElement();
            argInfo.put(paramName, paramTypes);
        }
        
        return invoking(request, argInfo, method, o, map, jsonconverter);
    }
    
    public static ModelView invoking(HttpServletRequest request, HashMap<String, Class<?>> argInfo, Method method, Object o, Mapping map, Gson jsonconverter)throws Exception{
        
        Object[] args = new Object[argInfo.size()];
        Class<?>[] argsClass = new Class<?>[argInfo.size()];
        int i = 0;
        
        for(String key : argInfo.keySet()){
            argsClass[i] = argInfo.get(key);
            Function<String, ?> parser = PARSERS.get(argsClass[i]);
            args[i] = parser.apply(request.getParameter(key));
            i++;
        }
        
        ModelView result = (ModelView) o.getClass().getMethod(map.getMethodName(), argsClass).invoke(o, args);
        
        return result;
    }

    
}
