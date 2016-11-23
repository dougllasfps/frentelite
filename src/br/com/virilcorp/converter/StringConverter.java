package br.com.virilcorp.converter;

public class StringConverter implements Converter<String> {
	@Override
	public String toString(String obj) {
		return obj;
	}

	@Override
	public String fromString(String value) {
		return value;
	}
}