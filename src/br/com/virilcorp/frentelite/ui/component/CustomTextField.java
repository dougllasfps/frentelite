package br.com.virilcorp.frentelite.ui.component;

import javafx.scene.control.TextField;

public class CustomTextField extends TextField {

	private final int limit;

	public CustomTextField(int limit) {
		this.limit = limit;
	}

	@Override
	public void replaceText(int start, int end, String text) {
		super.replaceText(start, end, text);
		verify();
	}

	@Override
	public void replaceSelection(String text) {
		super.replaceSelection(text);
		verify();
	}

	private void verify() {
		if (getText().length() > limit) {
			setText(getText().substring(0, limit));
		}

	}
}
