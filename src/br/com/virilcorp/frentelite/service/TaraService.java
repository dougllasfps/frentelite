package br.com.virilcorp.frentelite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.virilcorp.frentelite.exception.RegistroCadastradoException;
import br.com.virilcorp.frentelite.model.Tara;
import br.com.virilcorp.frentelite.ui.tara.TaraModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class TaraService extends GenericService<Tara> {
	public void save(Tara tara) throws RegistroCadastradoException{
		validate(tara);
		getDao().insert(tara);
	}
	
	public void update(Tara tara) throws RegistroCadastradoException{
		validate(tara);
		getDao().update(tara);
	}
	
	public void delete(Tara tara){
		getDao().delete(tara);
	}
	
	public ObservableList<TaraModel> find(final Tara tara){
		ObservableList<TaraModel> list = FXCollections.observableArrayList();
		List<Tara> resultList = getDao().findByExample(tara);
		resultList.forEach( (t) ->  list.add(new TaraModel(t)) );
		return list;
	}
	
	private void validate(Tara tara) throws RegistroCadastradoException{
		verificaCadastroDuplicado(tara);
	}
	
	public void verificaCadastroDuplicado(Tara tara) throws RegistroCadastradoException{
		Map<String, Object> params = new HashMap<>();
		params.put("descricao", tara.getDescricao());
		Tara inDataBase = getDao().findOneByParams(params, false);
		
		if(inDataBase == null){
			return;
		}
		
		if(tara == null || !inDataBase.getId().equals(tara.getId())){
			throw new RegistroCadastradoException("Tara com esta descrição já foi cadastrada anteriomente.");
		}
	}
}
