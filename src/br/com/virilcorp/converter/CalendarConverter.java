package br.com.virilcorp.converter;

import java.util.Calendar;

import javafx.util.StringConverter;

public class CalendarConverter extends StringConverter<Calendar> implements Converter<Calendar>{

	public static final int DATE = 0;
	public static final int HOUR = 1;
	public static final int DATE_TIME = 2;
	
	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String HOUR_PATTERN = "HH:mm:ss";
	public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	
	private int dateType = DATE;
	private String pattern;
	
	public CalendarConverter() {
	}
	
	public CalendarConverter( int dateType ) {
		this.dateType = dateType;
	}
	
	public CalendarConverter( String pattern ) {
		this.pattern = pattern;
	}
	
	@Override
	public String toString(Calendar object) {
		
		if(pattern != null){
			return DateTimeUtils.format(object, pattern);
		}
		
		if(DATE_TIME == this.dateType){
			return DateTimeUtils.format(object, DATE_TIME_PATTERN);
		}
		
		if(HOUR == this.dateType){
			return DateTimeUtils.format(object, HOUR_PATTERN);
		}
		
		return DateTimeUtils.format(object, DATE_PATTERN);
	}

	@Override
	public Calendar fromString(String dataString) {
		
		if(pattern != null){
			return DateTimeUtils.toCalendar(dataString, pattern);
		}
		
		if(DATE_TIME == this.dateType){
			return DateTimeUtils.toCalendar(dataString, DATE_TIME_PATTERN);
		}
		
		if(HOUR == this.dateType){
			return DateTimeUtils.toCalendar(dataString, HOUR_PATTERN);
		}
		
		return DateTimeUtils.toCalendar(dataString, DATE_PATTERN);
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}
}