package br.com.virilcorp.converter;

import br.com.virilcorp.frentelite.util.StringUtils;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public String toString(Integer obj) {
		if(obj  == null){
			return "";
		}
		return obj.toString();
	}

	@Override
	public Integer fromString(String value) {
		if(StringUtils.isNullOrEmpty(value)){
			return null;
		}
		
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}