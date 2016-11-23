package br.com.virilcorp.frentelite.ui.venda;

import br.com.virilcorp.converter.CalendarConverter;
import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.ui.DataModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VendaModel implements DataModel<Venda> {
	
	private Venda data;
	private StringProperty numeroVenda;
	private StringProperty dataVenda;
	private StringProperty valorTotal;
	
	public VendaModel(Venda data) {
		super();
		
		this.data = data;
		
		if(this.data.getId() != null){
			this.numeroVenda = new SimpleStringProperty(data.getId().toString());
		}
		
		if(this.data.getDataVenda() != null){
			this.dataVenda = new SimpleStringProperty(new CalendarConverter(CalendarConverter.DATE_TIME_PATTERN).toString(this.data.getDataVenda()));
		}
		
		if(this.data.getValorTotal() != null){
			this.valorTotal = new SimpleStringProperty(NumericConverter.formatCurrent(this.data.getValorTotal()));
		}
		
		if(this.data.getValorDesconto() != null){
			this.valorTotal = new SimpleStringProperty(NumericConverter.formatCurrent(this.data.getValorDesconto()));
		}
	}
	public Venda getData() {
		return data;
	}
	public void setData(Venda data) {
		this.data = data;
	}
	public StringProperty getNumeroVenda() {
		return numeroVenda;
	}
	public void setNumeroVenda(StringProperty numeroVenda) {
		this.numeroVenda = numeroVenda;
	}
	public StringProperty getDataVenda() {
		return dataVenda;
	}
	public void setDataVenda(StringProperty dataVenda) {
		this.dataVenda = dataVenda;
	}
	public StringProperty getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(StringProperty valorTotal) {
		this.valorTotal = valorTotal;
	}
}