package br.com.virilcorp.frentelite.ui.config;

import br.com.virilcorp.frentelite.model.ImpressoraConfig;
import br.com.virilcorp.frentelite.service.ConfiguracaoImpressoraService;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.FormDataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConfiguracaoImpressoraController extends Controller implements FormDataHandler<ImpressoraConfig>{

	@FXML	private TextField txtPorta;
	@FXML	private TextField txtVelocidade;
	
	private ImpressoraConfig bean;
	
	private ConfiguracaoImpressoraService service = new ConfiguracaoImpressoraService();
	
	public void handleSave(ActionEvent evt){
		try {
			populateBeanWithFormData(getBean());
			if(getBean().getId() == null){
				service.save(getBean());
			}else{
				service.update(getBean());
			}
			addInfoMessage("msg.sucess_insert");
		} catch (Exception e) {
			addErrorMessage("msg.error_default");
		}
	}

	public void handleCancel(ActionEvent evt){
		getStageController().closeScreen(getApplication());
	}
	
	@Override
	public ImpressoraConfig populateBeanWithFormData(ImpressoraConfig t) {
		t.setPorta(txtPorta.getText());
		t.setVelocidade(txtVelocidade.getText());
		return t;
	}

	@Override
	public void sendRegisterToForm(ImpressoraConfig t) {
		if(t.getPorta() != null)
			txtPorta.setText(t.getPorta());
		
		if(t.getVelocidade() != null)
			txtVelocidade.setText(t.getVelocidade());
	}

	@Override
	public void cleanForm() {}

	public ImpressoraConfig getBean() {
		return bean;
	}

	public void setBean(ImpressoraConfig bean) {
		this.bean = bean;
	}

	@Override
	public void postInitialize() {
		bean = service.findFirst();
		
		if(bean == null){
			setBean(new ImpressoraConfig());
		}
		
		sendRegisterToForm(getBean());
	}
}