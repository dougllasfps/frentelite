/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 *
 * @author DOUGLLASFPS
 */
@Filter(name = "intervaloFluxo" , condition = " data_venda between :dataInicial and :dataFinal ")
@FilterDef(name = "intervaloFluxo", parameters = {
		@ParamDef(name = "dataInicial", type = "java.util.Calendar" ),
		@ParamDef(name = "dataFinal", 	type = "java.util.Calendar" )

})
@Entity
@Table(name = "venda", schema = "ecf")
public class Venda implements Serializable , BaseModel{

	private static final long serialVersionUID = -7696768488418277041L;

	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Temporal (TemporalType.TIMESTAMP)
    @Column(name = "data_venda")
    private Calendar dataVenda;
    
    // auxiliar para as queries (GUARDA APENAS A DATA)
    @Temporal (TemporalType.DATE)
    @Column(name = "data")
    private Calendar data;
    
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;
    
    @OneToOne(mappedBy = "venda")
    private Pagamento pagamento;
    
    @OneToMany(mappedBy="venda")
    private Collection<ItemVenda> itensVenda;
    
    @Transient
    private CarrinhoVenda carrinho;
    
    @Column(name = "cancelada")
    private boolean cancelada;

    public Venda() {
    	this.carrinho = new CarrinhoVenda();
    	this.carrinho.setVenda(this);
    }

    public Venda(Integer id, Calendar dataVenda, BigDecimal valorTotal, BigDecimal valorDesconto) {
    	this();
        this.id = id;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.valorDesconto = valorDesconto;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Calendar dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        if(pagamento != null)
        	this.pagamento.setVenda(this);
    }

    public Collection<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(Collection<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
		if(itensVenda != null){
			getCarrinho().addAll(itensVenda);
		}
	}
	
	public boolean isPago(){
		return pagamento != null && pagamento.getId() != null;
	}

	public CarrinhoVenda getCarrinho() {
		return carrinho;
	}
	
	public void setCarrinho(CarrinhoVenda carrinho) {
		this.carrinho = carrinho;
	}
	
	public Calendar getData() {
		return data;
	}
	
	public void setData(Calendar data) {
		this.data = data;
	}

	@Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.dataVenda);
        hash = 31 * hash + Objects.hashCode(this.valorTotal);
        hash = 31 * hash + Objects.hashCode(this.valorDesconto);
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
        final Venda other = (Venda) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dataVenda, other.dataVenda)) {
            return false;
        }
        if (!Objects.equals(this.valorTotal, other.valorTotal)) {
            return false;
        }
        if (!Objects.equals(this.valorDesconto, other.valorDesconto)) {
            return false;
        }
        return true;
    }

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}
}