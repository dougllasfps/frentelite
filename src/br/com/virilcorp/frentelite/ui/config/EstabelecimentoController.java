package br.com.virilcorp.frentelite.ui.config;

import br.com.virilcorp.frentelite.model.Estabelecimento;
import br.com.virilcorp.frentelite.service.EstabelecimentoService;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.FormDataHandler;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EstabelecimentoController extends Controller implements FormDataHandler<Estabelecimento>{

	@FXML	private TextField txtRazao;
	@FXML	private TextField txtEndereco;
	@FXML	private TextField txtCidade;
	@FXML	private TextField txtTelefone;
	@FXML	private TextField txtCnpj;
	
	private Estabelecimento bean;
	private EstabelecimentoService service;
	
	@Override
	public void postInitialize() {
		service = new EstabelecimentoService();
		bean = service.findFirst();
		
		if(bean == null){
			setBean(new Estabelecimento());
		}
		
		sendRegisterToForm(getBean()); 
		
		MaskUtils.upperCase(txtCidade, txtEndereco, txtRazao);
	}

	@Override
	public Estabelecimento populateBeanWithFormData(Estabelecimento t) {
		t.setCidade(txtCidade.getText());
		t.setCnpj(txtCnpj.getText());
		t.setEndereco(txtEndereco.getText());
		t.setRazaoSocial(txtRazao.getText());
		t.setTelefone(txtTelefone.getText());
		return t;
	}

	@Override
	public void sendRegisterToForm(Estabelecimento t) {
		if(t.getCidade() != null)
			txtCidade.setText(t.getCidade());
		
		if(t.getCnpj() != null)
			txtCnpj.setText(t.getCnpj());
		
		if(t.getEndereco() != null)
			txtEndereco.setText(t.getEndereco());
		
		if(t.getRazaoSocial() != null)
			txtRazao.setText(t.getRazaoSocial());
		
		if(t.getTelefone() != null)
			txtTelefone.setText(t.getTelefone());
	}
	
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
	public void cleanForm() {}

	public Estabelecimento getBean() {
		return bean;
	}
	
	public void setBean(Estabelecimento bean) {
		this.bean = bean;
	}
}