package br.com.virilcorp.frentelite.ui.categoria;

import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.service.CategoriaService;
import br.com.virilcorp.frentelite.ui.CrudController;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class CategoriaController extends CrudController<Categoria, CategoriaModel, CategoriaService> implements Initializable{
	
	@FXML private TextField inputDescricao;
	@FXML private Button 	btnSalvar, btnPesquisar, btnExcluir, btnNovo;
	@FXML private TableColumn<CategoriaModel, String> descricaoColumn;
	
	@Override
	protected void prepareInsert() {
		super.prepareInsert();
	}

	public void cleanForm() {
		inputDescricao.setText("");
	}

	@Override
	public Categoria populateBeanWithFormData(Categoria t) {
		String descricao = inputDescricao.getText();
		t.setDescricao(descricao);
		return t;
	}
	
	@Override
	public void sendRegisterToForm(Categoria categoria) {
		inputDescricao.setText(categoria.getDescricao());
	}
	
	@Override
	public void cancel(){
		super.cancel();
	}

	@Override
	public FindActionCallBack getFindAction() {
		return () -> { 
			setBean(populateBeanWithFormData(new Categoria())); 
			ObservableList<CategoriaModel> list = getService().find(getBean()); 
			getDataTable().setItems(list);
			
			if(list == null || list.isEmpty()){
				addInfoMessage("Nenhum resultado encontrado.");
			}
		};
	}

	@Override
	public void postInitialize() {
		descricaoColumn.setCellValueFactory( ( cell) -> cell.getValue().getDescricao() );
		MaskUtils.upperCase(inputDescricao);
	}
}