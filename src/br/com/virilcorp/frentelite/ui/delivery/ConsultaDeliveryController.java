package br.com.virilcorp.frentelite.ui.delivery;

import java.util.Calendar;

import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.DeliveryService;
import br.com.virilcorp.frentelite.service.DialogFactory;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.Strings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ConsultaDeliveryController extends Controller {
	
	@FXML	private TextField txtData;
	@FXML	private TableView<DeliveryModel> dataTable;
	@FXML	private TableColumn<DeliveryModel, String> dataColumn;
	@FXML	private TableColumn<DeliveryModel, String> valorColumn;
	@FXML	private TableColumn<DeliveryModel, String> clienteColumn;
	@FXML	private TableColumn<DeliveryModel, String> telefoneColumn;
	@FXML	private TableColumn<DeliveryModel, String> situacaoColumn;
	
	private DeliveryService service = new DeliveryService();
	
	private void initComponents() {
		MaskUtils.dateField(txtData);
		txtData.setText( DateTimeUtils.format(Calendar.getInstance()) );
	}

	private void applyColumnsConfig() {
		dataColumn.setCellValueFactory( cell -> cell.getValue().getDataVenda() );
		valorColumn.setCellValueFactory( cell -> cell.getValue().getValor() );
		clienteColumn.setCellValueFactory( cell -> cell.getValue().getCliente() );
		telefoneColumn.setCellValueFactory( cell -> cell.getValue().getTelefone() );
		situacaoColumn.setCellValueFactory( cell -> cell.getValue().getSituacao() );
	}
	
	public void handleDetalhesVenda(ActionEvent evt){
		DeliveryModel selectedItem = dataTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			addErrorMessage("msg.selecao_obrigatoria");
			return;
		}
		
		Venda venda = selectedItem.getData().getVenda();
		getParameters().put(Strings.VENDA.name(), venda);
		getStageController().openScreenInANewWindow(ApplicationType.DETALHES_VENDA, getParameters(), getStage());
	}
	
	public void handleSearch(ActionEvent evt){
		try {
			String dataString = txtData.getText();
			Calendar data = DateTimeUtils.toCalendar(dataString);
			ObservableList<DeliveryModel> list = service.findByData(data);
			dataTable.setItems(list);
			
			if(list.isEmpty()){
				addInfoMessage("msg.no_result");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}
	
	public void handlePagamento(ActionEvent evt){
		DeliveryModel selectedItem = dataTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			addErrorMessage("msg.selecao_obrigatoria");
			return;
		}
		
		Delivery delivery = selectedItem.getData();
		
		if(delivery.getVenda().isPago()){
			addErrorMessage("msg.pagamento_ja_efetuado");
			return;
		}
		
		getParameters().put(Strings.VENDA.name(), delivery.getVenda());
		getStageController().openScreenInANewWindow(ApplicationType.PAGAMENTO, getParameters(), getStage());
	}
	
	public void handleCancelarEntrega(ActionEvent evt){
		DeliveryModel selectedItem = dataTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			addErrorMessage("msg.selecao_obrigatoria");
			return;
		}
		
		Delivery delivery = selectedItem.getData();
		
		if(delivery.getVenda().isPago()){
			addErrorMessage("msg.pagamento_ja_efetuado");
			return;
		}
		
		boolean confirm = DialogFactory.showConfirmDialog( getBundleMessage("msg.confirma_acao") );
		
		if(confirm){
			delivery.setCancelado(true);
			service.update(delivery);
			addInfoMessage("msg.sucess_insert");
		}
		
	}

	@Override
	public void postInitialize() {
		initComponents();
		applyColumnsConfig();
	}
}