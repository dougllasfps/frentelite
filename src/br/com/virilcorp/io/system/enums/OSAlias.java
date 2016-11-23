package br.com.virilcorp.io.system.enums;

public enum OSAlias {
	
	//XXX MEMBERS
	WIN32("win32"),
	WIN64("win64"),
	LIN32("lin32"),
	LIN64("lin64"),
	UNI32("uni32"),
	UNI64("uni64"),
	MAC32("mac32"),
	MAC64("mac64");
	
	//XXX PROPERTIES
	private String alias;
	
	//XXX CONSTRUCTOR
	private OSAlias(String alias) {
		this.alias = alias;
	}
	
	//XXX GETTERS
	public String getAlias() {
		return new String(alias);
	}
}
