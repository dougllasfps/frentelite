package br.com.virilcorp.frentelite.ui;

import br.com.virilcorp.frentelite.model.BaseModel;

public interface DataModel<T extends BaseModel> {
	T getData();
}
