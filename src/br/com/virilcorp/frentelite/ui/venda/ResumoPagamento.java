package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import br.com.virilcorp.frentelite.util.MonetaryUtils;

public class ResumoPagamento {

	private BigDecimal subTotal, desconto, valorPagar, totalPago;
	private Map<TipoPagamento, BigDecimal> pagamentos;
	
	public ResumoPagamento(BigDecimal subTotal) {
		this.pagamentos = new HashMap<>();
		this.subTotal = subTotal;
		this.valorPagar = subTotal;
		this.desconto = BigDecimal.ZERO;
		this.totalPago = BigDecimal.ZERO;
	}
	
	public void addValorPagamento(TipoPagamento tipoPagamento, BigDecimal valor){
		this.totalPago = MonetaryUtils.valueOrZero(totalPago);

		this.totalPago = totalPago.add(valor);
		boolean containsKey = pagamentos.containsKey(tipoPagamento);
		
		if(containsKey){
			BigDecimal bigDecimal = pagamentos.get(tipoPagamento);
			bigDecimal = bigDecimal.add(valor);
			pagamentos.put(tipoPagamento, bigDecimal);
		}else{
			pagamentos.put(tipoPagamento, valor);
		}
		
		calcularValorPagar();
	}

	private void calcularValorPagar() {
		BigDecimal diference = getSubTotal().subtract(getTotalPago());
		
		if(diference.compareTo(BigDecimal.ZERO) < 0){
			setValorPagar(BigDecimal.ZERO);
		}else{
			setValorPagar(diference);
		}
	}
	
	public void removerValorPagamento(TipoPagamento tipo, BigDecimal valor){
		BigDecimal valorPagoTipo = MonetaryUtils.valueOrZero(pagamentos.get(tipo));
		BigDecimal totalPagoTipo = valorPagoTipo.subtract(valor);
		pagamentos.put(tipo, totalPagoTipo);
		this.totalPago = MonetaryUtils.valueOrZero(totalPago);
		this.totalPago = totalPago.subtract(valor);
		calcularValorPagar();
	}
	
	public BigDecimal calcularTroco(){
		BigDecimal total = getSubTotal();
		BigDecimal valorDescontos = getDesconto();
		
		BigDecimal totalFinal = total.subtract( valorDescontos );
		BigDecimal totalPago = getTotalPago();

		boolean isMaior = totalPago.compareTo(totalFinal) > 0;
		
		return isMaior ? totalPago.subtract(totalFinal) : BigDecimal.ZERO;
	}

	public BigDecimal getSubTotal() {
		return MonetaryUtils.valueOrZero(subTotal);
	}

	public BigDecimal getDesconto() {
		return MonetaryUtils.valueOrZero(desconto);
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorPagar() {
		return MonetaryUtils.valueOrZero(valorPagar);
	}

	private void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public BigDecimal getTotalPago() {
		return MonetaryUtils.valueOrZero(totalPago);
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}
	
	public BigDecimal getTotalPagoPorTipo(TipoPagamento tipo){
		return MonetaryUtils.valueOrZero(pagamentos.get(tipo));
	}
}