package br.com.virilcorp.components;

import br.com.virilcorp.converter.Converter;
import br.com.virilcorp.converter.IntegerConverter;

public class InputInteger extends InputValue<Integer> {
	
	private IntegerConverter converter;
	
	@Override
	public Converter<Integer> getConverter() {
		if(converter == null)
			converter = new IntegerConverter();
		return converter;
	}
}