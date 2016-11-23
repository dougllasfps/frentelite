package br.com.virilcorp.components;

import java.math.BigDecimal;

import br.com.virilcorp.converter.BigDecimalConverter;
import br.com.virilcorp.converter.BigDecimalMonetaryConverter;
import br.com.virilcorp.converter.BigDecimalQuantidadeConverter;
import br.com.virilcorp.converter.Converter;


public class InputBigDecimal extends InputValue<BigDecimal> {

	private BigDecimalConverter converter;
	private Integer type = BigDecimalConverter.MONETARY;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public Converter<BigDecimal> getConverter() {
		defineConverter();
		return converter;
	}
	
	private void defineConverter(){
		if(BigDecimalConverter.MONETARY.equals(this.type)){
			this.converter = new BigDecimalMonetaryConverter();
		}
		
		if(BigDecimalConverter.QUANTITY.equals(this.type)){
			this.converter = new BigDecimalQuantidadeConverter();
		}
	}
}
