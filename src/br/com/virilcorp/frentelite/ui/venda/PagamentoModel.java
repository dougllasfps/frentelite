package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;

import br.com.virilcorp.converter.NumericConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PagamentoModel {

	private StringProperty formaPagamento;
	private StringProperty valorPagamento;
	private TipoPagamento tipo;
	private BigDecimal valor;
	
	public PagamentoModel(final TipoPagamento tipoPagamento, final BigDecimal valorPagamento) {
		this.tipo = (tipoPagamento);
		this.valor = (valorPagamento);
		this.formaPagamento = new SimpleStringProperty(tipoPagamento == null ? "" : tipoPagamento.getDescricao());
		this.valorPagamento = new SimpleStringProperty(valorPagamento == null ? "" : NumericConverter.formatCurrent(valorPagamento));
	}
	
	public StringProperty getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(StringProperty formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	public StringProperty getValorPagamento() {
		return valorPagamento;
	}
	public void setValorPagamento(StringProperty valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public TipoPagamento getTipo() {
		return tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}
}