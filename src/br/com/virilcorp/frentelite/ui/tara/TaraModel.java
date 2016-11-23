package br.com.virilcorp.frentelite.ui.tara;

import java.math.BigDecimal;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Tara;
import br.com.virilcorp.frentelite.ui.DataModel;
import br.com.virilcorp.frentelite.ui.util.FXPropertyUtils;
import javafx.beans.property.StringProperty;

public class TaraModel implements DataModel<Tara> {

	private StringProperty descricao;
	private StringProperty peso;
	private Tara tara;
	
	public TaraModel() {
	}
	
	public TaraModel(Tara tara){
		this.tara = tara;
		this.setDescricao(tara.getDescricao());
		this.setPeso(tara.getPeso());
	}
	
	public StringProperty getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = FXPropertyUtils.parse(descricao);
	}
	public StringProperty getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		String pesoString = "0,000";
		
		if(peso != null)
			pesoString = NumericConverter.formatWeight(peso);
		
		this.peso =  FXPropertyUtils.parse(pesoString);;
	}
	
	public Tara getTara(){
		return this.tara;
	}

	@Override
	public Tara getData() {
		return this.tara;
	}
}
