package br.com.virilcorp.converter;

import java.math.BigDecimal;

import br.com.virilcorp.frentelite.util.StringUtils;

public abstract class BigDecimalConverter implements Converter<BigDecimal> {
	
	public static final Integer MONETARY = 0;
	public static final Integer QUANTITY = 1;

	@Override
	public BigDecimal fromString(String value) {
		
		if(StringUtils.isNullOrEmpty(value)){
			return null;
		}
		
		try {
			return NumericConverter.convertBigDecimal(value);
		} catch (Exception e) {
			return null;
		}
	}
}
