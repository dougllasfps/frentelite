/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Medida;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author DOUGLLASFPS
 */
public class ItemVendaModel {
    
    private ItemVenda itemVenda;
    private StringProperty qtd;
    private StringProperty descProduto;
    private StringProperty unidadeMedida;
    private StringProperty valoUnitario;
    private StringProperty valorTotal;

    public ItemVendaModel() {
    }
    
    public ItemVendaModel(ItemVenda item){
        this.itemVenda = item;
        
        if(itemVenda.getQuantidade() == null){
            itemVenda.setQuantidade(BigDecimal.ONE);
        }
        
        if(itemVenda.getProduto().getDescricao() == null){
            itemVenda.getProduto().setDescricao("");
        }
        
        if(itemVenda.getProduto().getMedida() == null){
            itemVenda.getProduto().setMedida(Medida.UND);
        }
        
        if(itemVenda.getProduto().getValorVenda() == null){
            item.getProduto().setValorVenda(BigDecimal.ZERO);
        }
                
        this.qtd = new SimpleStringProperty(item.getQuantidade().toString());
        this.descProduto = new SimpleStringProperty(item.getProduto().getDescricao());
        this.unidadeMedida = new SimpleStringProperty(item.getProduto().getMedida().toString());
        this.valoUnitario = new SimpleStringProperty( NumericConverter.formatCurrent(item.getProduto().getValorVenda()));
        
        BigDecimal valorTotalProduto = item.getProduto().getValorVenda().multiply(item.getQuantidade());
        
        this.valorTotal = new SimpleStringProperty( NumericConverter.formatCurrent(valorTotalProduto));
    }

    public ItemVenda getItemVenda() {
        return itemVenda;
    }

    public void setItemVenda(ItemVenda itemVenda) {
        this.itemVenda = itemVenda;
    }

    public StringProperty getQtd() {
        return qtd;
    }

    public void setQtd(StringProperty qtd) {
        this.qtd = qtd;
    }

    public StringProperty getDescProduto() {
        return descProduto;
    }

    public void setDescProduto(StringProperty descProduto) {
        this.descProduto = descProduto;
    }

    public StringProperty getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(StringProperty unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public StringProperty getValoUnitario() {
        return valoUnitario;
    }

    public void setValoUnitario(StringProperty valoUnitario) {
        this.valoUnitario = valoUnitario;
    }

    public StringProperty getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(StringProperty valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public StringProperty toStringProperty() {
    	return new SimpleStringProperty(itemVenda.toString());
    }
    
}
