/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.dao.callback;

import javax.persistence.EntityManager;

/**
 *
 * @author DOUGLLASFPS
 */
public interface QueryCallBack<T> {
    T doQuery(EntityManager entityManager);
}
