package br.com.virilcorp.frentelite.balanca.exception;

public class PesoInstavelException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PesoInstavelException(String msg) {
		super(msg);
	}
	
	public PesoInstavelException() {
		super("Peso Inst�vel, Tente uma nova leitura do peso.");
	}
}
