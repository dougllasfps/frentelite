package br.com.virilcorp.fisco.impressao.interfaces;

import java.io.IOException;

import br.com.virilcorp.fisco.impressao.enums.FabricanteImpressora;

public interface IImpressoraProvider {
	
	< I extends IImpressoraDual> I  getInstance(FabricanteImpressora fabImp) throws IOException;
}
