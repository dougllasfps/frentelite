/*
o * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;
import java.util.List;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.relatorio.cupom.Cupom;
import br.com.virilcorp.daruma.relatorio.cupom.CupomService;
import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.CarrinhoVenda;
import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Pagamento;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.DialogFactory;
import br.com.virilcorp.frentelite.service.ItemVendaService;
import br.com.virilcorp.frentelite.service.ResourceBundleService;
import br.com.virilcorp.frentelite.service.VendaService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.ComponentUtils;
import br.com.virilcorp.frentelite.util.StringUtils;
import br.com.virilcorp.frentelite.util.Strings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * @author DOUGLLASFPS
 */
public class PagamentoController extends Controller implements Initializable{

	@FXML private TextField txtTotal;
	@FXML private TextField txtValorAdicionar;
	@FXML private TextField txtTotalPago;
	@FXML private TextField txtDesconto;
	@FXML private TextField txtValorPagar;
	@FXML private TextField txtTroco;
	
	@FXML private TableView<ItemVendaModel> dataTable;
	@FXML private TableColumn<ItemVendaModel, String> quantidadeColumn;
	@FXML private TableColumn<ItemVendaModel, String> produtoColumn;
	@FXML private TableColumn<ItemVendaModel, String> precoVendaColumn;
	@FXML private TableColumn<ItemVendaModel, String> totalColumn;
	
	@FXML private TableView<PagamentoModel> dataTablePagamentos;
	@FXML private TableColumn<PagamentoModel, String> formaPagamentoColumn;
	@FXML private TableColumn<PagamentoModel, String> valorPagamentoColumn;
	
	private TipoPagamento tipoSelecionado;
	private Venda venda;
	private VendaService vendaService;
	private ResumoPagamento resumoPagamento;
	private DarumaDualService daruma;
	
	@Override
	public void postInitialize(){
		vendaService = new VendaService();
		try {
			daruma = DarumaDualService.getInstance();
		} catch (Exception e) {
			addInfoMessage("Impressora Não conectada ou inativa.");
			e.printStackTrace();
		}
		inicializaComponentes();
		inicializaDetalhesVenda();
	}
	
	private void applyTableColumnsConfig() {
		
		quantidadeColumn.setCellValueFactory( x -> x.getValue().getQtd() );
		produtoColumn.setCellValueFactory( x -> x.getValue().getDescProduto() );
		precoVendaColumn.setCellValueFactory( x -> x.getValue().getValoUnitario() );
		totalColumn.setCellValueFactory( x -> x.getValue().getValorTotal() );
		
		formaPagamentoColumn.setCellValueFactory( x -> x.getValue().getFormaPagamento() );
		valorPagamentoColumn.setCellValueFactory( x -> x.getValue().getValorPagamento() );
		
	}
	
	public void handleAdicionarPagamentoDinheiro(ActionEvent evt){
		setTipoSelecionado(TipoPagamento.DINHEIRO);
		handleAdicionarFormaPagamento(evt);
	}
	
	public void handleAdicionarPagamentoCartao(ActionEvent evt){
		setTipoSelecionado(TipoPagamento.CARTAO);
		handleAdicionarFormaPagamento(evt);
	}
	
	public void handleAdicionarPagamentoCartaoDebito(ActionEvent evt){
		setTipoSelecionado(TipoPagamento.DEBITO);
		handleAdicionarFormaPagamento(evt);
	}
	
	private void handleAdicionarFormaPagamento(ActionEvent evt){
		String valorInformado = txtValorAdicionar.getText();
		BigDecimal valor = NumericConverter.convertBigDecimal(valorInformado);
		
		if(valor.compareTo(BigDecimal.ZERO) == 0){
			return;
		}
		
		PagamentoModel formaPagamento = new PagamentoModel(tipoSelecionado, valor);
		dataTablePagamentos.getItems().add(formaPagamento);
		resumoPagamento.addValorPagamento(tipoSelecionado, valor);
		
		updateResumoPagamento();
	}

	private void updateResumoPagamento() {
		
		BigDecimal valorPagar = resumoPagamento.getValorPagar();
		BigDecimal desconto = resumoPagamento.getDesconto();
		
		valorPagar = valorPagar.subtract(desconto);
		
		if(valorPagar.compareTo(BigDecimal.ZERO) < 0){
			valorPagar = BigDecimal.ZERO;
		}
		
		txtValorPagar.setText(NumericConverter.formatCurrent(valorPagar));
		txtValorAdicionar.setText(NumericConverter.formatCurrent(valorPagar));
		txtTotalPago.setText(NumericConverter.formatCurrent(resumoPagamento.getTotalPago()));

		BigDecimal troco = resumoPagamento.calcularTroco();
		txtTroco.setText(NumericConverter.formatCurrent(troco));
	}
	
	public void handleFinalizarPagamento(ActionEvent evt){
		try {
			
			String desconto = txtDesconto.getText();
			
			if(desconto != null && !"".equals(desconto.trim()) ){
				BigDecimal valorDesconto = NumericConverter.convertBigDecimal(desconto);
				resumoPagamento.setDesconto(valorDesconto);
			}
			
			BigDecimal totalCartao = resumoPagamento.getTotalPagoPorTipo(TipoPagamento.CARTAO);
			BigDecimal totalDinheiro = resumoPagamento.getTotalPagoPorTipo(TipoPagamento.DINHEIRO);
			BigDecimal totalDebito = resumoPagamento.getTotalPagoPorTipo(TipoPagamento.DEBITO);
			
			venda.getPagamento().setValorDinheiro(totalDinheiro);
			venda.getPagamento().setValorCartao(totalCartao);
			venda.getPagamento().setValorDebito(totalDebito);
			venda.getPagamento().setValorDesconto(resumoPagamento.getDesconto());
			venda.setValorDesconto(resumoPagamento.getDesconto());
			
			vendaService.validarEConfigurarPagamento(venda);
			vendaService.finalizarVenda(venda);
			
			getStageController().closeScreen(getApplication());

			VendaController vendaController = getStageController().getController(ApplicationType.VENDA);
			if(vendaController != null){
				vendaController.clean();
			}
			
			addInfoMessage("msg.venda_sucesso");
			
			if(DialogFactory.showConfirmDialog(ResourceBundleService.getMessage("msg.imprime_cupom")) ){
				
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						daruma = DarumaDualService.getInstance();
						Cupom cupom = CupomService.criarCupom(getVenda());
						daruma.imprimirCupom(cupom);
						return null;
					}
				};
				
				Thread t = new Thread(task);
				t.setDaemon(true);
				t.start(); 
			}
			
		} catch (ValidationException e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}
	
	public void handleCancelarPagamento(ActionEvent evt){
		if( DialogFactory.showCancelConfirmDialog() ){
			getStageController().closeScreen(getApplication());
		}
	}
	
	private void inicializaComponentes() {
		applyTableColumnsConfig();
		txtDesconto.focusedProperty().addListener(new ChangeValuesListener());
		
		EventHandler<ActionEvent> removerPagamento = evt -> {
			PagamentoModel selectedItem = dataTablePagamentos.getSelectionModel().getSelectedItem();
			
			if(selectedItem != null){
				dataTablePagamentos.getItems().remove(selectedItem);
				resumoPagamento.removerValorPagamento(selectedItem.getTipo(), selectedItem.getValor());
				updateResumoPagamento();
			}
			
		};
		
		ComponentUtils.createContextMenu(dataTablePagamentos, removerPagamento, "Remover");
		
		MaskUtils.monetaryField(txtTotal, txtValorAdicionar, txtDesconto);
		
		final String zero = "0,00";
		txtDesconto.setText(zero);
		txtTotalPago.setText(zero);
		txtTroco.setText(zero);
	}

	private void inicializaDetalhesVenda() {
		Venda vendaParam = getParameter(Strings.VENDA.name());
		
		if(vendaParam == null){
			throw new IllegalStateException();
		}

		if(vendaParam.getId() != null){
			ItemVendaService itemVendaService = new ItemVendaService();
			List<ItemVenda> itens = itemVendaService.findByVenda(vendaParam);
			vendaParam.setItensVenda(itens);
		}
		
		this.venda = vendaParam;
		this.venda.setPagamento(new Pagamento());
		
		if(vendaParam.getCarrinho() == null || vendaParam.getCarrinho().isEmpty()){
			vendaParam.setCarrinho(new CarrinhoVenda());
			vendaParam.getCarrinho().addAll(vendaParam.getItensVenda());
		}
		
		String subTotal = NumericConverter.formatCurrent(vendaParam.getCarrinho().getTotal());
		
		txtTotal.setText(subTotal);
		txtValorAdicionar.setText(subTotal);
		txtValorPagar.setText(subTotal);
		
		dataTable.setItems(this.venda.getCarrinho().getObservableList());
		resumoPagamento = new ResumoPagamento(vendaParam.getCarrinho().getTotal());
	}

	public TipoPagamento getTipoSelecionado() {
		return tipoSelecionado;
	}

	public void setTipoSelecionado(TipoPagamento tipoSelecionado) {
		this.tipoSelecionado = tipoSelecionado;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public VendaService getVendaService() {
		return vendaService;
	}

	public void setVendaService(VendaService vendaService) {
		this.vendaService = vendaService;
	}
	
	private class ChangeValuesListener implements ChangeListener<Boolean>{
		 @Override
	     public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			 String desconto = txtDesconto.getText();
			 
			 boolean nullOrEmpty = StringUtils.isNullOrEmpty(desconto);
			
			 if(nullOrEmpty){
				 return;
			 }
			 
			 BigDecimal descontoDigitado = NumericConverter.convertBigDecimal(desconto);
			 resumoPagamento.setDesconto(descontoDigitado);
			 updateResumoPagamento();
			 
		 }
	}
}