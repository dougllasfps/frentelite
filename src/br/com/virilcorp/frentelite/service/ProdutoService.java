/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.model.Medida;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;
import br.com.virilcorp.frentelite.ui.produto.ProdutoModel;
import br.com.virilcorp.frentelite.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author DOUGLLASFPS
 */
public class ProdutoService extends GenericService<Produto>{

    private final ItemVendaService itemVendaService;
    
	public ProdutoService() {
        this.itemVendaService = new ItemVendaService();
    }
	
	public List<Produto> findProdutosMaisVendidos(Integer count){
		QueryBuilder query = new QueryBuilder( "select p, count(p.id) from ItemVenda iv " );
		query.append(" right join iv.produto as p ");
		query.append(" group by p ");
		query.append(" order by count(p.id) ");
		
		List<Object[]> result = getDao().find(query.getQuery(), query.getParams(), count);
		List<Produto> list = new ArrayList<>();
		result.forEach( obj -> list.add((Produto) obj[0]) );
		return list;
	}
	
	public List<Produto> findByDescricaoLike(String descricao){
		QueryBuilder query = new QueryBuilder();
		query.append(" select p from Produto p ");
		query.append(" where upper(p.descricao) like upper(:descricao) ");
		query.addParam("descricao", "%" +descricao+ "%");
		return getDao().find(query.getQuery(), query.getParams());
	}
    
    public void remove(Produto produto){
        if(itemVendaService.existeItem(produto)){
            throw new RuntimeException("Não foi possível deletar, já existe(m) venda(s) registrada(s) para este produto.");
        }
        
        getDao().delete(produto);
    }
    
    public ObservableList<ProdutoModel> findAllObservableList(){
        ObservableList<ProdutoModel> list = FXCollections.observableArrayList();
        
        getDao().findAll().forEach( p -> {
            list.add(new ProdutoModel(p));
        });
        
        return list;
    }
    
    public Produto findByCodigo(String cod){
        QueryBuilder builder = new QueryBuilder("select * from ecf.produto as p where p.codigo = '" + cod + "'" );
        List<Produto> result = getDao().findBySQLQuery(builder.getQuery(), builder.getParams());
        return (result == null || result.isEmpty()) ? null : result.get(0);
    }
    
    public List<Produto> findByCategoria(Categoria cat){
    	Map<String, Object> params = new HashMap<>();
    	params.put("categoria", cat);
    	return getDao().findByParams(params, false);
    }
    
    public ObservableList<ProdutoModel> find( String codigo, String descricao ){
        ObservableList<ProdutoModel> list = FXCollections.observableArrayList();
        
        List<Produto> resultList = null;
        
        if( StringUtils.isNullOrEmpty(codigo) &&  StringUtils.isNullOrEmpty(descricao)){
            resultList = getDao().findAll();
        }else{
            resultList = getDao().findByExample(new Produto(codigo, descricao));
        }
        
        resultList.forEach( p -> {  list.add(new ProdutoModel(p));  });
        return list;
    }

    public void validate(Produto produto){
        StringBuilder errorMessage = new StringBuilder();
        boolean error = false;
        
        if(produto.getCodigo() != null){
        	Produto produtoDoBanco = findByCodigo(produto.getCodigo() );
        	
        	if(produtoDoBanco != null){
        		
        		if( produto.getId() == null || !produto.getId().equals(produtoDoBanco.getId())){
        			error = true;
        			errorMessage.append("O Código já cadastrado anteriormente.\n");
        		}
        		
        	}
        }
        
        if(produto.getDescricao() == null|| produto.getDescricao().trim().isEmpty()){
            error = true;
            errorMessage.append("A Descrição do produto é obrigatória.\n");
        }
        
        if(produto.getMedida() == null || produto.getMedida().equals(Medida.NONE)){
            error = true;
            errorMessage.append("A medida do produto é obrigatória.\n");
        }
        
        if(produto.getValorCusto()== null){
            error = true;
            errorMessage.append("O Valor de Custo do produto é obrigatório.\n");
        }
        
        if(produto.getValorVenda()== null){
            error = true;
            errorMessage.append("O Valor de Venda do produto é obrigatório.\n");
        }
        
        if(produto.getCategoria()== null){
            error = true;
            errorMessage.append("A Categoria do produto é obrigatória.\n");
        }
        
        if(error){
            throw new ValidationException(errorMessage.toString());
        }
    }
    
    public void insert(Produto produto) {
    	validate(produto);
    	getDao().insert(produto);
    	addCodigo(produto);
    	getDao().update(produto);
    }

	private void addCodigo(Produto produto) {
		if(produto.getCodigo() == null || "".equals(produto.getCodigo().trim())){
    		produto.setCodigo(produto.getId().toString());
    	}
	}
    
    public void update(Produto produto) {
    	validate(produto);
    	getDao().update(produto);
    }
}