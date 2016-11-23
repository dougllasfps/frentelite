package br.com.virilcorp.frentelite.exception;

public class FotoGrandeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5151497818201807065L;
	
	public FotoGrandeException(String msg){
		super(msg);
	}
	
	public FotoGrandeException() {
		super("Arquivo muito grande.");
	}

}
