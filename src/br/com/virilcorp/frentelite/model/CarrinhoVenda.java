/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.virilcorp.frentelite.ui.venda.ItemVendaModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author DOUGLLASFPS
 */
public class CarrinhoVenda {
    
    private List<ItemVenda> itensVenda;
    private ItemVenda ultimoProdutoAdicionado;
    private Venda venda;
    
    public ItemVenda ultimoProdutoAdicionado(){
        return ultimoProdutoAdicionado;
    }

    public CarrinhoVenda() {
        init();
    }
    
    public void limparCarrinho(){
        init();
    }
    
    private void init(){
    	if(this.itensVenda != null){
    		this.itensVenda.clear();
    	}else{
    		this.itensVenda = new ArrayList<>();
    	}
    }
    
    public void atualizaQuantidade( ItemVenda item , BigDecimal novaQuantidade ){
        item.setQuantidade(novaQuantidade);
        BigDecimal valorVenda = calcularValorVenda(item.getProduto(), novaQuantidade);
        item.setValorVenda(valorVenda);
    }
    
    public boolean remove(ItemVenda item){
    	if(venda.getItensVenda() != null)
    		venda.getItensVenda().remove(item);
    	
        return itensVenda.remove(item);
    }
    
    public ItemVenda add( Produto produto, BigDecimal quantidade ){
    	if(quantidade == null || quantidade.compareTo(BigDecimal.ZERO) == 0){
    		quantidade = BigDecimal.ONE;
    	}
    	
        BigDecimal valorVenda = calcularValorVenda(produto, quantidade);
		
        ItemVenda item = new ItemVenda().
				new ItemVendaBuilder()
					.produto(produto)
					.venda(venda)
					.quantidade(quantidade)
					.valorVenda(valorVenda)
					.valorUnitario(produto.getValorVenda())
					.build();
		
        this.add(item);
        this.venda.setValorTotal(getTotal());
        return item;
    }
    
    public void add(ItemVenda item){
        itensVenda.add(item);
        
        if(venda.getItensVenda() == null){
        	venda.setItensVenda(new ArrayList<ItemVenda>());
        }
        
        venda.getItensVenda().add(item);
        
        this.ultimoProdutoAdicionado = item;
    }
    
    public void addAll(Collection<ItemVenda> list){
    	for (ItemVenda itemVenda : list) {
    		itensVenda.add(itemVenda);
    		this.ultimoProdutoAdicionado = itemVenda;
		}
    }
    
    private BigDecimal calcularValorVenda(Produto p, BigDecimal qtd){
        return qtd.multiply(p.getValorVenda());
    }
    
    public Collection<ItemVenda> getItens(){
        return  itensVenda;
    }
    
    public boolean isEmpty(){
        return itensVenda == null || itensVenda.isEmpty();
    }
    
    public ObservableList<ItemVendaModel> getObservableList(){
        if(itensVenda ==  null || itensVenda.isEmpty()){
            return null;
        }
        
        ObservableList<ItemVendaModel> list = FXCollections.observableArrayList( );
        
        itensVenda.forEach( (i) -> {
            list.add(new ItemVendaModel(i));
        } );
        
        return list;
    }
    
    public BigDecimal getTotal(){
    	
        if(isEmpty()){
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = BigDecimal.ZERO;
        
        for ( ItemVenda item : itensVenda){
            total = total.add(item.getValorVenda());
        }
        
        return total;
    }

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
}
