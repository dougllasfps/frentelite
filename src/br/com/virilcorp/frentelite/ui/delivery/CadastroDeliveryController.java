package br.com.virilcorp.frentelite.ui.delivery;

import br.com.virilcorp.converter.BigDecimalMonetaryConverter;
import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.relatorio.cupom.Cupom;
import br.com.virilcorp.daruma.relatorio.cupom.CupomService;
import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.Cliente;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.DeliveryService;
import br.com.virilcorp.frentelite.service.DialogFactory;
import br.com.virilcorp.frentelite.service.ResourceBundleService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.FormDataHandler;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.ui.venda.VendaController;
import br.com.virilcorp.frentelite.util.StringUtils;
import br.com.virilcorp.frentelite.util.Strings;
import br.com.virilcorp.frentelite.util.TelefoneHandler;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CadastroDeliveryController extends Controller implements FormDataHandler<Delivery>{

	@FXML	private TextField inputTotal;
	@FXML	private TextField inputNome;
	@FXML	private TextField inputTelefone;
	@FXML	private TextField inputTaxa;
	@FXML	private TextArea  inputEndereco;
	@FXML	private TextArea  inputDetalhesPedido;
	
	private Venda venda;
	private Delivery bean;
	private DeliveryService deliveryService = new DeliveryService();
	
	public void handleClienteSearch(ActionEvent evt){
		if(StringUtils.isNullOrEmpty(inputTelefone.getText())){
			addErrorMessage("msg.telefone_obrigatorio");
			return;
		}
		
		String telefone = TelefoneHandler.removeMasks(inputTelefone.getText());
		Cliente cliente = getDeliveryService().findByTelefone(telefone);
		
		if(cliente == null){
			addInfoMessage("msg.cliente_nao_encontrado");
			cliente = new Cliente();
			cliente.setTelefone( inputTelefone.getText() );
			inputNome.setText(null);
			inputEndereco.setText(null);
		}else{
			bean.setCliente(cliente);
			sendRegisterToForm(bean);
		}
	}
	
	public void handleSave(ActionEvent evt){
		try {
			populateBeanWithFormData(getBean());
			TelefoneHandler.validate(getBean().getCliente().getTelefone());
			deliveryService.getVendaService().finalizarVendaComDelivery(getBean());
			addInfoMessage("msg.sucess_insert");
			
			if(DialogFactory.showConfirmDialog(ResourceBundleService.getMessage("msg.imprime_cupom")) ){
				Task<Void> task = new Task<Void>() {
					
					@Override
					protected Void call() throws Exception {
						DarumaDualService daruma = DarumaDualService.getInstance();
						Cupom cupom = CupomService.criarCupom(getBean());
						
						getStageController().showLoadingModal(getApplication());
						daruma.imprimirCupom(cupom);
						getStageController().hideLoadingModal();
						return null;
					}
				};
				
				Thread t = new Thread(task);
				t.setDaemon(true);
				t.start();
			}
			
			getStageController().closeScreen(getApplication());
			VendaController vendaController = getStageController().getController(ApplicationType.VENDA);
			vendaController.clean();
		} catch (ValidationException e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			addErrorMessage("msg.error_default");
		}finally{
			getStageController().hideLoadingModal();
		}
	}
	
	public void handleCancel(ActionEvent evt){
		getStageController().closeScreen(getApplication());
	}

	@Override
	public Delivery populateBeanWithFormData(Delivery t) {
		if(t.getCliente() == null){
			t.setCliente(new Cliente());
		}
		
		t.getCliente().setTelefone(TelefoneHandler.removeMasks(inputTelefone.getText()));
		t.getCliente().setNome(inputNome.getText());
		t.getCliente().setEndereco(inputEndereco.getText());
		t.setTaxa(new BigDecimalMonetaryConverter().fromString(inputTaxa.getText()));
		t.setDetalhesPedido(inputDetalhesPedido.getText());

		return t;
	}

	@Override
	public void sendRegisterToForm(Delivery t) {
		if(t.getVenda() != null){
			inputTotal.setText(NumericConverter.formatCurrent(t.getVenda().getValorTotal()));
		}
		
		inputTelefone.setText(t.getCliente().getTelefone());
		inputNome.setText(t.getCliente().getNome());
		inputEndereco.setText(t.getCliente().getEndereco());
	}

	@Override
	public void cleanForm() {
		inputTotal.setText(null);
		inputTelefone.setText(null);
		inputNome.setText(null);
		inputEndereco.setText(null);
		inputTaxa.setText(null);
		inputDetalhesPedido.setText(null);
	}

	public TextField getInputTotal() {
		return inputTotal;
	}

	public void setInputTotal(TextField inputTotal) {
		this.inputTotal = inputTotal;
	}

	public TextField getInputNome() {
		return inputNome;
	}

	public void setInputNome(TextField inputNome) {
		this.inputNome = inputNome;
	}

	public TextField getInputTelefone() {
		return inputTelefone;
	}

	public void setInputTelefone(TextField inputTelefone) {
		this.inputTelefone = inputTelefone;
	}

	public TextArea getInputEndereco() {
		return inputEndereco;
	}

	public void setInputEndereco(TextArea inputEndereco) {
		this.inputEndereco = inputEndereco;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Delivery getBean() {
		return bean;
	}

	public void setBean(Delivery bean) {
		this.bean = bean;
	}

	public DeliveryService getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	@Override
	public void postInitialize() {
		venda = getParameter(Strings.VENDA.name());
		Delivery d = new Delivery();
		d.setVenda(venda);
		d.setCliente(new Cliente());
		setBean(d);
		
		sendRegisterToForm(getBean());
		
		MaskUtils.monetaryField(inputTaxa);
	}
}