package br.com.virilcorp.frentelite.service;

import br.com.virilcorp.frentelite.model.FluxoCaixa;
import br.com.virilcorp.frentelite.model.TipoFluxoCaixa;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;

public class FluxoCaixaService extends GenericService<FluxoCaixa>{

	public boolean possuiAberturaSemFechamento(){
		QueryBuilder builder = new QueryBuilder();
		builder.append(" select case when ( count(id) > 0 ) then true else false end ");
		builder.append(" from FluxoCaixa where tipoFluxo =:tipo and fechamento is null ");
		builder.addParam("tipo", TipoFluxoCaixa.ABERTURA);
		
		return getDao().findOne(builder.getQuery(), builder.getParams(), 1);
	}
	
	public FluxoCaixa findUltimaAberturaSemFechamento(){
		QueryBuilder builder = new QueryBuilder();
		builder.append(" from FluxoCaixa ");
		builder.append(" where tipoFluxo =:tipo and fechamento is null  ");
		builder.addParam("tipo", TipoFluxoCaixa.ABERTURA);
		return getDao().findOne(builder.getQuery(), builder.getParams(), 1);
	}
	
}