package br.com.virilcorp.fisco.impressao.enums;

import br.com.virilcorp.io.system.enums.OSAlias;
import br.com.virilcorp.io.system.exceptions.UnknowSystemException;

abstract interface IFabricanteImpressora {
	
	String getLabel();
	String getInternalPackagePath();
	String getMainLibrayName(OSAlias osAlias) throws UnknowSystemException;
	
}
