package br.com.virilcorp.daruma.relatorio;

import br.com.virilcorp.daruma.relatorio.cupom.Cabecalho;
import br.com.virilcorp.daruma.relatorio.cupom.Rodape;

public abstract class RelatorioImpressora {
	
	private Cabecalho cabecalho;
	private CorpoRelatorio corpo;
	private Rodape rodape;
	
	public Cabecalho getCabecalho() {
		return cabecalho;
	}
	public void setCabecalho(Cabecalho cabecalho) {
		this.cabecalho = cabecalho;
	}
	public CorpoRelatorio getCorpo() {
		return corpo;
	}
	public void setCorpo(CorpoRelatorio corpo) {
		this.corpo = corpo;
	}
	public Rodape getRodape() {
		return rodape;
	}
	public void setRodape(Rodape rodape) {
		this.rodape = rodape;
	}	
}