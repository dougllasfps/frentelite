package br.com.virilcorp.converter;

import java.math.BigDecimal;

public class BigDecimalMonetaryConverter extends BigDecimalConverter {
	@Override
	public String toString(BigDecimal obj) {
		if(obj == null){
			return "";
		}
		return NumericConverter.formatCurrent(obj);
	}
}
