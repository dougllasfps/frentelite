package br.com.virilcorp.frentelite.environment;

import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.exception.DarumaImpressaoException;
import br.com.virilcorp.frentelite.balanca.BalancaHandler;

public class DefaultFrenteLiteInitializer implements FrenteLiteInitializer {

	@Override
	public void inicializaImpressora() {
		try {
			DarumaDualService.getInstance().inicializarImpressora();;
		} catch (DarumaImpressaoException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void inicializaBalanca() {
		try {
			BalancaHandler.getInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}