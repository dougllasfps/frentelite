package br.com.virilcorp.frentelite.service;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.virilcorp.frentelite.dao.GenericDao;
import br.com.virilcorp.frentelite.model.FluxoCaixa;
import br.com.virilcorp.frentelite.model.Pagamento;
import br.com.virilcorp.frentelite.model.Sangria;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;
import br.com.virilcorp.frentelite.util.MonetaryUtils;

public class PagamentoService extends GenericService<Pagamento> {
	
	private FluxoCaixaService fluxoCaixaService;
	private GenericDao<Sangria> sangriaDao;
	
	public PagamentoService() {
		super();
		this.fluxoCaixaService = new FluxoCaixaService();
		this.sangriaDao = new GenericDao<Sangria>(Sangria.class, getDao().getStatementExecutor());
	}

	public BigDecimal findValorDinheiroAtualizado(final Calendar dataAbertura, final Calendar dataFechamento){
		
		QueryBuilder query = new QueryBuilder();
		query.append(" select sum(p.valorDinheiro) from Pagamento p ");
		query.append(" inner join p.venda v ");
		query.append(" where v.dataVenda between :dataInicial and :dataFinal ");
		query.append(" and v.cancelada = false ");
		query.addParam("dataInicial", dataAbertura);
		query.addParam("dataFinal", dataFechamento);
		
		BigDecimal totalVendas = getDao().findOne(query.getQuery(), query.getParams(), 1);
		totalVendas = MonetaryUtils.valueOrZero(totalVendas);
		
		query = new QueryBuilder();
		query.append("select sum(s.valor) from Sangria s ");
		getSangriaDao()
			.ativarFiltroEntidade("intervaloFluxo")
			.setParameter("dataInicial", dataAbertura)
			.setParameter("dataFinal", dataFechamento);
		
		BigDecimal totalSangria = getSangriaDao().findOne(query.getQuery(), query.getParams(), 1);
		totalSangria = MonetaryUtils.valueOrZero(totalSangria);
		
		FluxoCaixa aberturaCaixa = fluxoCaixaService.findUltimaAberturaSemFechamento();
		BigDecimal valorAbertura = BigDecimal.ZERO;
		
		if(aberturaCaixa != null){
			valorAbertura = MonetaryUtils.valueOrZero( aberturaCaixa.getFundoCaixa() );
		}
		
		return totalVendas.add(valorAbertura).subtract(totalSangria);
	}
	
	public BigDecimal findValorDinheiroVendasValidas(final Calendar dataAbertura, final Calendar dataFechamento){
		QueryBuilder query = new QueryBuilder();
		
		query.append(" select sum(p.valorDinheiro) from Pagamento p ");
		query.append(" inner join p.venda v ");
		query.append(" where v.dataVenda between :dataInicial and :dataFinal ");
		query.append(" and v.cancelada = false ");
		query.addParam("dataInicial", dataAbertura);
		query.addParam("dataFinal", dataFechamento);
		
		BigDecimal totalVendas = getDao().findOne(query.getQuery(), query.getParams(), 1);
		return MonetaryUtils.valueOrZero(totalVendas);
	}
	
	public BigDecimal findValorCreditoVendasValidas(final Calendar dataAbertura, final Calendar dataFechamento){
		QueryBuilder query = new QueryBuilder();
		
		query.append(" select sum(p.valorCartao) from Pagamento p ");
		query.append(" inner join p.venda v ");
		query.append(" where v.dataVenda between :dataInicial and :dataFinal ");
		query.append(" and v.cancelada = false ");
		query.addParam("dataInicial", dataAbertura);
		query.addParam("dataFinal", dataFechamento);
		
		BigDecimal totalVendas = getDao().findOne(query.getQuery(), query.getParams(), 1);
		return MonetaryUtils.valueOrZero(totalVendas);
	}
	
	public BigDecimal findValorDebitoVendasValidas(final Calendar dataAbertura, final Calendar dataFechamento){
		QueryBuilder query = new QueryBuilder();
		
		query.append(" select sum(p.valorDebito) from Pagamento p ");
		query.append(" inner join p.venda v ");
		query.append(" where v.dataVenda between :dataInicial and :dataFinal ");
		query.append(" and v.cancelada = false ");
		query.addParam("dataInicial", dataAbertura);
		query.addParam("dataFinal", dataFechamento);
		
		BigDecimal totalVendas = getDao().findOne(query.getQuery(), query.getParams(), 1);
		return MonetaryUtils.valueOrZero(totalVendas);
	}

	public GenericDao<Sangria> getSangriaDao() {
		return sangriaDao;
	}

	public void setSangriaDao(GenericDao<Sangria> sangriaDao) {
		this.sangriaDao = sangriaDao;
	}
}