package br.com.virilcorp.converter;

import java.math.BigDecimal;

public class BigDecimalQuantidadeConverter extends BigDecimalConverter{
	@Override
	public String toString(BigDecimal obj) {
		try {
			return NumericConverter.formatWeight(obj);
		} catch (Exception e) {
			return null;
		}
	}
}