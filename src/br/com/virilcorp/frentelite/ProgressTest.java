package br.com.virilcorp.frentelite;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ProgressTest extends Application {
	  public static void main(String[] args) { Application.launch(args); }
	  @Override public void start(Stage stage) {
	    Task<Void> task = new Task<Void>() {
	      @Override public Void call() {
	        for (int i = 1; i < 10; i++) {
	          try {
	            Thread.sleep(3000);
	          } catch (InterruptedException e) {
	            e.printStackTrace();
	          }
	          System.out.println(i);
	          updateProgress(i, 1);
	          
	        }
	        return null;
	      }
	    };

	    ProgressBar updProg = new ProgressBar();
	    updProg.progressProperty().bind(task.progressProperty());

	    Thread th = new Thread(task);
	    th.setDaemon(true);
	    th.start();

	    StackPane layout = new StackPane();
	    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
	    layout.getChildren().add(updProg);

	    stage.setScene(new Scene(layout));
	    stage.show();
	  }
	}
