package br.com.virilcorp.daruma.relatorio.gerenciamento.abertura;

import br.com.virilcorp.daruma.relatorio.CorpoRelatorio;
import br.com.virilcorp.frentelite.model.FluxoCaixa;

public class CorpoRelatorioAbertura implements CorpoRelatorio{
	
	private FluxoCaixa abertura;

	public FluxoCaixa getAbertura() {
		return abertura;
	}

	public void setAbertura(FluxoCaixa abertura) {
		this.abertura = abertura;
	}
}