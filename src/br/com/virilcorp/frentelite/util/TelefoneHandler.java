package br.com.virilcorp.frentelite.util;

import br.com.virilcorp.frentelite.exception.ValidationException;

public class TelefoneHandler {
	
	public static void validate(String telefone){
		String tel = removeMasks(telefone);
		
		if(tel.length() != 11){
			throw new ValidationException("msg.tel_invalido");
		}
	}

	public static String removeMasks(String tel){
		tel = tel.replace("(", "");
		tel = tel.replace(")", "");
		tel = tel.replace("-", "");
		return tel;
	}
	
	public static String withMask(String tel){
		return "(" + tel.substring(0,2) + ")" + tel.substring(2,7) + "-" + tel.substring(7, tel.length());
	}
	
	public static void main(String[] args) {
		System.out.println(withMask("85888888888"));
	}
}
