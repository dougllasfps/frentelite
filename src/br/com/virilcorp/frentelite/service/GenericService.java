package br.com.virilcorp.frentelite.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import br.com.virilcorp.frentelite.dao.GenericDao;
import br.com.virilcorp.frentelite.dao.callback.StatementManager;
import br.com.virilcorp.frentelite.model.BaseModel;
import br.com.virilcorp.frentelite.persistence.EntityManagerObtainer;
import br.com.virilcorp.frentelite.persistence.EntityManagerObtainerFactory;

public abstract class GenericService<T extends BaseModel> {

	protected final GenericDao<T> entityDao;
	protected final Class<T> beanClass;
	
	@SuppressWarnings("unchecked")
	public GenericService() {
		EntityManagerObtainer obtainer = EntityManagerObtainerFactory.createEntityManagerObtainer();
		StatementManager manager = new StatementManager(obtainer);
		
		beanClass = (Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass() ).getActualTypeArguments()[0]; 
		this.entityDao = new GenericDao<T>(beanClass, manager);
	}
	
	public Class<T> getBeanClass() {
		return beanClass;
	}

	public GenericDao<T> getDao() {
		return entityDao;
	}
	
	public void save(T bean){
		getDao().insert(bean);
	}
	
	public void update(T bean){
		getDao().update(bean);
	}
	
	public void delete(T bean){
		getDao().delete(bean);
	}
	
	public T findById(Integer id){
		return getDao().findById(id);
	}
	
	public List<T> findAll(){
		return entityDao.findAll();
	}
	
	public T findFirst(){
		List<T> all = findAll();
		if(all != null && !all.isEmpty()){
			return all.get(0);
		}
		return null;
	}
}