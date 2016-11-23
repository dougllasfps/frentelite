package br.com.virilcorp.frentelite.service;

import java.util.Calendar;
import java.util.Collection;

import br.com.virilcorp.frentelite.model.Sangria;

public class SangriaService extends GenericService<Sangria> {

	public Collection<Sangria> findByData(Calendar dataAbertura, Calendar dataFechamento){
		getDao()
			.ativarFiltroEntidade("intervaloFluxo")
			.setParameter("dataInicial", dataAbertura)
			.setParameter("dataFinal", dataFechamento);
		return getDao().findAll();
	}
}