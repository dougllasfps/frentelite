/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import br.com.virilcorp.frentelite.context.ApplicationContext;
import br.com.virilcorp.frentelite.service.ResourceBundleService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author dougllas.sousa
 */
public abstract class Controller implements Initializable{
    
    private StageController stageController;
    private Stage stage;
    private Scene scene;
    private ResourceBundle bundle;
    private Map<String, Object> parameters;
    private DialogMessage dialogMessage;
    private ApplicationType application;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setBundle(resources);

    	Platform.runLater( ()->{
    		postInitialize();
    	});
    }

    public abstract void postInitialize();
    
    public Controller() {
    	dialogMessage = new DialogMessage(ResourceBundleService.getInstance());
	}
    
    public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		dialogMessage.setBundle(bundle);
		this.bundle = bundle;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    public StageController getStageController() {
        return stageController;
    }

	public Map<String, Object> getParameters() {
		if(parameters == null)
			parameters = new HashMap<>();
		return parameters;
	}

	public void addParameter(String name, Object value) {
		if(this.parameters == null){
			this.parameters = new HashMap<>();
		}
		this.parameters.put(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParameter(String name){
		if(this.parameters == null)
			return null;
		return (T) this.parameters.get(name);
	}

	public String getBundleMessage(String key){
		try {
			if(bundle == null)
				return null;
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	public void addInfoMessage(String msg){
        dialogMessage.addMessage(msg, Alert.AlertType.INFORMATION);
    }
    
    public void addErrorMessage(String msg){
    	dialogMessage.addMessage(msg, Alert.AlertType.ERROR);
    }
    
    public void sendParams(ApplicationType application){
    	Controller controller = getStageController().getController(application);
    	
    	if(controller == null){
    		return;
    	}
    	
    	controller.parameters = new HashMap<>(parameters);
    }

	public ApplicationType getApplication() {
		return application;
	}

	public void setApplication(ApplicationType applicationType) {
		this.application = applicationType;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public ApplicationContext getApplicationContext(){
		return ApplicationContext.getInstance();
	}
}