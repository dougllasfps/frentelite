package br.com.virilcorp.frentelite.ui.component;

import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.ui.ButtonAdapter;
import javafx.scene.control.Button;

public class ProdutoButton extends Button implements ButtonAdapter {

	private Produto produto;

	public ProdutoButton(Produto produto) {
		super(produto.getDescricao());
		this.produto = produto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}