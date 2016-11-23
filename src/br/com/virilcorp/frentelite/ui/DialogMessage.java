/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import br.com.virilcorp.frentelite.service.ResourceBundleService;
import javafx.scene.control.Alert;

/**
 *
 * @author DOUGLLASFPS
 */
public class DialogMessage {
	
	private ResourceBundle bundle;
	
	public DialogMessage() {
	}
	
	public DialogMessage(ResourceBundle bundle){
		this.bundle = bundle;
	}
	
    public void addMessage(String msg, Alert.AlertType alertType){
    	String finalMessage = null;
    	
    	try {
			if(bundle != null){
				String messageFromBundle = bundle.getString(msg);
				
				if(messageFromBundle == null){
					finalMessage = capturateFragmentedMessage(msg);
				}else{
					finalMessage = messageFromBundle;
				}
			}
		} catch (MissingResourceException e) {
			finalMessage = msg;
		}
    	
        Alert alert = new Alert(alertType);
        alert.setTitle("Mensagem");
        alert.setContentText(finalMessage == null ? msg : finalMessage);
        alert.showAndWait();
    }

	private String capturateFragmentedMessage(String msg) {
		String[] parts = msg.split(" ");
		
		StringBuilder sb = new StringBuilder();
		
		if(parts == null){
			return msg;
		}
		
		for (int x = 0 ; x < parts.length ; x++) {
			String message;
			
			try {
				message = bundle.getString(parts[x]);
				sb.append(message + " ");
			} catch (MissingResourceException e) {
				sb.append( parts[x] + " ");
			}
			
		}
		
		return sb.toString();
	}
    
    public void addInfoMessage(String msg){
        addMessage(msg, Alert.AlertType.INFORMATION);
    }
    
    public void addErrorMessage(String msg){
        addMessage(msg, Alert.AlertType.ERROR);
    }

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public static void main(String[] args) {
		DialogMessage message = new DialogMessage(ResourceBundleService.getInstance());
		String capturateFragmentedMessage = message.capturateFragmentedMessage("label.descricao teste msg.produto_nao_encontrado");
		System.out.println(capturateFragmentedMessage);
	}
}
