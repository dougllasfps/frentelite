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
public abstract class WriteEventCallBack {

    public abstract int execute(EntityManager entityManager, BaseModel entity);
}
