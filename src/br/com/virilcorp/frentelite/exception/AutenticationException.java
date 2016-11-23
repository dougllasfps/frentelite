/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.exception;

/**
 *
 * @author DOUGLLASFPS
 */
public class AutenticationException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1752439646100064159L;

	public AutenticationException(String msg) {
        super(msg);
    }
    
}
