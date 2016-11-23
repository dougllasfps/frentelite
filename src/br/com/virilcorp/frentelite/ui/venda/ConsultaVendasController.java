package br.com.virilcorp.frentelite.ui.venda;

import java.util.Calendar;
import java.util.Collection;

import br.com.virilcorp.converter.CalendarConverter;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.VendaService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ConsultaVendasController extends Controller{

	private VendaService vendaService;
	
	@FXML private TextField txtDataVenda;
	
	@FXML private TableView<VendaModel> dataTable;
	@FXML private TableColumn<VendaModel, String> numeroVendaColumn;
	@FXML private TableColumn<VendaModel, String> dataVendaColumn;
	@FXML private TableColumn<VendaModel, String> valorVendaColumn;
	
	@Override
	public void postInitialize() {
		vendaService = new VendaService();
		applyDataTableConfig();
		MaskUtils.dateField(txtDataVenda);
	}
	
	private void applyDataTableConfig() {
		numeroVendaColumn.setCellValueFactory( cell -> cell.getValue().getNumeroVenda() );
		dataVendaColumn.setCellValueFactory( cell -> cell.getValue().getDataVenda() );
		valorVendaColumn.setCellValueFactory( cell -> cell.getValue().getValorTotal() );
	}

	public void handleSearch(ActionEvent evt){
		Calendar value = new CalendarConverter().fromString(txtDataVenda.getText());
		
		if(value == null){
			value = Calendar.getInstance();
		}
		
		Collection<Venda> vendas = vendaService.findVendasByDate( value );
		dataTable.setItems(vendaService.toDataModelList(vendas));
		
		if(vendas.isEmpty()){
			addErrorMessage("msg.no_result");
		}
	}
	
	public void handleShowDetails(ActionEvent evt){
		VendaModel selectedItem = dataTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			return;
		}
		
		Venda data = selectedItem.getData();
		addParameter(Strings.VENDA.name(), data);
		getStageController().openScreenInANewWindow(ApplicationType.DETALHES_VENDA, getParameters(), getStage());
	}

	public TableView<VendaModel> getDataTable() {
		return dataTable;
	}

	public void setDataTable(TableView<VendaModel> dataTable) {
		this.dataTable = dataTable;
	}
}