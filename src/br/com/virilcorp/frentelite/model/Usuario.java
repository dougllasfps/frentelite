/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.virilcorp.frentelite.service.ModuloService;

/**
 *
 * @author DOUGLLASFPS
 */

@Entity
@Table (name = "usuario", schema = "acesso")
public class Usuario implements BaseModel{
    
    @Id @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private String nome;
    
    @Column
    private String login;
    
    @Column
    private String senha;
    
    @Column
    private Boolean administrador;
    
    @OneToMany(mappedBy = "usuario")
    private List<ModuloUsuario> modulos;

    public Usuario() {
    }

    public Usuario(Integer id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

	public Boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean master) {
		this.administrador = master;
	}

	public List<ModuloUsuario> getModulos() {
		return modulos;
	}

	public void setModulos(List<ModuloUsuario> modulos) {
		this.modulos = modulos;
	}
	
	public boolean hasAcessoModulo(ModuloService service , TipoModulo modulo){
		Modulo m = service.findByNome(modulo);
		return hasAcessoModulo(m);
	}
	
	public boolean hasAcessoModulo(Modulo modulo){
		if(id == null){
			return false;
		}
		
		if(this.modulos == null || this.modulos.isEmpty()){
			return false;
		}
		
		for (ModuloUsuario moduloUsuario : modulos) {
			if(moduloUsuario.getModulo().getId().equals(modulo.getId()))
				return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Nome: " +this.nome + ", Login: " + this.login;
	}
}