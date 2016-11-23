/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.produto;

import java.math.BigDecimal;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Medida;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.ui.DataModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author DOUGLLASFPS
 */
public class ProdutoModel implements DataModel<Produto>{
        
        private Produto produto;
        private StringProperty codigo;
        private StringProperty descricao;
        private StringProperty valorCusto;
        private StringProperty valorVenda;
        private StringProperty categoria;
        private StringProperty unidadeMedida;

        public ProdutoModel(Produto produto) {
            
            if(produto.getCodigo() == null){
                produto.setCodigo("");
            }
            
            if(produto.getDescricao() == null){
                produto.setDescricao("");
            }
            
            if(produto.getValorCusto() == null){
                produto.setValorCusto(BigDecimal.ZERO);
            }
            
            if(produto.getValorVenda() == null){
                produto.setValorVenda(BigDecimal.ZERO);
            }
            
            if(produto.getEstoqueMinimo() == null){
                produto.setEstoqueMinimo(0);
            }
            
            this.produto = produto;
            this.codigo = new SimpleStringProperty(produto.getCodigo());
            this.descricao = new SimpleStringProperty(produto.getDescricao());
            this.valorCusto = new SimpleStringProperty( NumericConverter.formatCurrent(produto.getValorCusto()));
            this.valorVenda = new SimpleStringProperty( NumericConverter.formatCurrent(produto.getValorVenda()));
            this.categoria = new SimpleStringProperty(produto.getCategoria().getDescricao());
            this.setUnidadeMedida(new SimpleStringProperty( produto.getMedida() == null || produto.getMedida().equals(Medida.NONE)? "" : produto.getMedida().toString() ));
        }

        public StringProperty getCodigo() {
            return codigo;
        }

        public void setCodigo(StringProperty codigo) {
            this.codigo = codigo;
        }

        public StringProperty getDescricao() {
            return descricao;
        }

        public void setDescricao(StringProperty descricao) {
            this.descricao = descricao;
        }

        public StringProperty getValorCusto() {
            return valorCusto;
        }

        public void setValorCusto(StringProperty valorCusto) {
            this.valorCusto = valorCusto;
        }

        public StringProperty getValorVenda() {
            return valorVenda;
        }

        public void setValorVenda(StringProperty valorVenda) {
            this.valorVenda = valorVenda;
        }

        public StringProperty getCategoria() {
            return categoria;
        }

        public void setCategoria(StringProperty categoria) {
            this.categoria = categoria;
        }    

        public Produto getProduto() {
            return produto;
        }

		public StringProperty getUnidadeMedida() {
			return unidadeMedida;
		}

		public void setUnidadeMedida(StringProperty unidadeMedida) {
			this.unidadeMedida = unidadeMedida;
		}

		@Override
		public Produto getData() {
			return this.produto;
		}
        
    }
