package br.com.virilcorp.frentelite.ui.config;

import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.configuracoes.Balanca;
import br.com.virilcorp.frentelite.service.BalancaService;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BalancaController extends Controller {

	@FXML	private TextField inputSerialPort;
	@FXML	private TextField inputBaudrate;
	@FXML	private TextField inputDataBits;
	@FXML	private TextField inputStopBits;
	@FXML	private TextField inputParity;
	@FXML	private TextField inputAsci;
	@FXML	private TextField inputBytes;
	
	private BalancaService service = new BalancaService();
	private Balanca bean;
	
	public void handleSave(ActionEvent evt){
		try {
			fillBeanWithFormData();
			service.saveOrUpdate(bean);
			addInfoMessage("msg.sucess_insert");
		} catch (ValidationException e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			addErrorMessage("msg.error_default");
		}
	}
	
	public void handleCancel(ActionEvent evt){
		getStageController().closeScreen(getApplication());
	}
	
	private void fillBeanWithFormData(){
		bean.setSerialPort(inputSerialPort.getText());
		
		String baudrate = inputBaudrate.getText();
		if(!StringUtils.isNullOrEmpty(baudrate))
			bean.setBaudRate(Integer.valueOf(baudrate));
		
		String databits = inputDataBits.getText();
		if(!StringUtils.isNullOrEmpty(databits))
			bean.setDataBits(Integer.valueOf(databits));
		
		String parity = inputParity.getText();
		if(!StringUtils.isNullOrEmpty(parity))
			bean.setParity(Integer.valueOf(parity));
		
		String stopbits = inputStopBits.getText();
		if(!StringUtils.isNullOrEmpty(stopbits))
			bean.setStopBits(Integer.valueOf(stopbits));
		
		String bytes = inputBytes.getText();
		if(!StringUtils.isNullOrEmpty(bytes) )
			bean.setBytes(Integer.valueOf(bytes));

		String asci = inputAsci.getText();
		if(!StringUtils.isNullOrEmpty(asci))
			bean.setAsciCode(Integer.valueOf(asci));
	}
	
	private void sendBeanToForm(){
		inputSerialPort.setText(bean.getSerialPort());

		if(bean.getBaudRate() != null)
			inputBaudrate.setText(bean.getBaudRate().toString());
		if(bean.getDataBits() != null)
			inputDataBits.setText(bean.getDataBits().toString());
		if(bean.getParity() != null)
			inputParity.setText(bean.getParity().toString());
		if(bean.getStopBits() != null)
			inputStopBits.setText(bean.getStopBits().toString());
		if(bean.getBytes() != null)
			inputBytes.setText(bean.getBytes().toString());
		if(bean.getAsciCode() != null)
			inputAsci.setText(bean.getAsciCode().toString());
		
	}

	@Override
	public void postInitialize() {
		bean = service.loadBalancaConfig();
		sendBeanToForm();
		
		MaskUtils.maxField(inputSerialPort, 20);
		MaskUtils.numericField(inputBaudrate, inputDataBits, inputStopBits, inputParity);
	}
}
