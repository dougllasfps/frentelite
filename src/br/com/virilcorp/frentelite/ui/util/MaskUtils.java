/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.util;

import static java.awt.Event.F1;
import static java.awt.Event.F10;
import static java.awt.Event.F11;
import static java.awt.Event.F12;
import static java.awt.Event.F2;
import static java.awt.Event.F3;
import static java.awt.Event.F4;
import static java.awt.Event.F5;
import static java.awt.Event.F6;
import static java.awt.Event.F7;
import static java.awt.Event.F8;
import static java.awt.Event.F9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author DOUGLLASFPS
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MaskUtils {
     private static List<KeyCode> ignoreKeyCodes;

    static {
        ignoreKeyCodes = new ArrayList( Arrays.asList( F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12 ) );
    }
    
    public static void upperCase(final TextField... textFields){
    	for (TextField textField : textFields) {
			upperCase(textField);
		}
    }
    
    public static void upperCase(final TextField textField){
    	javafx.util.StringConverter<String> converter = new javafx.util.StringConverter<String>() {
			@Override
			public String toString(String s) {
				if(s == null){
					return "";
				}
				return s.toUpperCase();
			}
			@Override
			public String fromString(String s) {
				if(s == null){
					return "";
				}
				return s.toUpperCase();
			}
		};
    	
    	TextFormatter<String> formatter = new TextFormatter<String>(converter);
    	textField.setTextFormatter(formatter);
    }

    public static void ignoreKeys(final TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (ignoreKeyCodes.contains(keyEvent.getCode())) {
                    keyEvent.consume();
                }
            }
        });
    }

    /**
     * Monta a mascara para Data (dd/MM/yyyy).
     *
     * @param textField TextField
     */
    public static void dateField(final TextField textField) {
        maxField(textField, 10);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < 11) {
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
                    value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)", "$1/$2/$3");
                    textField.setText(value);
                    positionCaret(textField);
                }
            }
        });
    }

    /**
     * Campo que aceita somente numericos.
     *
     * @param textField TextField
     */
    public static void numericField(final TextField textField) {
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    char ch = textField.getText().charAt(oldValue.intValue());
                    if (!(ch >= '0' && ch <= '9')) {
                        textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                    }
                }
            }
        });
    }
    
    public static void numericField(final TextField... textField) {
        for (TextField textField1 : textField) {
            numericField(textField1);
        }
    }
    
    public static void monetaryField(final TextField... textField) {
        for (TextField textField1 : textField) {
            monetaryField(textField1);
        }
    }

    /**
     * Monta a mascara para Moeda.
     *
     * @param textField TextField
     */
    public static void monetaryField(final TextField textField) {
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String value = textField.getText();
                value = value.replaceAll("[^0-9]", "");
                value = value.replaceAll("([0-9]{1})([0-9]{14})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{11})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{8})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{5})$", "$1.$2");
                value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
                textField.setText(value);
                positionCaret(textField);

                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (newValue.length() > 17)
                            textField.setText(oldValue);
                    }
                });
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
                if (!fieldChange) {
                    final int length = textField.getText().length();
                    if (length > 0 && length < 3) {
                        textField.setText(textField.getText() + "00");
                    }
                }
            }
        });
    }
    
    /**
     * Monta a mascara para Moeda.
     *
     * @param textField TextField
     */
	public static void weightField(final TextField textField) {
		textField.setAlignment(Pos.CENTER_RIGHT);
		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				try {
					String value = textField.getText();
					value = value.replaceAll("[^0-9]", "");
					value = value.replaceAll("([0-9]{1})([0-9]{15})$", "$1.$2");
					value = value.replaceAll("([0-9]{1})([0-9]{12})$", "$1.$2");
					value = value.replaceAll("([0-9]{1})([0-9]{9})$", "$1.$2");
					value = value.replaceAll("([0-9]{1})([0-9]{6})$", "$1.$2");
					value = value.replaceAll("([0-9]{1})([0-9]{3})$", "$1,$2");
					textField.setText(value);
					positionCaret(textField);

					textField.textProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> observableValue, String oldValue,
								String newValue) {
							if (newValue.length() > 17)
								textField.setText(oldValue);
						}
					});
				} catch (Exception e) {
					//do nothing
				}
			}

		});

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean,
					Boolean fieldChange) {
				if (!fieldChange) {
					final int length = textField.getText().length();
					if (length > 0 && length < 3) {
						textField.setText(textField.getText() + "000");
					}
				}
			}
		});
	}

    /**
     * Monta as mascara para CPF/CNPJ. A mascara eh exibida somente apos o campo perder o foco.
     *
     * @param textField TextField
     */
    public static void cpfCnpjField(final TextField textField) {

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
                String value = textField.getText();
                if (!fieldChange) {
                    if (textField.getText().length() == 11) {
                        value = value.replaceAll("[^0-9]", "");
                        value = value.replaceFirst("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})$", "$1.$2.$3-$4");
                    }
                    if (textField.getText().length() == 14) {
                        value = value.replaceAll("[^0-9]", "");
                        value = value.replaceFirst("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})$", "$1.$2.$3/$4-$5");
                    }
                }
                textField.setText(value);
                if (textField.getText() != value) {
                    textField.setText("");
                    textField.insertText(0, value);
                }

            }
        });

        maxField(textField, 18);
    }

    /**
     * Monta a mascara para os campos CNPJ.
     *
     * @param textField TextField
     */
    public static void cnpjField(final TextField textField) {
        maxField(textField, 18);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String value = textField.getText();
                value = value.replaceAll("[^0-9]", "");
                value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
                value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2.$3");
                value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
                value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
                textField.setText(value);
                positionCaret(textField);
            }
        });

    }

    /**
     * Devido ao incremento dos caracteres das mascaras eh necessario que o cursor sempre se posicione no final da string.
     *
     * @param textField TextField
     */
    private static void positionCaret(final TextField textField) {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	try {
			    		textField.positionCaret(textField.getText().length());
			    	} catch (Exception e) {
			    		
			    	}
			    }
			});
    }

    /**
     * @param textField TextField.
     * @param length    Tamanho do campo.
     */
    public static void maxField(final TextField textField, final Integer length) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.length() > length)
                    textField.setText(oldValue);
            }
        });
    }
    
    /**
     * mascara para telefone
     */
    public static void telCelularField(final TextField textField) {
    	 textField.lengthProperty().addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                 String value = textField.getText();
                 value = value.replaceAll("[^0-9]", "");
                 textField.setText(value);
                 positionCaret(textField);
             }
         });
    }
    
    /**
     * v=v.replace(/\D/g,"");             //Remove tudo o que não é dígito
    v=v.replace(/^(\d{2})(\d)/g,"($1) $2"); //Coloca parênteses em volta dos dois primeiros dígitos
    v=v.replace(/(\d)(\d{4})$/,"$1-$2");    //Coloca hífen entre o quarto e o quinto dígitos
     */
}
