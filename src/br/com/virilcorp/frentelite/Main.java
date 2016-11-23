/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite;


import br.com.virilcorp.frentelite.environment.DefaultFrenteLiteInitializer;
import br.com.virilcorp.frentelite.environment.FrenteLiteInitializer;
import br.com.virilcorp.frentelite.ui.StageController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 *
 * @author DOUGLLASFPS
 */
public class Main extends Application {
	
	public static boolean IMPRESSORA_OFFLINE = false;
    
    @Override
    public void start(Stage stage) throws Exception {
        StageController controller = new StageController(stage);
        controller.loadPrimaryStage();
        FrenteLiteInitializer initializer = new DefaultFrenteLiteInitializer();
        
        Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					initializer.inicializaImpressora();
				} catch (Exception e) {
					e.printStackTrace();
					Main.IMPRESSORA_OFFLINE = true;
				}
				
				return null ;
			}
		};
    	
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
		
		
		Task<Void> task2 = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					initializer.inicializaBalanca();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};

		Thread t2 = new Thread(task2);
		t2.setDaemon(true);
		t2.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
//    	LauncherImpl.launchApplication(Main.class, PreLoader.class, args);
    }
    
}
