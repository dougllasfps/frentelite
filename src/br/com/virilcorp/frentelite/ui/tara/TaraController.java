package br.com.virilcorp.frentelite.ui.tara;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Tara;
import br.com.virilcorp.frentelite.service.TaraService;
import br.com.virilcorp.frentelite.ui.CrudController;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class TaraController extends CrudController<Tara, TaraModel, TaraService> implements Initializable{
	
	@FXML private TextField inputDescricao, inputPeso;
	@FXML private TableColumn<TaraModel, String> descricaoColumn;
	@FXML private TableColumn<TaraModel, String> pesoColumn;
	
	private void executeColumnsConfigurations(){
		descricaoColumn.setCellValueFactory( cell -> cell.getValue().getDescricao() );
		pesoColumn.setCellValueFactory( cell -> cell.getValue().getPeso() );
	}
	
	public void cleanForm(){
		inputDescricao.setText("");
		inputPeso.setText("");
	}
	
	@Override
	public Tara populateBeanWithFormData(Tara t) {
		String descricao = inputDescricao.getText();
		String peso = inputPeso.getText();
		
		t.setDescricao(descricao);
		t.setPeso(NumericConverter.convertBigDecimal(peso, true));
		
		return t;
	}

	@Override
	public void sendRegisterToForm(Tara t) {
		inputDescricao.setText(t.getDescricao());
		if(t.getPeso() != null)
			inputPeso.setText(NumericConverter.formatNumber(t.getPeso(), 3));
	}

	@Override
	public DeleteActionCallBack getDeleteAction() {
		return () -> {	getService().delete(getBean()); super.prepareSearch();	};
	}

	@Override
	public UpdateActionCallBack getUpdateAction() {
		return () -> {	getService().update(getBean()); };
	}

	@Override
	public InsertActionCallBack getInsertAction() {
		return () -> {	getService().save(getBean());};
	}

	@Override
	public FindActionCallBack getFindAction() {
		return () -> {
			
			setBean(populateBeanWithFormData(new Tara()));
			
			ObservableList<TaraModel> list = getService().find(getBean());
			getDataTable().setItems(list);
			
			if(list.isEmpty()){
				addInfoMessage("Nenhum registro encontrado.");
			}
				
		};
	}

	@Override
	public void postInitialize() {
		executeColumnsConfigurations();
		MaskUtils.upperCase(inputDescricao);
		MaskUtils.maxField(inputDescricao, 40);
		MaskUtils.weightField(inputPeso);
	}
}