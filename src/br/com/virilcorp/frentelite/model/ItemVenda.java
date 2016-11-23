/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.virilcorp.converter.NumericConverter;

/**
 *
 * @author DOUGLLASFPS
 */
@Entity
@Table( name = "item_venda", schema = "ecf" )
public class ItemVenda implements Serializable, BaseModel {

	private static final long serialVersionUID = -2786957686710550974L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_produto")
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "id_venda")
	private Venda venda;

	@Column(name = "quantidade")
	private BigDecimal quantidade;
	
	@Column(name = "valor_custo_unitario")
	private BigDecimal valorCustoUnitario;
	
	@Column(name = "valor_custo_total")
	private BigDecimal valorCustoTotal;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	@Column(name = "valor_venda")
	private BigDecimal valorVenda;

	@Column(name = "valor_desconto")
	private BigDecimal valorDesconto;

	public ItemVenda() { }

	public ItemVenda( Integer id, Produto produto, Venda venda, BigDecimal quantidade, BigDecimal valorVenda, BigDecimal valorDesconto, BigDecimal valorUnitario ) {
		
		this.id = id;
		this.produto = produto;
		this.venda = venda;
		this.quantidade = quantidade;
		this.valorVenda = valorVenda;
		this.valorDesconto = valorDesconto;
		this.valorUnitario = valorUnitario;
		
		this.valorCustoUnitario = produto.getValorCusto();
		this.valorCustoTotal = quantidade.multiply(produto.getValorCusto());
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	@Override
	public String toString() {
		String pipe = " | ";
		return quantidade + pipe + produto.getDescricao() + pipe + NumericConverter.formatCurrent(valorVenda);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Objects.hashCode(this.id);
		hash = 97 * hash + Objects.hashCode(this.produto);
		hash = 97 * hash + Objects.hashCode(this.venda);
		hash = 97 * hash + Objects.hashCode(this.quantidade);
		hash = 97 * hash + Objects.hashCode(this.valorVenda);
		hash = 97 * hash + Objects.hashCode(this.valorDesconto);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final ItemVenda other = (ItemVenda) obj;
		
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.produto, other.produto)) {
			return false;
		}
		if (!Objects.equals(this.venda, other.venda)) {
			return false;
		}
		if (!Objects.equals(this.quantidade, other.quantidade)) {
			return false;
		}
		if (!Objects.equals(this.valorVenda, other.valorVenda)) {
			return false;
		}
		if (!Objects.equals(this.valorDesconto, other.valorDesconto)) {
			return false;
		}
		return true;
	}

	public class ItemVendaBuilder {

		private Integer id;
		private Produto produto;
		private Venda venda;
		private BigDecimal quantidade;
		private BigDecimal valorVenda;
		private BigDecimal valorDesconto;
		private BigDecimal valorUnitario;

		public final ItemVenda build() {
			return new ItemVenda(id, produto, venda, quantidade, valorVenda, valorDesconto, valorUnitario);
		}

		public ItemVendaBuilder id(Integer id) {
			this.id = id;
			return this;
		}
		
		public ItemVendaBuilder valorUnitario(BigDecimal valorUnitario){
			this.valorUnitario = valorUnitario;
			return this;
		}

		public ItemVendaBuilder produto(Produto produto) {
			this.produto = produto;
			return this;
		}

		public ItemVendaBuilder venda(Venda venda) {
			this.venda = venda;
			return this;
		}

		public ItemVendaBuilder quantidade(BigDecimal quantidade) {
			this.quantidade = quantidade;
			return this;
		}
		
		public ItemVendaBuilder valorVenda(BigDecimal valorVenda) {
			this.valorVenda = valorVenda;
			return this;
		}
		
		public ItemVendaBuilder valorDesconto(BigDecimal valorDesconto) {
			this.valorDesconto = valorDesconto;
			return this;
		}
	}

	public BigDecimal getValorCustoUnitario() {
		return valorCustoUnitario;
	}

	public void setValorCustoUnitario(BigDecimal valorCustoUnitario) {
		this.valorCustoUnitario = valorCustoUnitario;
	}

	public BigDecimal getValorCustoTotal() {
		return valorCustoTotal;
	}

	public void setValorCustoTotal(BigDecimal valorCustoTotal) {
		this.valorCustoTotal = valorCustoTotal;
	}

}
