package br.com.virilcorp.frentelite.balanca.exception;

public class BalancaInativaException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalancaInativaException() {
		super("Balan�a Inativa.");
	}

	public BalancaInativaException(String string) {
		super(string);
	}
}
