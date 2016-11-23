package br.com.virilcorp.fisco.impressao.enums;

import br.com.virilcorp.io.system.enums.OSAlias;
import br.com.virilcorp.io.system.exceptions.UnknowSystemException;

public enum FabricanteImpressora implements IFabricanteImpressora {
	
	//XXX MEMBERS
	URMET_DARUMA("Urmet Daruma", "urmetdaruma"){
		@Override
		public String getMainLibrayName(OSAlias osAlias) throws UnknowSystemException {
			switch (osAlias) {
			case WIN64:
			case WIN32:
				return "DarumaFrameWork.dll";
			case LIN32:
			case LIN64:
				return "libDarumaFramework.so";
			case MAC64:
			case MAC32:
			default:
				throw new UnknowSystemException("unsupported system");
			}
		}
	};
	
	//XXX PROPERTIES
	private final String label;
	private final String packagePath;
	
	//XXX CONSTRUCTORS
	private FabricanteImpressora(String label, String packagePath) {
		this.label = label;
		this.packagePath = packagePath;
	}
	
	@Override
	public String getLabel() {
		return new String(label);
	}
	
	@Override
	public String getInternalPackagePath() {
		return new String(packagePath);
	}
	
	@Override
	public String getMainLibrayName(OSAlias osAlias) throws UnknowSystemException {
		throw new UnsupportedOperationException("define implementation in enum member for this method");
	}
}
