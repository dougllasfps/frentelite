package br.com.virilcorp.frentelite.ui;

import br.com.virilcorp.frentelite.model.BaseModel;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public interface CrudView<T extends BaseModel> extends FormDataHandler<T>{
	void handleNewRegister(ActionEvent evt);
	void handleSave(ActionEvent evt);
	void handleSearch(ActionEvent evt);
	void handleDelete(ActionEvent evt);
	void onRowSelect(MouseEvent evt);
}
