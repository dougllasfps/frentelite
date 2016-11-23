package br.com.virilcorp.frentelite.service;

import java.util.Optional;

import br.com.virilcorp.converter.Converter;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class DialogFactory {

	public static <T> ChoiceDialog<T> createChoiceDialog(ObservableList<T> choices, String title, String headerText, String contentText){
		ChoiceDialog<T> dialog = new ChoiceDialog<T>(choices.get(0), choices);
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		return dialog;
	}
	
	public static boolean showCancelConfirmDialog(){
		String contentText = ResourceBundleService.getMessage("msg.certeza_cancelamento");
		return showConfirmDialog(contentText);
	}
	
	public static boolean showConfirmDialog(String contentText){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText(contentText);
		Optional<ButtonType> result = alert.showAndWait();
		return result.get() != null && result.get() == ButtonType.OK;
	}
	
	public static <T> T showInputDialog(Converter<T> converter, String contentText){
		return showInputDialog(converter, null, null, contentText);
	}
	
	public static <T> T showInputMonetaryDialog(Converter<T> converter, String contentText){
		return showInputMonetaryDialog(converter, null, null, contentText);
	}
	
	public static <T> T showInputDialog(Converter<T> converter, String title, String headerText, String contentText){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		
		Optional<String> result = dialog.showAndWait();
		
		if(result.get() == null){
			return null;
		}
		
		return converter.fromString(result.get());
	}
	
	public static <T> T showInputMonetaryDialog(Converter<T> converter, String title, String headerText, String contentText){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		
		Optional<String> result = dialog.showAndWait();
		
		TextField editor = dialog.getEditor();
		MaskUtils.monetaryField(editor);
		
		if(result.get() == null){
			return null;
		}
		
		return converter.fromString(result.get());
	}
}