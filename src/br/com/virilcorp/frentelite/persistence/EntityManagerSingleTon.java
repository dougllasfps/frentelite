/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.virilcorp.frentelite.ui.DialogMessage;

/**
 *
 * @author DOUGLLASFPS
 */
public class EntityManagerSingleTon {
    
    private static final EntityManagerFactory FACTORY;
    
    static{
    	Properties p = new Properties();
    	
    	try (InputStream file = EntityManagerSingleTon.class.getResourceAsStream("/project.properties");){
			p.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	EntityManagerFactory emf = null;
    	
    	try{
    		emf = Persistence.createEntityManagerFactory("FrenteLitePU", p);
    	}catch(Exception e){
    		new DialogMessage().addErrorMessage("Erro ao tentar acessar o banco de dados.");
    		System.exit(0);
    	}
    	
    	FACTORY = emf;
    }
    
    public static EntityManager createEntityManager(){
        return FACTORY.createEntityManager();
    }
    
    public static void detroyEntityManagerFactory(){
    	if(FACTORY.isOpen()){
    		FACTORY.close();
    	}
    }
}
