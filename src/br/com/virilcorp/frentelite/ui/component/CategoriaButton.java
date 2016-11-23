package br.com.virilcorp.frentelite.ui.component;

import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.ui.ButtonAdapter;
import javafx.scene.control.Button;

public class CategoriaButton extends Button implements ButtonAdapter{

	private Categoria categoria;
	
	public CategoriaButton(Categoria categoria) {
		super(categoria.getDescricao());
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}