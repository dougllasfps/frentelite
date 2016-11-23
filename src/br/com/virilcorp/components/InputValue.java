package br.com.virilcorp.components;

import br.com.virilcorp.converter.Converter;
import javafx.scene.control.TextField;

public abstract class InputValue<T> extends TextField {
	
	public void setValue(T value){
		setText(getConverter().toString(value));
	}
	
	public T getValue(){
		return getConverter().fromString(getText());
	}

	public abstract Converter<T> getConverter();
	
}