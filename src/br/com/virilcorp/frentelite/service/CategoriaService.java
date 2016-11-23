package br.com.virilcorp.frentelite.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.collection.internal.PersistentBag;

import br.com.virilcorp.frentelite.exception.RegistroCadastradoException;
import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;
import br.com.virilcorp.frentelite.ui.categoria.CategoriaModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoriaService extends GenericService<Categoria> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProdutoService produtoService;
	
	public CategoriaService() {
		this.produtoService = new ProdutoService();
	}
	
	public ObservableList<CategoriaModel> find(final Categoria example){
		ObservableList<CategoriaModel> list = FXCollections.observableArrayList();
		List<Categoria> resultList = getDao().findByExample(example);
		resultList.forEach( (c) -> list.add(new CategoriaModel(c)) );
		return list;
	}
	
	public void carregaProdutosCategoria(Categoria categoria){
		List<Produto> produtos = categoria.getProdutos();

		if( !(produtos instanceof PersistentBag) && (produtos != null && !produtos.isEmpty()) ){
			return;
		}
		
		List<Produto> list = produtoService.findByCategoria(categoria);
		categoria.setProdutos(list);
	}
	
	public List<Categoria> findCategoriasComProdutos(){
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct c From Categoria c ");
		sb.append("inner join c.produtos ");
		return getDao().find(sb.toString(), new HashMap<>());
	}
	
	public List<Categoria> findByDescricaoLike(String descricao){
		QueryBuilder query = new QueryBuilder();
		query.append(" select p from Categoria p ");
		query.append(" where upper(p.descricao) like upper(:descricao) ");
		query.addParam("descricao", "%" +descricao+ "%");
		return getDao().find(query.getQuery(), query.getParams());
	}
	
	public void save(final Categoria cat){
		validate(cat);
		super.save(cat);
	}
	
	public void update(final Categoria cat){
		validate(cat);
		getDao().update(cat);
	}

	private void validate(final Categoria cat) throws RegistroCadastradoException {
		if(cat.getDescricao() == null || "".equals(cat.getDescricao().trim())){
			throw new ValidationException("msg.descricao_vazia");
		}
		verificaCadastroDuplicado(cat);
	}

	private void verificaCadastroDuplicado(final Categoria cat) throws RegistroCadastradoException {
		
		QueryBuilder query = new QueryBuilder();
		query.append(" select c from Categoria c ");
		query.append(" where c.descricao = '" + cat.getDescricao()+"'");
		List<Categoria> inDataBase = getDao().find(query);
		
		if(inDataBase == null || inDataBase.isEmpty() ){
			return;
		}
		
		if(cat.getId() == null || !cat.getId().equals(inDataBase.get(0).getId()) ){
			throw new RegistroCadastradoException("Categoria já cadastrada anteriormente.");
		}
	}
	
	public void delete(final Categoria bean) {
		validateDelete(bean);
		getDao().delete(bean);
	}

	private void validateDelete(Categoria bean) {
		List<Produto> produtos = produtoService.findByCategoria(bean);
		if(produtos != null && !produtos.isEmpty()){
			throw new ValidationException("msg.categoria_associada");
		}
	}
}