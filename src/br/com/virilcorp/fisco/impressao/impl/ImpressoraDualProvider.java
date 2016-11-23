package br.com.virilcorp.fisco.impressao.impl;

import java.io.IOException;

import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.fisco.impressao.abstractions.AbstractImpressoraDualProvider;
import br.com.virilcorp.fisco.impressao.enums.FabricanteImpressora;
import br.com.virilcorp.fisco.impressao.interfaces.IImpressoraDual;

public final class ImpressoraDualProvider extends AbstractImpressoraDualProvider {

	@Override
	protected IImpressoraDual createInstance(FabricanteImpressora fabImp) throws IOException {
		switch (fabImp) {
		case URMET_DARUMA:
			return DarumaDualService.getInstance();
		default:
			throw new IOException("Unknow Fabricante Impressora");
		}
	}
}