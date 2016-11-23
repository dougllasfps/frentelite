/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.dao.callback;

import br.com.virilcorp.frentelite.model.BaseModel;
import javax.persistence.EntityManager;

/**
 *
 * @author DOUGLLASFPS
 */
public enum WriteEvent { 

    PERSIST {
        @Override
        public WriteEventCallBack getCallBack() {
            return new WriteEventCallBack() {
                @Override
                public int execute(EntityManager entityManager, BaseModel entity) {
                   entityManager.persist(entity);
                   return entity.getId();
                }
            };
        }
    },
    UPDATE {
        @Override
        public WriteEventCallBack getCallBack() {
            return new WriteEventCallBack() {
                @Override
                public int execute(EntityManager entityManager,BaseModel entity) {
                   entityManager.merge(entity);
                   return entity.getId();
                }
            };
        }
    },
    DELETE {
        @Override
        public WriteEventCallBack getCallBack() {
            return new WriteEventCallBack() {
                @Override
                public int execute(EntityManager entityManager,BaseModel entity) {
                   entity = entityManager.merge(entity);
                   entityManager.remove(entity);
                   return 1;
                }
            };
        }
    };
    
    public abstract WriteEventCallBack getCallBack();
  
}
