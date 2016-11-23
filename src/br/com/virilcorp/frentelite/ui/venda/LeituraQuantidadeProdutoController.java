/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.balanca.BalancaHandler;
import br.com.virilcorp.frentelite.balanca.exception.BalancaInativaException;
import br.com.virilcorp.frentelite.balanca.exception.PesoInstavelException;
import br.com.virilcorp.frentelite.balanca.exception.PesoNegativoException;
import br.com.virilcorp.frentelite.balanca.exception.SobreCargaPesoException;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.model.Tara;
import br.com.virilcorp.frentelite.service.DialogFactory;
import br.com.virilcorp.frentelite.service.TaraService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.LeitorQuantidade;
import br.com.virilcorp.frentelite.ui.LeituraPeso;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import jssc.SerialPortException;

/**
 *
 * @author dougllas.sousa
 */
public class LeituraQuantidadeProdutoController extends Controller implements Initializable {
	
	@FXML private TextField inputQuantidade;
	@FXML private Label statusBalanca;
	@FXML private TextField txtProduto;
	@FXML private FlowPane tarasPanel;
	@FXML private TitledPane tarasTitledPanel;
	@FXML private Button btnTara;
	
	private List<Tara> taras;
	private List<Tara> tarasAdicionadas;
	private BalancaHandler balancaHandler;
	private boolean balancaConectada = true;
	private TaraService taraService = new TaraService();
	private Produto produto;
	private LeitorQuantidade leitorQuantidade;
	
	public void postInitialize(){
		MaskUtils.weightField(inputQuantidade);
		
		produto = getParameter("produto");
		
		if(produto == null){
			addErrorMessage("msg.produto_nao_encontrado");
			return;
		}
		
		txtProduto.setText(produto.getDescricao());
		
		if(!produto.needsWeightReading() || !balancaConectada){
			aplicarConfiguracoesProdutosSemPeso();
			return;
		}
		
		fazerLeituraBalanca();
		listarTaras();
		setLeitorQuantidade(new LeituraPeso());
		
		tarasPanel.setVgap(10);
		tarasPanel.setHgap(10);
		tarasPanel.setPadding(new Insets(10));
	}

	private void fazerLeituraBalanca() {
		Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				try {
					balancaHandler = BalancaHandler.getInstance();
					BigDecimal peso = balancaHandler.fazerLeituraPeso();
					LeituraPeso leituraPeso = (LeituraPeso) getLeitorQuantidade();
					leituraPeso.setLeitura(peso);
					String pesoString = NumericConverter.formatWeight(leituraPeso.getLeitura());
					inputQuantidade.setText(pesoString);
					
				} catch (SerialPortException | PesoInstavelException | PesoNegativoException | SobreCargaPesoException| BalancaInativaException e) {
					statusBalanca.setText(getBundleMessage("msg.balanca_inativa"));
					balancaConectada = false;
				}
				return null;
			}
		};
		
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();

		if(!balancaConectada){
			return;
		}
	}
	
	private void listarTaras() {
		if(this.taras == null)
			this.taras = taraService.getDao().findAll();
	}

	public List<Tara> getTaras() {
		return taras;
	}
	
	public void setTaras(List<Tara> taras) {
		this.taras = taras;
	}
	
	public void handleAdicionar(ActionEvent evt){
		BigDecimal quantidade = getQuantidadeFromInput();
		VendaController controller = (VendaController) getStageController().getController(ApplicationType.VENDA);
		controller.setProdutoAdicionar(produto);
		controller.addProduto(quantidade);
		getStageController().closeScreen(getApplication());
	}
	
	public void handleAdicionarTara(ActionEvent evt){
		showTaraDialog();
	}

	private BigDecimal getQuantidadeFromInput() {
		String text = inputQuantidade.getText();
		if(text == null || StringUtils.isNullOrEmpty(text))
			inputQuantidade.setText("0,000");
		
		return NumericConverter.convertBigDecimal( inputQuantidade.getText(), true );
	}
	
	private void sendQuantidadeToInput(BigDecimal newQuantidade){
		inputQuantidade.setText(NumericConverter.formatWeight(newQuantidade));
	}
	
	public void handleCancelar(ActionEvent evt){
		getStageController().closeScreen(this.getApplication());
	}
	
	private void aplicarConfiguracoesProdutosSemPeso(){
		statusBalanca.setVisible(false);
		btnTara.setDisable(true);
		tarasTitledPanel.setDisable(true);
		tarasTitledPanel.setCollapsible(false);
	}
	
	public boolean adicionarTara(Tara tara){
		if(!validarTaras(tara)){
			addErrorMessage("msg.taras_maior_produto");
			return false;
		}
		
		if(tarasAdicionadas == null)
			tarasAdicionadas = new ArrayList<>();
		
		tarasAdicionadas.add(tara);
		calcularEInformarPesoComTaras(null);
		
		LeituraPeso leituraPeso = (LeituraPeso) getLeitorQuantidade();
		leituraPeso.addTara(tara);
		
		tarasPanel.getChildren().add(new Label(tara.getDescricao()));
		
		inputQuantidade.requestFocus();
		
		return true;
	}
	
	public void removerTara(Tara tara){
		tarasAdicionadas.remove(tara);
		taras.add(tara);
		calcularEInformarPesoComTaras(tara.getPeso());
	}
	
	private boolean validarTaras(Tara tara){
		BigDecimal totalTaras = getTotalTaras().add(tara.getPeso());
		BigDecimal quantidadeFromInput = getQuantidadeFromInput();
		
		return totalTaras.compareTo(quantidadeFromInput) == -1;
	}
	
	private void calcularEInformarPesoComTaras(BigDecimal pesoDevolver){
		if(taras == null)
			return;
		
		BigDecimal total = getTotalTaras();
		
		BigDecimal quantidadeFromInput = getQuantidadeFromInput();
		
		BigDecimal newQuantidade = pesoDevolver == null ? quantidadeFromInput.subtract(total) : quantidadeFromInput.add(pesoDevolver);
		sendQuantidadeToInput(newQuantidade);
	}

	private BigDecimal getTotalTaras() {
		BigDecimal total = BigDecimal.ZERO;
		
		if(tarasAdicionadas == null){
			return total;
		}
		
		for (Tara t : tarasAdicionadas){
			total = total.add(t.getPeso());
		}
		return total;
	}

	private void showTaraDialog(){
		if(this.taras == null || this.taras.isEmpty()){
			addErrorMessage("msg.taras_vazias");
			return;
		}
		
		ObservableList<Tara> list = FXCollections.observableArrayList(this.taras);
		ChoiceDialog<Tara> dialog = DialogFactory.createChoiceDialog(list, "Tara", "Adicionar Tara", "Selecione uma tara");
		Optional<Tara> result = dialog.showAndWait();
		
		if(result.isPresent()){
			Tara tara = result.get();
			adicionarTara(tara);
		}
	}
	
	public BalancaHandler getBalancaHandler() {
		return balancaHandler;
	}

	public void setBalancaHandler(BalancaHandler balancaHandler) {
		this.balancaHandler = balancaHandler;
	}

	public List<Tara> getTarasAdicionadas() {
		return tarasAdicionadas;
	}

	public void setTarasAdicionadas(List<Tara> tarasAdicionadas) {
		this.tarasAdicionadas = tarasAdicionadas;
	}

	public LeitorQuantidade getLeitorQuantidade() {
		return leitorQuantidade;
	}

	public void setLeitorQuantidade(LeitorQuantidade leitorQuantidade) {
		this.leitorQuantidade = leitorQuantidade;
	}
}