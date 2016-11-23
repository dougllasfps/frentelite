package br.com.virilcorp.frentelite.ui.usuario;

import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.ui.DataModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuarioModel implements DataModel<Usuario> {
	
	private StringProperty login;
	private StringProperty	senha;
	private StringProperty nome;
	private StringProperty	administrador;
	private Usuario usuario;
	
	public UsuarioModel() {
	}
	
	public UsuarioModel(Usuario usuario) {
		this.usuario = usuario;
		setLogin(usuario.getLogin());
		setSenha(usuario.getSenha());
		setAdministrador( usuario.isAdministrador() );
	}

	public StringProperty getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = new SimpleStringProperty(login);
	}

	public StringProperty getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = new SimpleStringProperty(senha);
	}

	public StringProperty getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = new SimpleStringProperty(nome);
	}

	public StringProperty getAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = new SimpleStringProperty(administrador ? "Sim" : "Não");
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Usuario getData() {
		return getUsuario();
	}

}
