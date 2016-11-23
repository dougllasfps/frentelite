/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import br.com.virilcorp.frentelite.dao.callback.QueryCallBack;
import br.com.virilcorp.frentelite.dao.callback.StatementManager;
import br.com.virilcorp.frentelite.dao.callback.WriteEvent;
import br.com.virilcorp.frentelite.model.BaseModel;
import br.com.virilcorp.frentelite.persistence.EntityManagerObtainerImpl;
import br.com.virilcorp.frentelite.persistence.QueryBuilder;

/**
 * @author DOUGLLASFPS
 * @param <T>
 */
public class GenericDao<T extends BaseModel> implements Serializable {

	private static final long serialVersionUID = -6662099351453627312L;
	private Class<T> entidade;
	private StatementManager manager;

	public GenericDao( final Class<T> clazz, final StatementManager manager ) {
		this.entidade = clazz;
		this.manager = manager;
	}

	public Integer insert(T t) {
		return manager.executeUpdate(WriteEvent.PERSIST, t);
	}

	public void update(T t) {
		manager.executeUpdate(WriteEvent.UPDATE, t);
	}

	public void delete(T t) {
		manager.executeUpdate(WriteEvent.DELETE, t);
	}

	@SuppressWarnings("hiding")
	public <T> T findById(final Integer id) {
		return this.manager.executeQuery( em->  em.find(entidade, id) );
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return manager.executeQuery((QueryCallBack<List<T>>) (EntityManager entityManager) -> entityManager
				.createQuery("from " + entidade.getSimpleName()).getResultList());
	}
	
	public Filter ativarFiltroEntidade(String name){
		return manager.enableFilter(name);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T t) {
		Example example = Example.create(t);
		example.excludeZeroes();
		example.ignoreCase();
		example.enableLike(MatchMode.ANYWHERE);
		return createCriteria().add(example).list();
	}

	public List<T> findByParams(Map<String, Object> params, boolean enableLike) {
		return manager.executeQuery(new QueryCallBack<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doQuery(EntityManager entityManager) {
				String queryS = generateQuery(params, enableLike);
				Query query = entityManager.createQuery(queryS);
				addQueryParams(query, params);
				return query.getResultList();
			}
		});
	}
	
	@SuppressWarnings("hiding")
	public <T> List<T> find(QueryBuilder query) {
		return find(query.getQuery(), query.getParams(), null);
	}
	
	@SuppressWarnings("hiding")
	public <T> List<T> find(QueryBuilder query, Integer maxResults) {
		return find(query.getQuery(), query.getParams(), maxResults);
	}

	@SuppressWarnings("hiding")
	public <T> List<T> find(String query, Map<String, Object> params) {
		return find(query, params, null);
	}
	
	@SuppressWarnings("hiding")
	public <T> List<T> find(String query, Map<String, Object> params, Integer maxResult) {
		return manager.executeQuery(new QueryCallBack<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doQuery(EntityManager entityManager) {
				Query q = entityManager.createQuery(query);
				if(maxResult != null)
					q.setMaxResults(maxResult);
				addQueryParams(q, params);
				return q.getResultList();
			}
		});
	}
	
	@SuppressWarnings("hiding")
	public <T> T findOne(String query, Map<String, Object> params){
		return findOne(query, params,null);
	}
	
	@SuppressWarnings("hiding")
	public <T> T findOne(QueryBuilder query){
		return findOne(query.getQuery(), query.getParams(), null);
	}
	
	@SuppressWarnings("hiding")
	public <T> T findOne(String query, Map<String, Object> params, Integer maxResult) {
		return manager.executeQuery(new QueryCallBack<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doQuery(EntityManager entityManager) {
				Query q = entityManager.createQuery(query);
				if(maxResult != null)
					q.setMaxResults(maxResult);
				addQueryParams(q, params);
				try{
					return  (T) q.getSingleResult();
				}catch (NoResultException e) {
					return null;
				}
			}
		});
	}

	public List<T> findBySQLQuery(String query, Map<String, Object> params) {
		return manager.executeQuery(new QueryCallBack<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doQuery(EntityManager entityManager) {
				SQLQuery q = entityManager.unwrap(Session.class).createSQLQuery(query);
				q.addEntity(getEntidade());
				if (params != null)
					addQueryParams(q, params);
				return q.list();
			}
		});
	}

	public T findOneByParams(Map<String, Object> params, boolean enableLike) {
		return manager.executeQuery(new QueryCallBack<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doQuery(EntityManager entityManager) {
				String queryS = generateQuery(params, enableLike);
				Query query = entityManager.createQuery(queryS);
				addQueryParams(query, params);
				List<T> resultList = query.getResultList();

				if (resultList != null && !resultList.isEmpty()) {
					return (T) resultList.get(0);
				} else {
					return null;
				}
			}
		});
	}

	private String generateQuery(Map<String, Object> params, boolean enableLike) {
		StringBuffer sb = new StringBuffer();
		sb.append("select obj from ");
		sb.append(entidade.getSimpleName());
		sb.append(" obj where 1 = 1 ");

		params.keySet().stream().forEach((k) -> {

			if (enableLike && params.get(k) instanceof String) {
				sb.append(" and obj." + k + " ilike :" + k.replaceAll("\\.", ""));
			} else {
				sb.append(" and obj." + k + " = :" + k.replaceAll("\\.", ""));
			}

		});

		return sb.toString();
	}

	private void addQueryParams(SQLQuery query, Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return;
		}

		params.keySet().stream().forEach((k) -> {
			Object obj = params.get(k);
			if (obj instanceof String) {
				query.setParameter(k, "%" + params.get(k.replaceAll("\\.", "")) + "%");
			} else {
				query.setParameter(k.replaceAll("\\.", ""), obj);
			}
		});
	}

	private void addQueryParams(Query query, Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return;
		}

		params.keySet().stream().forEach((k) -> {
			Object obj = params.get(k);
			if (obj instanceof String) {
				query.setParameter(k, "%" + params.get(k.replaceAll("\\.", "")) + "%");
			} else {
				query.setParameter(k.replaceAll("\\.", ""), obj);
			}
		});
	}

	public Criteria createCriteria() {
		Session session = new EntityManagerObtainerImpl().obtain().unwrap(Session.class);
		return session.createCriteria(entidade);
	}

	public Class<T> getEntidade() {
		return entidade;
	}

	public void setEntidade(Class<T> entidade) {
		this.entidade = entidade;
	}

	public StatementManager getStatementExecutor() {
		return manager;
	}

	public void setStatementExecutor(StatementManager statementExecutor) {
		this.manager = statementExecutor;
	}
}
