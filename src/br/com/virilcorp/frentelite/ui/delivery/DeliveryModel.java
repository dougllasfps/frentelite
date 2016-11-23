package br.com.virilcorp.frentelite.ui.delivery;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.Pagamento;
import br.com.virilcorp.frentelite.model.SituacaoDelivery;
import br.com.virilcorp.frentelite.ui.DataModel;
import br.com.virilcorp.frentelite.util.TelefoneHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeliveryModel implements DataModel<Delivery>{

	private Delivery data;
	private StringProperty dataVenda;
	private StringProperty cliente;
	private StringProperty telefone;
	private StringProperty valor;
	private StringProperty situacao;
	
	public DeliveryModel(Delivery data) {
		this.data = data;
		
		setCliente(data.getCliente().getNome());
		setTelefone( TelefoneHandler.withMask( data.getCliente().getTelefone()) );
		setValor(data.getVenda().getValorTotal());
		setDataVenda(data.getVenda().getDataVenda());
		
		if(data.isCancelado()){
			setSituacao( SituacaoDelivery.CANCELADO.name() );
		}else{
			Pagamento pagamento = data.getVenda().getPagamento();
			boolean possuiPagamento = pagamento != null && pagamento.getId() != null;
			setSituacao( possuiPagamento ? SituacaoDelivery.PAGO.name() : SituacaoDelivery.PENDENTE.name() );
		}
	}
	
	public void setCliente(String cliente) {
		this.cliente = new SimpleStringProperty(cliente == null ? "" : cliente);
	}
	
	public StringProperty getCliente() {
		return cliente;
	}
	
	public Delivery getData() {
		return data;
	}

	public StringProperty getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = new SimpleStringProperty(valor == null ? "" : NumericConverter.formatCurrent(valor));
	}

	public StringProperty getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = new SimpleStringProperty(situacao == null ? "" : situacao);
	}

	public StringProperty getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = new SimpleStringProperty(telefone == null ? "" : telefone);
	}

	public StringProperty getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Calendar dataVenda) {
		this.dataVenda = new SimpleStringProperty(dataVenda == null ? "" : DateTimeUtils.format(dataVenda));
	}
}