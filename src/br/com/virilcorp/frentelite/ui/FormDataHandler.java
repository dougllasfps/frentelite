package br.com.virilcorp.frentelite.ui;

public interface FormDataHandler<T> {
	T populateBeanWithFormData(T t);
	void sendRegisterToForm(T t);
	void cleanForm();
}
