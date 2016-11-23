package br.com.virilcorp.frentelite.util;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class ComponentUtils {

	public static <T> void createContextMenu(TableView<T> dataTable, EventHandler<ActionEvent> evt, String label){
		dataTable.setRowFactory((TableView<T> param) -> {
			final TableRow<T> row = new TableRow<>();

			final ContextMenu menu = new ContextMenu();
			final MenuItem menuItem = new MenuItem(label);

			menuItem.setOnAction(evt);

			menu.getItems().add(menuItem);

			row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(menu));

			return row;
		});
	}
}
