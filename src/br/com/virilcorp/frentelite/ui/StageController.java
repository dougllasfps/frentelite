/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.virilcorp.frentelite.persistence.PersistenceUtils;
import br.com.virilcorp.frentelite.service.ResourceBundleService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dougllas.sousa
 */
public class StageController {

    private final Map<ApplicationFactory.ApplicationType, Parent> screens;
    private final Map<ApplicationFactory.ApplicationType, Stage> stages;
    private final Map<ApplicationFactory.ApplicationType, Controller> controllers;
    
    private final Stage primaryStage;
    private Stage loadingModal;

    public StageController( Stage primaryStage ) {
        this.screens = new HashMap<ApplicationFactory.ApplicationType, Parent>();
        this.stages = new HashMap<ApplicationFactory.ApplicationType, Stage>();
        this.controllers = new HashMap<ApplicationFactory.ApplicationType, Controller>();
        this.primaryStage = primaryStage;
    }
    
    @SuppressWarnings("unchecked")
	public <T> T getController(ApplicationFactory.ApplicationType applicationType){
        return (T) controllers.get(applicationType);
    }
    
    public Stage getStage(ApplicationFactory.ApplicationType applicationType){
    	 return stages.get(applicationType);
    }
    
    private void registerScreen(ApplicationFactory.ApplicationType applicationType, Parent parent) {
        screens.put(applicationType, parent);
    }
    
    private void registerController(ApplicationFactory.ApplicationType applicationType, Controller controller) {
        controllers.put(applicationType, controller);
    }
    
    private void registerStage(ApplicationFactory.ApplicationType applicationType, Stage stage) {
        stages.put(applicationType, stage);
    }
    
    private void unregisterScreen(ApplicationFactory.ApplicationType applicationType) {
        screens.remove(applicationType);
    }
    
    private void unregisterController(ApplicationFactory.ApplicationType applicationType) {
        controllers.remove(applicationType);
    }
    
    private void unregisterStage(ApplicationFactory.ApplicationType applicationType) {
        stages.remove(applicationType);
    }

    public Parent loadScreen(final ApplicationFactory.ApplicationType applicationType) {
        
        String resource = applicationType.getResource();
        String path = ApplicationFactory.FXML_PACKAGE_LOCATION + resource;
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( getClass().getResource(path), ResourceBundleService.getInstance() );
            Parent loadScreen = (Parent) fxmlLoader.load();
            
            Controller controller  = ((Controller) fxmlLoader.getController());
            controller.setStageController(this);
            controller.setApplication(applicationType);
            
            registerScreen(applicationType, loadScreen);
            registerController(applicationType, controller);
            
            return loadScreen;
        } catch (LoadException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            return null;
		}
    }
    
    public void showLoadingModal(ApplicationType owner){
    	Stage stage = getStage(owner);
    	loadingModal = createLoadModal();
    	loadingModal.initOwner(stage);
		loadingModal.show();
    }
    
    public void showLoadingModal(Stage stage){
    	loadingModal = createLoadModal();
    	loadingModal.initOwner(stage);
		loadingModal.show();
    }
    
    public void hideLoadingModal(){
    	if(loadingModal !=null && loadingModal.isShowing())
    		loadingModal.close();
    }

	private Stage createLoadModal() {
		try {
			Parent parent = loadParent(ApplicationType.LOADING);
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.WINDOW_MODAL);
			return stage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Parent loadParent(ApplicationType applicationType) throws IOException {
		String resource = applicationType.getResource();
		String path = ApplicationFactory.FXML_PACKAGE_LOCATION + resource;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		Parent parent = (Parent) loader.load();
		return parent;
	}
    
    public void closeScreen(final ApplicationFactory.ApplicationType applicationType){
        Stage stage = stages.get(applicationType);
        unregisterScreen(applicationType);
        unregisterStage(applicationType);
        unregisterController(applicationType);
        
        if(stage != null ){
            stage.close();
        }
    }
    
    public void openMainScreen(){
    	loadInPrimaryStage(ApplicationFactory.MAIN);
    }
    
    public void loadPrimaryStage(){
    	loadInPrimaryStage(ApplicationFactory.INITIAL_SCREEN);
    }
    
    private void loadInPrimaryStage(ApplicationType type){
    	 Parent mainScreen = loadScreen(type);
         Scene scene = new Scene(mainScreen);
         
         primaryStage.setScene(scene);
         primaryStage.setTitle(type.getTitle());
         primaryStage.centerOnScreen();
         primaryStage.setResizable(false);
         primaryStage.show();
         
         Controller controller = getController(type);
         
         if(controller != null){
        	 controller.setStage(primaryStage);
         }
         
         applyScreenConfigurations(type, scene, primaryStage);
         
         if(ApplicationFactory.MAIN.equals(type)){
        	 primaryStage.setIconified(false);
         }
         
    }
    
    public void openScreenInANewWindow(final ApplicationFactory.ApplicationType applicationType){
       openScreenInANewWindow(applicationType, null);
    }
    
    public void openScreenInANewWindow(final ApplicationFactory.ApplicationType applicationType, boolean fullScreen){
        openScreenInANewWindow(applicationType, null, fullScreen);
     }
    
    public void openScreenInANewWindow(final ApplicationFactory.ApplicationType applicationType, Map<String, Object> params, boolean fullScreen){
        openScreenInANewWindow(applicationType, params, null, fullScreen);
    }
    
    public void openScreenInANewWindow(final ApplicationFactory.ApplicationType applicationType, Map<String, Object> params){
        openScreenInANewWindow(applicationType, params, null, false);
    }
    
    public void openScreenInANewWindow(final ApplicationFactory.ApplicationType applicationType, Map<String, Object> params, Stage stage){
        openScreenInANewWindow(applicationType, params, stage, false);
    }
    
    public void openScreenInANewWindow(final ApplicationFactory.ApplicationType applicationType, Map<String, Object> params, Stage owner, boolean fullScreen){
        
        if(isOpen(applicationType)){
            return;
        }
        
        Parent parent = screens.get(applicationType);
        if(parent == null){
            parent = loadScreen(applicationType);
        }
        
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(applicationType.getTitle());
        
        stage.centerOnScreen();
        stage.setResizable(false);
        
        Controller controller = getController(applicationType);
        
        if(controller != null){
        	initControllerProperties( controller, params, stage, scene);
        }
        
        applyScreenConfigurations(applicationType, scene, stage, owner);
        registerStage(applicationType, stage);
        
    	stage.setFullScreen(fullScreen);
        
        stage.show();
    }

	private void initControllerProperties(Controller controller, Map<String, Object> params, Stage stage, Scene scene) {
		controller.setStage(stage);
		controller.setScene(scene);
		controller.setParameters(params);
	}

	private void applyScreenConfigurations(final ApplicationFactory.ApplicationType applicationType, final Scene scene, final Stage stage) {
		applyScreenConfigurations(applicationType, scene, stage, null);
	}
	
	private void applyScreenConfigurations(final ApplicationFactory.ApplicationType applicationType, final Scene scene, final Stage stage, final Stage owner) {
		registerStage(applicationType, stage);
		
		//para a tecla ESC fechar qualquer tela... obrigado.
        Platform.runLater(() -> {
            ObservableMap<KeyCombination, Runnable> accelerators = scene.getAccelerators();

            accelerators.put(new KeyCodeCombination(KeyCode.ESCAPE), (Runnable) () -> {
                closeScreen(applicationType);
            });

        });
        
        if(applicationType.in(ApplicationFactory.getFinalizableApplications())){
            stage.setOnCloseRequest((WindowEvent event) -> {
                closeScreen(applicationType);
                PersistenceUtils.closeEntityManagerFactory();
                System.exit(0);
            });
        }else{
            stage.setOnCloseRequest((WindowEvent event) -> {
                closeScreen(applicationType);
                unregisterScreen(applicationType);
                unregisterStage(applicationType);
            });
        }
        
        if(applicationType.in(ApplicationFactory.getMAximizableApplications())){
        	stage.setMaximized(true);
        }
        
        stage.setResizable(false);
        
        Stage ownerStage = owner == null ? getStage(applicationType.getOwner()) : owner;
        
		if(ownerStage != null ){
        	stage.initModality(Modality.WINDOW_MODAL);
        	stage.initOwner(ownerStage);
        }
	}
    
    public boolean isOpen(ApplicationFactory.ApplicationType type){
        return stages.get(type) != null && stages.get(type).isShowing();
    }
}