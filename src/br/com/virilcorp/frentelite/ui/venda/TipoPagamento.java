package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;

import br.com.virilcorp.frentelite.model.Pagamento;

public enum TipoPagamento {
	
	DINHEIRO("Dinheiro"), 
	CARTAO("Cart�o de Cr�dito"),
	DEBITO("Cart�o de D�bito");
	
	private String descricao;

	private TipoPagamento(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void adicionarValor(final Pagamento pagamento, BigDecimal valor){

		switch(this){
			case DINHEIRO:{
				
				if(pagamento.getValorDinheiro() == null){
					pagamento.setValorDinheiro(BigDecimal.ZERO);
				}
				
				pagamento.setValorDinheiro(pagamento.getValorDinheiro().add(valor));
				break;
			}
			
			case CARTAO: {
				
				if(pagamento.getValorCartao() == null){
					pagamento.setValorCartao(BigDecimal.ZERO);
				}
				
				pagamento.setValorCartao(pagamento.getValorCartao().add(valor));
				break;
			}
			
			case DEBITO: {
				
				if(pagamento.getValorCartao() == null){
					pagamento.setValorDebito(BigDecimal.ZERO);
				}
				
				pagamento.setValorDebito(pagamento.getValorDebito().add(valor));
				break;
			}
		}
		
	}
}