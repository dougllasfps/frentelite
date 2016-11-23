package br.com.virilcorp.frentelite.balanca.exception;

public class SobreCargaPesoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SobreCargaPesoException(String msg) {
		super(msg);
	}
	
	public SobreCargaPesoException() {
		super("Sobrecarga de peso, Tente uma nova leitura do peso.");
	}

}
