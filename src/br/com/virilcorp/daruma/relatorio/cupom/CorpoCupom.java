package br.com.virilcorp.daruma.relatorio.cupom;

import br.com.virilcorp.daruma.relatorio.CorpoRelatorio;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.Venda;

public class CorpoCupom implements CorpoRelatorio {
	
	private Venda venda;
	private Delivery delivery;
	
	public CorpoCupom() {}

	public CorpoCupom(Venda venda) {
		super();
		this.venda = venda;
	}
	
	public CorpoCupom(Delivery delivery) {
		super();
		this.delivery = delivery;
		this.venda = delivery.getVenda();
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
}