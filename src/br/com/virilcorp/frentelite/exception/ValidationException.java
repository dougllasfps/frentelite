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
public class ValidationException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1825008647008880655L;

	public ValidationException(String msg) {
        super(msg);
    }
    
}
