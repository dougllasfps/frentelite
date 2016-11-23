/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.virilcorp.frentelite.Main;
import br.com.virilcorp.frentelite.context.Session;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.ui.ApplicationFactory;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DOUGLLASFPS
 */
public class MainWindowController extends Controller implements Initializable {
    
    @FXML private Label lblUsuario, lblUltimoAcesso, lblStatusImpressora, lblStatusBalanca;
    @FXML private MenuBar barraDeMenu;
    
    public void handleExit(ActionEvent evt){
        System.exit(1);
    }
    
    public void handleMenuVenda(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
        final ApplicationFactory.ApplicationType VENDA = ApplicationFactory.ApplicationType.VENDA;
        openWindow(VENDA);
        getStageController().hideLoadingModal();
    }
    
    public void handleMenuConsultaVenda(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
        openWindow(ApplicationFactory.ApplicationType.CONSULTA_VENDAS);
        getStageController().hideLoadingModal();
    }
    public void handleMenuDelivery(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationFactory.ApplicationType.CONSULTA_DELIVERY);
    	getStageController().hideLoadingModal();
    }
    
    public void handleMenuTara(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationType.TARA);
    	getStageController().hideLoadingModal();
    }
    
    public void handleMenuFluxoCaixa(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationType.GERENCIAMENTO_CAIXA);
    	 getStageController().hideLoadingModal();
    }
    public void handleMenuConfigBalanca(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationType.CONFIG_BALANCA);
    	 getStageController().hideLoadingModal();
    }
    
    public void handleMenuEstabelecimento(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationType.CONFIG_ESTABELECIMENTO);
    	getStageController().hideLoadingModal();
    }
    
    public void handleMenuConfigImpressora(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationType.CONFIG_IMPRESSORA);
    	 getStageController().hideLoadingModal();
    }
    
    public void handleMenuCategoria(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
    	openWindow(ApplicationType.CATEGORIA);
    	 getStageController().hideLoadingModal();
    }
    
    public void handleMenuRelatorioVendas(ActionEvent e){
    	Parent parent = getStageController().loadScreen(ApplicationType.RELATORIO_VENDAS);
    	Stage stage = new Stage();
    	Scene scene = new Scene(parent, 900, 600);
    	stage.setScene(scene);
    	stage.show();
    }
    
    public void handleMenuProduto(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
        openWindow(ApplicationType.CADASTRO_PRODUTO);
        getStageController().hideLoadingModal();
    }
    
    public void handleMenuUsuario(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
        openWindow(ApplicationType.USUARIO);
        getStageController().hideLoadingModal();
    }
    
    public void handleMenuGerenciamentoCaixa(ActionEvent e){
    	getStageController().showLoadingModal(getApplication());
        openWindow(ApplicationType.GERENCIAMENTO_CAIXA);
        getStageController().hideLoadingModal();
    }
    
    private void openWindow(final ApplicationFactory.ApplicationType app) {
        openWindow(app, false);
    }
    
    private void openWindow(final ApplicationFactory.ApplicationType app, boolean fullScreen) {
        if(!getStageController().isOpen(app))
            getStageController().openScreenInANewWindow(app, fullScreen);
        else 
            addInfoMessage("Janela já encontra-se aberta!");
    }


	@Override
	public void postInitialize() {
		Usuario usuario = (Usuario) Session.get("usuario");
    	
    	if(usuario != null){
    		lblUsuario.setText(usuario.getNome());
    	}
    	
    	lblUltimoAcesso.setText(new SimpleDateFormat("dd/MM/yyyy HH:ss").format(new Date()));
    	
    	if(barraDeMenu != null){
    		barraDeMenu.prefWidthProperty().bind(getStage().widthProperty());
    	}
    	
    	Platform.runLater(()->{
    		if(Main.IMPRESSORA_OFFLINE){
    			lblStatusImpressora.setText("Status Impressora: Offline");
    		}else{
    			lblStatusImpressora.setText("Status Impressora: Online");
    		}
    	});
	}
}