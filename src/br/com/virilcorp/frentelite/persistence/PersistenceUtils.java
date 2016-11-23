/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.persistence;

import javax.persistence.EntityManager;
import org.jboss.logging.Logger;

/**
 *
 * @author DOUGLLASFPS
 */
public class PersistenceUtils {

    private static final ThreadLocal<EntityManager> SESSION = new ThreadLocal<>();
    
    public static void closeEntityManagerFactory(){
    	EntityManagerSingleTon.detroyEntityManagerFactory();
    }

    public static void closeEntityManager() {
        EntityManager entityManager = SESSION.get();
        if (entityManager != null) {
            entityManager.close();
        }
        SESSION.set(null);
    }

    public static EntityManager getCurrentEntityManager() {
        Logger.getLogger(PersistenceUtils.class.getName()).log(Logger.Level.INFO, "Criando ou obtendo EntityManager");
        
        EntityManager entityManager = SESSION.get();
        
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = EntityManagerSingleTon.createEntityManager();
            SESSION.set(entityManager);
        }
        
        return entityManager;
    }

    public static void closeEntityManager(final EntityManager entityManager) {
        Logger.getLogger(PersistenceUtils.class.getName()).log(Logger.Level.INFO, "Fechando EntityManager");
        entityManager.close();
    }
}
