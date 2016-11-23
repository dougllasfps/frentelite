/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.context;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DOUGLLASFPS
 */
public class Session {
    private static Map<String,Object> SESSION_OBJECTS;
    
    static{
        SESSION_OBJECTS = new HashMap<String, Object>();
    }
    
    public static void put(String name, Object value){
        SESSION_OBJECTS.put(name, value);
    }
    
    public static Object get(String name){
        return SESSION_OBJECTS.get(name);
    }
    
    public static void remove(String name){
        SESSION_OBJECTS.remove(name);
    }
}
