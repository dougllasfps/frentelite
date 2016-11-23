/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author DOUGLLASFPS
 */
@Entity
@Table(name = "pagamento" , schema = "ecf")
public class Pagamento implements BaseModel, Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_venda")
    private Venda venda;
    
    @Column(name = "valor_dinheiro")
    private BigDecimal valorDinheiro;
    
    @Column(name = "valor_cartao")
    private BigDecimal valorCartao;
    
    @Column(name = "valor_debito")
    private BigDecimal valorDebito;
    
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public BigDecimal getValorDinheiro() {
		return valorDinheiro;
	}

	public void setValorDinheiro(BigDecimal valorDinheiro) {
		this.valorDinheiro = valorDinheiro;
	}

	public BigDecimal getValorCartao() {
		return valorCartao;
	}

	public void setValorCartao(BigDecimal valorCartao) {
		this.valorCartao = valorCartao;
	}

	public BigDecimal getValorDebito() {
		return valorDebito;
	}

	public void setValorDebito(BigDecimal valorDebito) {
		this.valorDebito = valorDebito;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valorCartao == null) ? 0 : valorCartao.hashCode());
		result = prime * result + ((valorDebito == null) ? 0 : valorDebito.hashCode());
		result = prime * result + ((valorDesconto == null) ? 0 : valorDesconto.hashCode());
		result = prime * result + ((valorDinheiro == null) ? 0 : valorDinheiro.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valorCartao == null) {
			if (other.valorCartao != null)
				return false;
		} else if (!valorCartao.equals(other.valorCartao))
			return false;
		if (valorDebito == null) {
			if (other.valorDebito != null)
				return false;
		} else if (!valorDebito.equals(other.valorDebito))
			return false;
		if (valorDesconto == null) {
			if (other.valorDesconto != null)
				return false;
		} else if (!valorDesconto.equals(other.valorDesconto))
			return false;
		if (valorDinheiro == null) {
			if (other.valorDinheiro != null)
				return false;
		} else if (!valorDinheiro.equals(other.valorDinheiro))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}
	
}