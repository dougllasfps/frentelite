package br.com.virilcorp.frentelite.ui.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FXPropertyUtils {

	public static StringProperty parse(String string){
		if(string == null){
			string = "";
		}
		return new SimpleStringProperty(string);
	}
}
