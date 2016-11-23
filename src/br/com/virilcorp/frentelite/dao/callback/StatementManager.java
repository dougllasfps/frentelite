/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.dao.callback;

import br.com.virilcorp.frentelite.model.BaseModel;
import br.com.virilcorp.frentelite.persistence.EntityManagerObtainer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 *
 * @author DOUGLLASFPS
 */
public class StatementManager {
    
    private final EntityManagerObtainer entityManagerObtainer;

    public StatementManager(EntityManagerObtainer obtainer) {
        this.entityManagerObtainer = obtainer;
    }
    
    public Filter enableFilter(String filterName){
    	EntityManager entityManager = this.entityManagerObtainer.obtain();
    	if(entityManager != null){
    		Session session = entityManager.unwrap(Session.class);
    		return session.enableFilter(filterName);
    	}
    	return null;
    }
        
    public int executeUpdate(WriteEvent writeEvent, BaseModel entity){
        
        EntityManager entityManager = entityManagerObtainer.obtain();
        
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        
        try{
            writeEvent.getCallBack().execute(entityManager, entity);
            transaction.commit();
            return 1;
        }catch(Exception e){
            transaction.rollback();
            throw new RuntimeException("Erro ao executar o write event.");
        }finally{
            entityManager.close();
        }
    }
    
    public int executeCallBack(TransactionCallBack callBack){
        EntityManager entityManager = entityManagerObtainer.obtain();
        
        entityManager.getTransaction().begin();
        try{
            callBack.doInTransaction(entityManager);
            entityManager.getTransaction().commit();
            return 1;
        }catch(Exception e){
        	e.printStackTrace();
            entityManager.getTransaction().rollback();
            return 0;
        }finally{
            entityManager.close();
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T executeQuery(QueryCallBack query){
        EntityManager entityManager = null;
        try{
            entityManager = entityManagerObtainer.obtain();
            return (T) query.doQuery(entityManager);
        }finally{
            if(entityManager != null){
                entityManager.close();
            }
        }
    }
    
}
