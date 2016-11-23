package br.com.virilcorp.frentelite;

import br.com.virilcorp.frentelite.environment.DefaultFrenteLiteInitializer;
import br.com.virilcorp.frentelite.environment.FrenteLiteInitializer;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PreLoader extends Preloader {

	private Stage preloaderStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.preloaderStage = primaryStage;

		VBox loading = new VBox(20);
		loading.setMaxWidth(Region.USE_PREF_SIZE);
		loading.setMaxHeight(Region.USE_PREF_SIZE);
		loading.getChildren().add(new ProgressBar());
		Label label = new Label("Aguarde... inicializando");
		loading.getChildren().add(label);

		BorderPane root = new BorderPane(loading);
		Scene scene = new Scene(root);

		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				FrenteLiteInitializer initializer = new DefaultFrenteLiteInitializer();
				try {
					initializer.inicializaImpressora();
				} catch (Exception e) {
					e.printStackTrace();
					Main.IMPRESSORA_OFFLINE = true;
				}
				
				try {
					initializer.inicializaBalanca();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null ;
			}
		};
    	
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
		if (stateChangeNotification.getType() == Type.BEFORE_START) {
			preloaderStage.hide();
		}
	}

}
