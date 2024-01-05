/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.etu2020;

import java.util.Map;

/**
 *
 * @author miaro
 */
public class ModelView {
    
    String view;
    Map<String, Object> datas;
    
    public ModelView(String view, Map<String, Object> datas)throws Exception{
        this.setView(view);
        this.setDatas(datas);
    }
    
    public ModelView(){}

    public String getView() {
        return view;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setView(String view) throws Exception{
        if("".equals(view)){
            throw new Exception("No view has been called");
        };
        this.view = view;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }
    
    
    
}
