/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.virilcorp.frentelite.context.Session;
import br.com.virilcorp.frentelite.dao.callback.QueryCallBack;
import br.com.virilcorp.frentelite.dao.callback.StatementManager;
import br.com.virilcorp.frentelite.dao.callback.TransactionCallBack;
import br.com.virilcorp.frentelite.exception.AutenticationException;
import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.ModuloUsuario;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.util.StringUtils;

/**
 *
 * @author DOUGLLASFPS
 */
public class UsuarioService extends GenericService<Usuario> {

    private final StatementManager statementManager;

    public UsuarioService() {
    	super();
    	this.statementManager = getDao().getStatementExecutor();
    }
    
    public void validate(Usuario usuario){
    	 StringBuilder errorMessage = new StringBuilder();
         boolean error = false;
         
         if(StringUtils.isNullOrEmpty(usuario.getLogin())){
             error = true;
             errorMessage.append("O Login é obrigatório.\n");
         }
         
         if(StringUtils.isNullOrEmpty(usuario.getNome())){
             error = true;
             errorMessage.append("O nome do usuário é obrigatório.\n");
         }
         
         if(!usuario.isAdministrador() && ( usuario.getModulos() == null || usuario.getModulos().isEmpty()) ){
             error = true;
             errorMessage.append("Selecione pelo menos um módulo.\n");
         }
         
         if(error){
             throw new ValidationException(errorMessage.toString());
         }
    }
    
    public List<ModuloUsuario> findModulosByUsuario(Usuario usuario){
    	return this.statementManager.executeQuery(new QueryCallBack<List<ModuloUsuario>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ModuloUsuario> doQuery(EntityManager entityManager) {
				Query query = entityManager.createQuery("from ModuloUsuario where usuario = :usuario ");
				query.setParameter("usuario", usuario);
				return query.getResultList();
			}
		});
    }
    
    public void save(Usuario usuario){
    	validate(usuario);
    	List<ModuloUsuario> modulos = usuario.getModulos();
    	this.statementManager.executeCallBack(new TransactionCallBack() {
			@Override
			public int doInTransaction(EntityManager entityManager) {
				entityManager.persist(usuario);
				modulos.forEach( obj -> { entityManager.persist(obj);  });
				return 0;
			}
		});
    }
    
    public void update(Usuario usuario){
    	validate(usuario);
    	this.statementManager.executeCallBack(new TransactionCallBack() {
			@Override
			public int doInTransaction(EntityManager entityManager) {
				entityManager.merge(usuario);
				updateModulos(entityManager, usuario);
				return 0;
			}
		});
    }
    
    private void updateModulos(EntityManager entityManager, Usuario usuario){
		StringBuffer hql = new StringBuffer( "delete from ModuloUsuario where id not in ( ");
		
		usuario.getModulos().forEach(obj -> {
			if(obj.getId() == null){
				entityManager.persist(obj);
			}
			
			if( usuario.getModulos().indexOf(obj) == usuario.getModulos().size() - 1 )
				hql.append( obj.getId() + ")" );
			else
				hql.append( obj.getId() + ","  );
		});
		
		entityManager.createQuery(hql.toString()).executeUpdate();
    }

    public Usuario findByLoginAndSenha(String login, String senha) throws NoSuchAlgorithmException {
        String sql = "select * from acesso.usuario u where u.login = '"+login+"' and u.senha = '"+senha+"'"; 
        List<Usuario> list = this.getDao().findBySQLQuery(sql, null);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    public Usuario autenticar(String login, String senha) throws AutenticationException, NoSuchAlgorithmException {
        Usuario usuario = findByLoginAndSenha(login, senha);

        if (usuario == null) {
            throw new AutenticationException("Login e/ou senha incorreto(s).");
        }
        return usuario;
    }
    
    public void putUsuarioInSession(Usuario usuario) {
        Session.put("usuario", usuario);
    }

    public String hashMD5String(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public String generateRandomPassword(int size) {
        String[] carct = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};

        String senha = "";
        for (int x = 0; x < size; x++) {
            int j = (int) (Math.random() * carct.length);
            senha += carct[j];
        }

        return senha;
    }
}