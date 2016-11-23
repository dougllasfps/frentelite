package br.com.virilcorp.frentelite.service;

public enum BalancaConfigKeys {

	SERIAL_PORT("balanca.serialport"),
	BAUD_RATE("balanca.baudrate"),
	DATA_BITS("balanca.databits"),
	STOP_BITS("balanca.stopbits"),
	PARITY("balanca.parity"),
	ASCI("balanca.asci"),
	BYTES_TO_READ("balanca.bytes");
	
	private String key;

	private BalancaConfigKeys(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}