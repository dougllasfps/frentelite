package br.com.virilcorp.fisco.impressao.impl;

import br.com.daruma.jna.ECF;
import br.com.virilcorp.fisco.impressao.interfaces.IImpressoraDual;

public class UrmetDarumaImpressoraFiscal implements IImpressoraDual {
	
	@SuppressWarnings("unused")
	private ECF ecf;
	
	public UrmetDarumaImpressoraFiscal() {
		this.ecf = new ECF();
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		UrmetDarumaImpressoraFiscal imp = new UrmetDarumaImpressoraFiscal();
	}
}
