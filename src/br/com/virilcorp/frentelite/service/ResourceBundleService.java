package br.com.virilcorp.frentelite.service;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleService {

	private static final String FILE_NAME = "Messages";
	
	private static final ResourceBundle instance;
	
	static{
		instance = loadDefault();
	}
	
	private static ResourceBundle loadDefault(){
		return ResourceBundle.getBundle(FILE_NAME);
	}
	
	public static ResourceBundle getInstance(){
		return instance;
	}
	
	public static String getMessage(String key){
		try{
			return instance.getString(key);
		}catch(MissingResourceException e){
			return null;
		}
	}
	
}
