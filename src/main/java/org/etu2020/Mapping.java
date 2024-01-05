/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.etu2020;

/**
 *
 * @author Miaro
 */
public class Mapping {
    String className;
    String methodName;

    public Mapping(String className, String methodName) throws Exception{
        this.setClassName(className);
        this.setMethodName(methodName);
    }
    
    

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setClassName(String className) throws Exception{
        if(className == null && className.trim().isEmpty()){
            throw new Exception("Class name given is null or empty");
        }
        this.className = className;
    }

    public void setMethodName(String methodName) throws Exception{
        if(className == null && className.trim().isEmpty()){
            throw new Exception("Method name given is null or empty");
        }
        this.methodName = methodName;
    }
    
    
}
