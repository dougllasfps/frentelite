package br.com.virilcorp.frentelite.ui.venda;

import java.util.List;

import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.VendaService;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.util.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DetalhesVendaController extends Controller{
	
	private VendaService vendaService = new VendaService();
	
	@FXML private TextField txtData;
	@FXML private TextField txtCodigo;
	@FXML private TextField txtTotal;
	
	@FXML private TableView<ItemVendaModel> dataTable;
	@FXML private TableColumn<ItemVendaModel, String> quantidadeColumn;
	@FXML private TableColumn<ItemVendaModel, String> produtoColumn;
	@FXML private TableColumn<ItemVendaModel, String> precoVendaColumn;
	@FXML private TableColumn<ItemVendaModel, String> totalColumn;

	private void applyTableColumnsConfig() {
		quantidadeColumn.setCellValueFactory( x -> x.getValue().getQtd() );
		produtoColumn.setCellValueFactory( x -> x.getValue().getDescProduto() );
		precoVendaColumn.setCellValueFactory( x -> x.getValue().getValoUnitario() );
		totalColumn.setCellValueFactory( x -> x.getValue().getValorTotal() );
	}

	@Override
	public void postInitialize() {
		applyTableColumnsConfig();

		Venda venda = (Venda) getParameters().get(Strings.VENDA.name());
		
		if(venda == null){
			throw new RuntimeException();
		}
		
		List<ItemVenda> itens = vendaService.findItensVenda(venda);
		venda.setItensVenda(itens);
		
		dataTable.setItems(venda.getCarrinho().getObservableList());
		
		txtData.setText(DateTimeUtils.format(venda.getDataVenda()));
		txtTotal.setText(NumericConverter.formatCurrent(venda.getValorTotal()));
		txtCodigo.setText( String.valueOf(venda.getId()) );
	}
}