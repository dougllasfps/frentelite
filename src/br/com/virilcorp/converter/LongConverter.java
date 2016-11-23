package br.com.virilcorp.converter;

public class LongConverter implements Converter<Long> {

	@Override
	public String toString(Long obj) {
		return obj == null ? null : obj.toString();
	}

	@Override
	public Long fromString(String value) {
		return value == null ? null : Long.valueOf(value);
	}
}