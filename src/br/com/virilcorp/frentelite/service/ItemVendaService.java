/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;

/**
 *
 * @author dougllas.sousa
 */
public class ItemVendaService extends GenericService<ItemVenda> {
    
    public boolean existeItem(Produto produto){
        Map<String,Object> params = new HashMap<>();
        params.put("produto", produto);
        List<ItemVenda> itens = getDao().findByParams(params, false);
        return itens != null && !itens.isEmpty();
    }
    
    public List<ItemVenda> findByVenda(Venda venda){
    	 Map<String,Object> params = new HashMap<>();
         params.put("venda", venda);
         return getDao().findByParams(params, false);
    }
    
    public List<ItemVenda> findAll(){
    	return getDao().findAll();
    }
    
	public List<ItemVenda> findByDate(Calendar date){
    	QueryBuilder builder = new QueryBuilder();
    	
    	builder.append(" select * from ecf.item_venda iv ");
    	builder.append(" inner join ecf.venda as v on iv.id_venda = v.id ");
    	builder.append(" where 1 = 1 ");
    	builder.append(" and to_char(v.data_venda, 'dd/MM/yyyy' ) = '" + new SimpleDateFormat("dd/MM/yyyy").format(date.getTime()) + "'");
    	
    	return getDao().findBySQLQuery(builder.getQuery(), builder.getParams());
    }
    
}
