/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.virilcorp.frentelite.ui.component.ProdutoButton;
import javafx.scene.image.Image;

/**
 *
 * @author DOUGLLASFPS
 */
@Entity
@Table(name = "produto", schema = "ecf")
public class Produto implements Serializable, BaseModel, FotoHolder {

	private static final long serialVersionUID = 9009784900751797678L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private String codigo;
    
    @Column(name = "descricao")
    private String descricao;
    
    @Column(name = "valor_custo")
    private BigDecimal valorCusto;
    
    @Column(name = "valor_venda")
    private BigDecimal valorVenda;
    
    @Column(name = "qtd_estoque")
    private Integer quantidadeEstoque;
    
    @Column(name = "qtd_estoque_minimo")
    private Integer estoqueMinimo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "medida")
    private Medida medida;
    
    @JoinColumn(name = "id_foto")
    @OneToOne(cascade=CascadeType.ALL)
    private Foto foto;
    
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    
    @Transient
    private Image image;
    
    @Transient
    private ProdutoButton button;
    
    public Produto() {
    }
    
    public Produto(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Produto(Integer id, String descricao, BigDecimal valorCusto, BigDecimal valorVenda, Integer quantidadeEstoque, Integer estoqueMinimo, Medida medida) {
        this.id = id;
        this.descricao = descricao;
        this.valorCusto = valorCusto;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.medida = medida;
    }    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.descricao);
        hash = 79 * hash + Objects.hashCode(this.valorCusto);
        hash = 79 * hash + Objects.hashCode(this.valorVenda);
        hash = 79 * hash + Objects.hashCode(this.quantidadeEstoque);
        hash = 79 * hash + Objects.hashCode(this.estoqueMinimo);
        hash = 79 * hash + Objects.hashCode(this.medida);
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
        final Produto other = (Produto) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.valorCusto, other.valorCusto)) {
            return false;
        }
        if (!Objects.equals(this.valorVenda, other.valorVenda)) {
            return false;
        }
        if (!Objects.equals(this.quantidadeEstoque, other.quantidadeEstoque)) {
            return false;
        }
        if (!Objects.equals(this.estoqueMinimo, other.estoqueMinimo)) {
            return false;
        }
        if (this.medida != other.medida) {
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
    
    public boolean needsWeightReading(){
    	return Medida.KG.equals(this.medida);
    }

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public ProdutoButton getButton() {
		return button;
	}

	public void setButton(ProdutoButton button) {
		this.button = button;
	}
}
