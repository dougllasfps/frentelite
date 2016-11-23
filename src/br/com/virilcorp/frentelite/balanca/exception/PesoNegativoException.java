package br.com.virilcorp.frentelite.balanca.exception;

public class PesoNegativoException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PesoNegativoException(String msg) {
		super(msg);
	}
	
	public PesoNegativoException() {
		super("Peso Negativo, Tente uma nova leitura do peso.");
	}
}
