/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.com.virilcorp.frentelite.dao.GenericDao;
import br.com.virilcorp.frentelite.dao.callback.StatementManager;
import br.com.virilcorp.frentelite.dao.callback.TransactionCallBack;
import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.Cliente;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Pagamento;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;
import br.com.virilcorp.frentelite.ui.venda.VendaModel;
import br.com.virilcorp.frentelite.util.MonetaryUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author DOUGLLASFPS
 */
public class VendaService extends GenericService<Venda> {
    
    private final GenericDao<ItemVenda> itemVendaDao;
    private final GenericDao<Venda> vendaDao;
    private final GenericDao<Pagamento> pagamentoDao;
    private final StatementManager transactionalStatementExecutor;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public VendaService( ) {
    	super();
    	this.transactionalStatementExecutor =  getDao().getStatementExecutor();
    	this.itemVendaDao = new GenericDao(  ItemVenda.class, transactionalStatementExecutor);
    	this.vendaDao     = new GenericDao(  Venda.class,  transactionalStatementExecutor);
    	this.pagamentoDao = new GenericDao(  Pagamento.class,  transactionalStatementExecutor);
    }   
    
    public List<Venda> findAll(){
    	return vendaDao.findAll();
    }
    
    public GenericDao<ItemVenda> getItemVendaDao() {
		return itemVendaDao;
	}

	public GenericDao<Venda> getVendaDao() {
		return vendaDao;
	}

	public GenericDao<Pagamento> getPagamentoDao() {
		return pagamentoDao;
	}

	public void validarEConfigurarPagamento(Venda venda){
		Pagamento pagamento = venda.getPagamento();
		
		//pega os valores
        BigDecimal totalVenda = venda.getCarrinho().getTotal();
        BigDecimal valorCartao =    MonetaryUtils.valueOrZero(pagamento.getValorCartao());
        BigDecimal valorDebito =    MonetaryUtils.valueOrZero(pagamento.getValorDebito());
        BigDecimal valorDinheiro =  MonetaryUtils.valueOrZero(pagamento.getValorDinheiro());
        BigDecimal valorDesconto =  MonetaryUtils.valueOrZero(pagamento.getValorDesconto());
        
        //verifica o total a ser pago
        BigDecimal subTotal = totalVenda.subtract(valorDesconto);
        
        //total em pagamentos do cliente
        BigDecimal somaPagamentos = valorDinheiro.add(valorCartao).add(valorDebito);
        
        // verifica se há pagamentos suficientes para a venda
        if(subTotal.compareTo(somaPagamentos)  == 1 ){
            throw new ValidationException("Pagamento Insuficiente para o Total da Venda.");
        }
        
        //valor em cartao nao pode ser maior do que o subtotal, pois não há como dar troco para cartão.
        if(subTotal.compareTo(valorCartao) == -1){
        	throw new ValidationException("O valor em Cartão ultrapassa o Total da Venda.");
        }

        //para fechar o valor correto em dinheiro, pois o usuario informará de fato
        //o valor entregue pelo cliente, para que o sistema informe o troco e no banco vá o valor recebido - o troco.
        BigDecimal valorEmDinheiroReal = subTotal.subtract(valorCartao);
        pagamento.setValorDinheiro(valorEmDinheiroReal);
    }
	
	public List<Venda> findVendasCanceladas(Calendar horaAbertura, Calendar horaFechamento){
		QueryBuilder query = new QueryBuilder()
				.append(" select v from Venda v ")
				.append(" where cancelada = true ")
				.append(" and v.dataVenda between :dataI and :dataF ")
				.addParam("dataI", horaAbertura)
				.addParam("dataF", horaFechamento);
		
		return getDao().find(query);
	}
    
    public Venda finalizarVenda( final Venda venda ){
    	
        TransactionCallBack procedure = (EntityManager entityManager) -> {
        	
        	Calendar data = Calendar.getInstance();
			venda.setDataVenda(data);
        	venda.setData(data);
        	
        	Pagamento pagamento = venda.getPagamento();
        	venda.setPagamento(null);

        	if(venda.getId() == null )
        		entityManager.persist(venda);
        	else
        		entityManager.merge(venda);
        	
        	entityManager.flush();
        	
			if(pagamento != null){
            	if(pagamento.getId() == null){
            		entityManager.persist(pagamento);
            	}else{
            		entityManager.merge(pagamento);
            	}
            	
            	venda.setPagamento(pagamento);
            }
            
            entityManager.flush();
            
            Collection<ItemVenda> itens = venda.getCarrinho().getItens();
            
            itens.forEach( i -> {
            	if(i.getId() == null)
            		entityManager.persist(i);
            	else
            		entityManager.merge(i);
            });
            
            return 1;
        };
        
        if(transactionalStatementExecutor.executeCallBack(procedure) == 0){
        	venda.setId(null);
        	venda.getPagamento().setId(null);
        	throw new RuntimeException();
        }
        
        return venda;
    }
    
    public Venda finalizarVendaComDelivery( final Delivery delivery ){
    	Venda venda = delivery.getVenda();
    	
        TransactionCallBack procedure = (EntityManager entityManager) -> {
        	
        	Cliente cliente = delivery.getCliente();
    		
    		if(cliente.getId() == null){
    			entityManager.persist(cliente);
    		}else{
    			entityManager.merge(cliente);
    		}
        	
        	Calendar data = Calendar.getInstance();
			venda.setDataVenda(data);
        	venda.setData(data);
        	
        	if(venda.getId() == null )
        		entityManager.persist(venda);
        	else
        		entityManager.merge(venda);
        	
        	entityManager.flush();
        	
            Collection<ItemVenda> itens = venda.getCarrinho().getItens();
            
            itens.forEach( i -> {
            	if(i.getId() == null)
            		entityManager.persist(i);
            	else
            		entityManager.merge(i);
            });
            
            entityManager.persist(delivery);
            
            return 1;
        };
        if(transactionalStatementExecutor.executeCallBack(procedure) == 0){
        	venda.setId(null);
        	throw new RuntimeException();
        }
        return venda;
    }
    
    public Collection<Venda> findVendasByDate(final Calendar data){
    	String hql = " from Venda v where v.data = :data ";
    	Map<String, Object> params = new HashMap<>();
    	params.put("data", data);
    	return getDao().find(hql, params);
    }
    
    public ObservableList<VendaModel> toDataModelList(Collection<Venda> vendas){
    	if(vendas == null){
    		return FXCollections.observableArrayList();
    	}
    	ObservableList<VendaModel> list = FXCollections.observableArrayList();
    	vendas.forEach( obj -> list.add(new VendaModel(obj)) );
    	return list;
    }
    
    public List<ItemVenda> findItensVenda(Venda venda){
    	Map<String, Object> params = new HashMap<>();
    	params.put("venda", venda);
    	return this.itemVendaDao.findByParams(params, false);
    }
    
    public Long countVendasValidas(Calendar dataInicial, Calendar dataFinal){
    	QueryBuilder query = new QueryBuilder();
    	query.append(" select count(v.id) from Venda v ");
    	query.append(" where v.cancelada = false ");
    	
    	getDao()
    		.ativarFiltroEntidade("intervaloFluxo")
    		.setParameter("dataInicial", dataInicial)
    		.setParameter("dataFinal", dataFinal);
    	
    	return getDao().findOne(query.getQuery(), query.getParams(), null);
    }
    
    public Venda findUltimaVenda(Calendar dataInicial, Calendar dataFinal){
    	QueryBuilder query = new QueryBuilder();
    	query.append(" select v from Venda v ");
    	query.append(" where v.cancelada = false ");
    	query.append(" order by v.dataVenda desc ");
    	
    	getDao()
    		.ativarFiltroEntidade("intervaloFluxo")
    		.setParameter("dataInicial", dataInicial)
    		.setParameter("dataFinal", dataFinal);
    	
    	return getDao().findOne(query.getQuery(), query.getParams(), 1);
    }
    
    public static void main(String[] args) {
    	Calendar c =  new GregorianCalendar(2016, 7, 17, 0, 0, 0);
    	Calendar c2 = new GregorianCalendar(2016, 7, 17, 16, 17, 2);
    	
		VendaService s = new VendaService();
		Long countVendasValidas = s.countVendasValidas(c,c2);
		System.out.println(countVendasValidas);
		
	}
}