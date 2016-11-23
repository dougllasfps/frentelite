package br.com.virilcorp.daruma.relatorio.cupom;

import br.com.virilcorp.daruma.relatorio.RelatorioImpressora;

public class Cupom extends RelatorioImpressora{
	
	private CorpoCupom corpo;
	
	public CorpoCupom getCorpo() {
		return corpo;
	}
	
	public void setCorpo(CorpoCupom corpo) {
		this.corpo = corpo;
	}
}