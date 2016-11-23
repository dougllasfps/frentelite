package br.com.virilcorp.frentelite.ui.component;

import br.com.virilcorp.frentelite.ui.ButtonAdapter;
import javafx.scene.control.Button;

public class NumericButton extends Button implements ButtonAdapter {

	private Integer number;
	
	public void applyLabel(){
		setText(number != null ? number.toString() : "");
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
}