package br.com.virilcorp.frentelite.service;

import java.util.Calendar;
import java.util.List;

import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.frentelite.model.Cliente;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;
import br.com.virilcorp.frentelite.ui.delivery.DeliveryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeliveryService extends GenericService<Delivery>{

	private VendaService vendaService;
	private ClienteService clienteService;
	
	public DeliveryService() {
		super();
		vendaService = new VendaService();
		clienteService = new ClienteService();
	}
	
	public VendaService getVendaService() {
		return vendaService;
	}
	
	public Cliente findByTelefone(final String tel){
		String hql = String.format("From Cliente where telefone = '%s' ", tel );
		return getDao().findOne( hql, null, 1 );
	}
	
	public ObservableList<DeliveryModel> findPendentes(Calendar data){
		ObservableList<DeliveryModel> list = FXCollections.observableArrayList();
		
		if(data == null){
			data = Calendar.getInstance();
		}
		
		QueryBuilder query = new QueryBuilder();
		query.append("from Delivery where venda.pagamento is null ");
		query.addParam("data", data);
		
		List<Delivery> result = getDao().find(query.getQuery(), query.getParams());
		
		result.forEach( r -> list.add(new DeliveryModel(r)) );
		
		return list;
	}
	
	public ObservableList<DeliveryModel> findByData(Calendar data){
		ObservableList<DeliveryModel> list = FXCollections.observableArrayList();
		
		if(data == null){
			data = Calendar.getInstance();
		}

		QueryBuilder query = new QueryBuilder();
		query.append( String.format("from Delivery where to_char(venda.dataVenda,'dd/MM/yyyy') = '%s' ",DateTimeUtils.format(data)) );
		
		List<Delivery> result = getDao().find(query.getQuery(), null);
		
		result.forEach( r -> list.add(new DeliveryModel(r)) );
		
		return list;
	}
	
	public void save(Delivery delivery){
		vendaService.finalizarVenda(delivery.getVenda());
		Cliente cliente = delivery.getCliente();
		
		if(cliente.getId() == null){
			clienteService.save(cliente);
		}else{
			clienteService.update(cliente);
		}
		
		getDao().insert(delivery);
	}
}
