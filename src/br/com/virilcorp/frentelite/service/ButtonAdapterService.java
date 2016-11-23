package br.com.virilcorp.frentelite.service;

import java.io.IOException;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.ui.component.CategoriaButton;
import br.com.virilcorp.frentelite.ui.component.ProdutoButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class ButtonAdapterService {

	public static CategoriaButton createCategoriaButton(Categoria categoria, int width, int height, EventHandler<ActionEvent> evt){
		CategoriaButton button = new CategoriaButton(categoria);
		button.setOnAction(evt);
		button.setMaxSize(width, height);
		button.setMinSize(width, height);
		addDefaultButtonStyle(button);
		return button;
	}
	
	public static Button createCustomButton(String label, int width, int height, EventHandler<ActionEvent> evt) {
		Button button = new Button();
		button.setText(label);
		button.setOnAction(evt);
		button.setMaxSize(width, height);
		button.setMinSize(width, height);
		button.setMaxHeight(height);
		button.setMaxWidth(width);
		button.setMinHeight(height);
		button.setMinWidth(width);
		addDefaultButtonStyle(button);
		return button;
	}
	
	public static ProdutoButton createProdutoButton(Produto produto, int width, int height, EventHandler<ActionEvent> evt) throws IOException{
		ProdutoButton button = new ProdutoButton(produto);
		button.setOnAction(evt);
		button.setMaxSize(width, height);
		button.setMinSize(width, height);
		button.setMaxHeight(height);
		button.setMaxWidth(width);
		button.setMinHeight(height);
		button.setMinWidth(width);
		button.setContentDisplay( ContentDisplay.TOP );

		if(produto.getFoto() != null && produto.getFoto().getBytes() != null){
			FotoService.loadFoto(produto);
			ImageView imageView = new ImageView(produto.getImage());
			
			imageView.setFitHeight(80);
			imageView.setFitWidth(80);
			imageView.setPreserveRatio(true);
			
			button.setGraphic(imageView);
		}
			
		String precoString = NumericConverter.formatCurrent( button.getProduto().getValorVenda() );
		Tooltip tooltip = new Tooltip( button.getProduto().getDescricao() + " - " + precoString );
		button.setTooltip( tooltip );
		
		addDefaultButtonStyle(button);
		return button;
	}
	
	public static void addDefaultButtonStyle(Button button){
		if(button == null){
			return;
		}
		
		ObservableList<String> styleClass = button.getStyleClass();
		styleClass.add("btn");
//		styleClass.add("btn-sm");
		styleClass.add("btn-default");
	}
}