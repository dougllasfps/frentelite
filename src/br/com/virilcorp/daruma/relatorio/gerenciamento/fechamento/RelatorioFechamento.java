package br.com.virilcorp.daruma.relatorio.gerenciamento.fechamento;

import br.com.virilcorp.daruma.relatorio.RelatorioImpressora;

public class RelatorioFechamento extends RelatorioImpressora {

	private CorpoRelatorioFechamento corpo;

	public CorpoRelatorioFechamento getCorpo() {
		return corpo;
	}

	public void setCorpo(CorpoRelatorioFechamento corpo) {
		this.corpo = corpo;
	}
}