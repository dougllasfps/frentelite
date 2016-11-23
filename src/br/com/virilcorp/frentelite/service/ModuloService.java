package br.com.virilcorp.frentelite.service;

import java.util.List;

import br.com.virilcorp.frentelite.model.Modulo;
import br.com.virilcorp.frentelite.model.ModuloUsuario;
import br.com.virilcorp.frentelite.model.TipoModulo;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;

public class ModuloService extends GenericService<Modulo> {

	private List<Modulo> all;

	public List<Modulo> findAll() {
		if (this.all == null)
			this.all = this.getDao().findAll();
		return this.all;
	}
	
	public List<ModuloUsuario> findByUsuario(final Usuario usuario){
		String hql = "from ModuloUsuario where usuario =:usuario ";
		QueryBuilder builder = new QueryBuilder(hql);
		builder.addParam("usuario", usuario);
		return getDao().find(builder.getQuery(), builder.getParams());
	}
	
	public Modulo findByNome(final TipoModulo mod){
		List<Modulo> modulos = this.findAll();
		
		for (Modulo modulo : modulos) {
			if(modulo.getNome().equalsIgnoreCase(mod.getDescricao())){
				return modulo;
			}
		}
		return null;
	}
}
