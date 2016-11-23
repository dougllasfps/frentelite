/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.login;

import java.security.NoSuchAlgorithmException;

import org.hibernate.HibernateException;

import br.com.virilcorp.frentelite.exception.AutenticationException;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.service.UsuarioService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author DOUGLLASFPS
 */
public class LoginController extends Controller implements Initializable{

    private UsuarioService service;
    
    @FXML private TextField inputLogin, inputSenha;
    @FXML private ImageView logo;
    
    private static final ApplicationType THIS_TYPE = ApplicationType.LOGIN;
    
    @Override
	public void postInitialize() {
		service = new UsuarioService();
        MaskUtils.maxField( inputLogin, 10 );
        MaskUtils.maxField( inputSenha, 10 );
	}
    
    public void handleAutenticacao(){
    	getStageController().showLoadingModal(THIS_TYPE);
        try {
        	if(StringUtils.isNullOrEmpty(inputLogin.getText()) || StringUtils.isNullOrEmpty(inputSenha.getText())){
        		addErrorMessage("Informe o Login e a Senha.");
        		return;
        	}	
            String login = inputLogin.getText().trim();
            String senha = service.hashMD5String(inputSenha.getText().trim());
            Usuario usuario  = service.autenticar(login, senha);
            
            getApplicationContext().setAtributoSessao("usuario", usuario);
            
            getStageController().hideLoadingModal();
            getStageController().openMainScreen();
        } catch (AutenticationException ex) {
        	getStageController().hideLoadingModal();
            addErrorMessage(ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
        	getStageController().hideLoadingModal();
            addErrorMessage("Ocorreu um erro.");
        } catch (HibernateException e) {
        	getStageController().hideLoadingModal();
        	addErrorMessage("Ocorreu um erro ao tentar acessar o banco de dados.");
		} 
    }
    
    public void handleAutenticacao(ActionEvent evt){
        handleAutenticacao();
    }
    
    public void handleAutenticacaoEnterSubmit(KeyEvent evt){
    	if(!evt.getCode().equals(KeyCode.ENTER)){
    		return;
    	}
        handleAutenticacao();
    }
}
