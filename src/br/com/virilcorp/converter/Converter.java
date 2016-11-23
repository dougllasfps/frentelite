package br.com.virilcorp.converter;

public interface Converter<T>{
	
	String toString(T obj);
	
	T fromString(String value);
	
}