package br.com.virilcorp.frentelite.ui.categoria;

import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.ui.DataModel;
import javafx.beans.property.SimpleStringProperty;

public class CategoriaModel implements DataModel<Categoria> {
	
	private Categoria data;
	private SimpleStringProperty descricao;
	
	public CategoriaModel() {
	}
	
	public CategoriaModel(Categoria data) {
		this.data = data;
		String desc = ( data == null || data.getDescricao() == null ) ? "" : data.getDescricao();
		setDescricao(desc);
	}

	@Override
	public Categoria getData() {
		return data;
	}

	public SimpleStringProperty getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = new SimpleStringProperty(descricao);
	}

}
