/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.persistence;

/**
 *
 * @author DOUGLLASFPS
 */
public class EntityManagerObtainerFactory {
    private static final EntityManagerObtainer obj;
    
    static{
        obj = new EntityManagerObtainerImpl();
    }
    
    public static EntityManagerObtainer createEntityManagerObtainer(){
        return obj;
    }
}
