package br.com.virilcorp.components;

import java.util.Calendar;

import br.com.virilcorp.converter.CalendarConverter;
import br.com.virilcorp.converter.Converter;

public class InputCalendar extends InputValue<Calendar>{
	
	private String pattern;

	@Override
	public Converter<Calendar> getConverter() {
		return new CalendarConverter( pattern );
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}