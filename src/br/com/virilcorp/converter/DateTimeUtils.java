package br.com.virilcorp.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.virilcorp.frentelite.util.StringUtils;

public class DateTimeUtils {
	public static Calendar toCalendar(String dataString, String pattern){
		if(StringUtils.isNullOrEmpty(dataString)){
			return null;
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern == null ? "dd/MM/yyyy" : pattern);
			Date data = sdf.parse(dataString);
			Calendar instance = Calendar.getInstance();
			instance.setTime(data);
			return instance;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static Calendar toCalendar(String dataString){
		return toCalendar(dataString, null);
	}
	
	public static String format(Calendar data, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern == null ? "dd/MM/yyyy" : pattern);
		return sdf.format(data.getTime());
	}
	
	public static String format(Calendar data){
		return format(data, null);
	}
}