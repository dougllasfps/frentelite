/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.persistence;

import javax.persistence.EntityManager;

/**
 *
 * @author DOUGLLASFPS
 */
public interface EntityManagerObtainer {
    EntityManager obtain();
}
