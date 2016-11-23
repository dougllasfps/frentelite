package br.com.virilcorp.daruma.relatorio.gerenciamento.fechamento;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

import br.com.virilcorp.daruma.relatorio.CorpoRelatorio;
import br.com.virilcorp.frentelite.model.Sangria;
import br.com.virilcorp.frentelite.model.Venda;

public class CorpoRelatorioFechamento implements CorpoRelatorio{

	private Integer quantidadeVendas;
	
	private BigDecimal totalDinheiro;
	private BigDecimal totalDebito;
	private BigDecimal totalCredito;
	
	private BigDecimal totalSangrias;
	private BigDecimal fundoCaixa;
	private BigDecimal totalDinheiroEmCaixa;
	
	private Calendar data;
	
	private Collection<Sangria> sangrias;
	private Collection<Venda> vendasCanceladas;
	
	public Integer getQuantidadeVendas() {
		return quantidadeVendas;
	}
	public void setQuantidadeVendas(Integer quantidadeVendas) {
		this.quantidadeVendas = quantidadeVendas;
	}
	public BigDecimal getTotalDinheiro() {
		return totalDinheiro;
	}
	public void setTotalDinheiro(BigDecimal totalDinheiro) {
		this.totalDinheiro = totalDinheiro;
	}
	public BigDecimal getTotalDebito() {
		return totalDebito;
	}
	public void setTotalDebito(BigDecimal totalDebito) {
		this.totalDebito = totalDebito;
	}
	public BigDecimal getTotalCredito() {
		return totalCredito;
	}
	public void setTotalCredito(BigDecimal totalCredito) {
		this.totalCredito = totalCredito;
	}
	public BigDecimal getFundoCaixa() {
		return fundoCaixa;
	}
	public void setFundoCaixa(BigDecimal fundoCaixa) {
		this.fundoCaixa = fundoCaixa;
	}
	public BigDecimal getTotalSangrias() {
		return totalSangrias;
	}
	public void setTotalSangrias(BigDecimal totalSangrias) {
		this.totalSangrias = totalSangrias;
	}
	public BigDecimal getTotalDinheiroEmCaixa() {
		return totalDinheiroEmCaixa;
	}
	public void setTotalDinheiroEmCaixa(BigDecimal totalDinheiroEmCaixa) {
		this.totalDinheiroEmCaixa = totalDinheiroEmCaixa;
	}
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	public Collection<Sangria> getSangrias() {
		return sangrias;
	}
	public void setSangrias(Collection<Sangria> sangrias) {
		this.sangrias = sangrias;
	}
	public Collection<Venda> getVendasCanceladas() {
		return vendasCanceladas;
	}
	public void setVendasCanceladas(Collection<Venda> vendasCanceladas) {
		this.vendasCanceladas = vendasCanceladas;
	}
	
	
}