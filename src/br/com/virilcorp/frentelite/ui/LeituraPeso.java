package br.com.virilcorp.frentelite.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.virilcorp.frentelite.model.Tara;

public class LeituraPeso implements LeitorQuantidade {

	private List<Tara> taras;
	private BigDecimal leitura;
	
	public LeituraPeso() {
		setLeitura(BigDecimal.ZERO);
	}
	
	public void clear(){
		setLeitura(BigDecimal.ZERO);
		setTaras(null);
	}
	
	public BigDecimal getLeitura() {
		return leitura;
	}

	public void setLeitura(BigDecimal leitura) {
		this.leitura = leitura;
	}

	@Override
	public BigDecimal calcularQuantidade() {
		BigDecimal totalTaras = BigDecimal.ZERO;
		
		for (Tara tara : taras) {
			totalTaras = totalTaras.add(tara.getPeso());
		}
		
		return getLeitura().subtract(totalTaras);
	}

	public List<Tara> getTaras() {
		if(this.taras == null)
			taras = new ArrayList<>();
		return taras;
	}

	public void setTaras(List<Tara> taras) {
		this.taras = taras;
	}

	public void addTara(Tara tara){
		getTaras().add(tara);
	}
}