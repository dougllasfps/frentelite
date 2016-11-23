package br.com.virilcorp.daruma.relatorio.gerenciamento.abertura;

import br.com.virilcorp.daruma.relatorio.RelatorioImpressora;

public class RelatorioAbertura extends RelatorioImpressora {

	private CorpoRelatorioAbertura corpo;

	public CorpoRelatorioAbertura getCorpo() {
		return corpo;
	}

	public void setCorpo(CorpoRelatorioAbertura corpo) {
		this.corpo = corpo;
	}
}