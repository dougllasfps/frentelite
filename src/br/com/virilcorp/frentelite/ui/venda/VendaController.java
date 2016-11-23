package br.com.virilcorp.frentelite.ui.venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.ButtonAdapterService;
import br.com.virilcorp.frentelite.service.CategoriaService;
import br.com.virilcorp.frentelite.service.DialogFactory;
import br.com.virilcorp.frentelite.service.ProdutoService;
import br.com.virilcorp.frentelite.service.ResourceBundleService;
import br.com.virilcorp.frentelite.ui.ApplicationFactory.ApplicationType;
import br.com.virilcorp.frentelite.ui.Controller;
import br.com.virilcorp.frentelite.ui.component.CategoriaButton;
import br.com.virilcorp.frentelite.ui.component.ProdutoButton;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import br.com.virilcorp.frentelite.util.ComponentUtils;
import br.com.virilcorp.frentelite.util.Strings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;

public class VendaController extends Controller {
	
	private Venda venda;
	private Usuario usuarioLogado;
	private Produto produtoBusca;
	private Categoria categoriaBusca;
	private Produto produtoAdicionar;

	private List<Produto> produtos;
	private List<Categoria> categorias;
	
	private CategoriaService categoriaService;
	private ProdutoService produtoService;
	
	private Button btnTodosProdutos;
	private Button btnTodasCategorias;
	
	@FXML private FlowPane 	produtosPanel;
	@FXML private FlowPane	categoriasPanel;
	@FXML private TitledPane itensPane;
	@FXML private TitledPane categoriasPane;
	@FXML private TitledPane produtosPane;
	@FXML private TextField 	txtTotalVenda;
	@FXML private TextField 	txtCodigoProduto;
	@FXML private TextField 	txtDescricao;
	@FXML private Label 		lblProdutoAdicionado;
	@FXML private Button btnBuscaMaisVendidos;
	
	@FXML private TableView<ItemVendaModel> dataTable;
	@FXML private TableColumn<ItemVendaModel, String> quantidadeColumn;
	@FXML private TableColumn<ItemVendaModel, String> produtoColumn;
	@FXML private TableColumn<ItemVendaModel, String> precoVendaColumn;
	@FXML private TableColumn<ItemVendaModel, String> totalColumn;
	
	private static final Integer MAX_DISPLAYED_PRODUCTS = 12;	
	
	@Override
	public void postInitialize(){
		this.categoriaService = new CategoriaService();
		this.produtoService = new ProdutoService();
		this.venda = new Venda();
		
		inicializaCategoriasEProdutos();
		inicializaComponents();
	}
	
	private void inicializaCategoriasEProdutos(){
		List<Categoria> categorias = categoriaService.findCategoriasComProdutos();
		List<Produto> produtos = produtoService.findProdutosMaisVendidos(MAX_DISPLAYED_PRODUCTS);

		setProdutos(produtos);
		setCategorias(categorias);

		loadCategoriasPanel();
		loadProdutosPanel();
	}
	
	private void inicializaComponents(){
		this.txtCodigoProduto.requestFocus();
		this.lblProdutoAdicionado.setText(ResourceBundleService.getMessage("label.caixa_livre"));
		
		String labelRemover = ResourceBundleService.getMessage("label.remover");
		
		EventHandler<ActionEvent> removeItemEvent = e -> {
			ItemVendaModel selectedItem = dataTable.getSelectionModel().getSelectedItem();
			if(selectedItem != null){
				ItemVenda itemVenda = selectedItem.getItemVenda();
				removeItem(itemVenda);
			}
		};
		
		ComponentUtils.createContextMenu( dataTable, removeItemEvent, labelRemover );
		
		applyPanelsConfig();
		applyTableColumnsConfig();
		
		MaskUtils.numericField(txtCodigoProduto);
		MaskUtils.maxField(txtCodigoProduto, 20);
	}

	private void applyTableColumnsConfig() {
		quantidadeColumn.setCellValueFactory( x -> x.getValue().getQtd() );
		produtoColumn.setCellValueFactory( x -> x.getValue().getDescProduto() );
		precoVendaColumn.setCellValueFactory( x -> x.getValue().getValoUnitario() );
		totalColumn.setCellValueFactory( x -> x.getValue().getValorTotal() );
	}

	private void applyPanelsConfig() {
		itensPane.setCollapsible(false);
		categoriasPane.setCollapsible(false);
		produtosPane.setCollapsible(false);
		
		Insets panelsPadding = new Insets(10, 10, 10, 10);
		int vGap = 15 , Hgap = 15; 
		
		getProdutosPanel().setPadding(panelsPadding);
		getProdutosPanel().setVgap(vGap);
		getProdutosPanel().setHgap(Hgap);
		
		getCategoriasPanel().setPadding(panelsPadding);
		getCategoriasPanel().setVgap(vGap);
		getCategoriasPanel().setHgap(Hgap);
	}

	private void loadProdutosPanel(){
		List<ProdutoButton> produtosButtons = new ArrayList<ProdutoButton>();
		
		getProdutos().stream().forEach( prod -> {
			if(prod.getButton() == null){
				createProductButton(produtosButtons, prod);		
			}else{
				produtosButtons.add(prod.getButton());
			}
		});
		
		getProdutosPanel().getChildren().clear();
		getProdutosPanel().getChildren().add(getBtnTodosProdutos());
		getProdutosPanel().getChildren().addAll(produtosButtons);
	}
	
	public Button getBtnTodasCategorias() {
		if(btnTodasCategorias == null){
			btnTodasCategorias = ButtonAdapterService.createCustomButton("Todas", 100, 100, evt -> {
				List<Categoria> cats = categoriaService.findCategoriasComProdutos();
				setCategorias(cats);
				loadCategoriasPanel();
			} );
		}
		return btnTodasCategorias;
	}
	
	public Button getBtnTodosProdutos() {
		if(btnTodosProdutos == null){
			btnTodosProdutos = ButtonAdapterService.createCustomButton("Mais Vendidos", 120, 120, evt -> {
				List<Produto> list = produtoService.findProdutosMaisVendidos(MAX_DISPLAYED_PRODUCTS);
				setProdutos(list);
				loadProdutosPanel();
			} );
		}
		return btnTodosProdutos;
	}

	private void createProductButton(List<ProdutoButton> produtosButtons, Produto prod) {
		try {
			EventHandler<ActionEvent> onProdutoClick = evt ->  {
				this.produtoAdicionar = prod;
				prepareAdicionarProduto();
			};
			ProdutoButton produtosButton = ButtonAdapterService.createProdutoButton( prod, 120, 120, onProdutoClick );
			produtosButtons.add(produtosButton);
			prod.setButton(produtosButton);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadCategoriasPanel(){
		List<CategoriaButton> categoriasButtons = new ArrayList<CategoriaButton>();
		
		getCategorias().forEach(cat-> {
			if(cat.getButton() == null){
				createCatButton( categoriasButtons, cat );
			}
		});
		
		getCategoriasPanel().getChildren().clear();
		getCategoriasPanel().getChildren().addAll(categoriasButtons);
	}

	private void createCatButton(List<CategoriaButton> categoriasButtons, Categoria cat) {
		try {
			EventHandler<ActionEvent> onCatClick = evt -> { 
				categoriaService.carregaProdutosCategoria(cat); 
				setProdutos(cat.getProdutos());
				loadProdutosPanel();
			};
			CategoriaButton categoriaButton = ButtonAdapterService.createCategoriaButton( cat, 110, 50, onCatClick);
			categoriasButtons.add(categoriaButton);
			cat.setButton(categoriaButton);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void prepareAdicionarProduto(){
		addParameter("produto", this.produtoAdicionar);
		ApplicationType inputQuantidadeModal = ApplicationType.INPUT_QUANTIDADE_MODAL;
		getStageController().openScreenInANewWindow(inputQuantidadeModal, getParameters());
	}
	
	public void addProduto(BigDecimal quantidade){
		getVenda().getCarrinho().add( getProdutoAdicionar(), quantidade );
		getDataTable().setItems(getVenda().getCarrinho().getObservableList());
		String totalString = NumericConverter.formatCurrent(getVenda().getValorTotal());
		txtTotalVenda.setText(totalString);
		lblProdutoAdicionado.setText(getProdutoAdicionar().getDescricao());
	}
	
	private void removeItem(ItemVenda itemVenda){
		boolean success = getVenda().getCarrinho().remove(itemVenda);
		
		if(success){
			getDataTable().setItems(getVenda().getCarrinho().getObservableList());
			txtTotalVenda.setText(NumericConverter.formatCurrent(getVenda().getCarrinho().getTotal()));
		}
	}
	
	public void handleBuscaProduto(ActionEvent evt){
		Object component = evt.getSource();
		
		if(component.equals(txtDescricao)){
			this.produtos = produtoService.findByDescricaoLike(txtDescricao.getText());
		}

		if(this.produtos.isEmpty()){
			addInfoMessage("msg.produto_nao_encontrado");
			return;
		}
		
		loadProdutosPanel();
	}
	
	public void handleAdicionarProdutoPorCodigo(ActionEvent evt){
		String text = txtCodigoProduto.getText();
		Produto produtoEncontrado = produtoService.findByCodigo(text);
		
		if(produtoEncontrado == null){
			addInfoMessage("msg.produto_nao_encontrado");
			return;
		}
		
		setProdutoAdicionar(produtoEncontrado);
		prepareAdicionarProduto();
	}
	
	public void handlePagamento(ActionEvent evt){
		if(venda.getCarrinho().isEmpty()){
			addErrorMessage("msg.venda_sem_items");
			return;
		}
		
		addParameter(Strings.VENDA.name(), getVenda());
		getStageController().openScreenInANewWindow(ApplicationType.PAGAMENTO, getParameters(), getStage());
	}
	
	public void handleDelivery(ActionEvent evt){
		if(venda.getCarrinho().isEmpty()){
			addErrorMessage("msg.venda_sem_items");
			return;
		}
		
		addParameter(Strings.VENDA.name(), getVenda());
		getStageController().openScreenInANewWindow(ApplicationType.DELIVERY, getParameters(), getStage());
	}
	
	public void handleCancelamento(ActionEvent evt){
		if( DialogFactory.showCancelConfirmDialog() ){
			clean();
		}
	}
	
	public void clean(){
		this.venda = new Venda();
		this.dataTable.setItems(null);
		this.lblProdutoAdicionado.setText("Caixa Livre");
		this.txtCodigoProduto.setText(null);
		this.txtTotalVenda.setText("0,00");
		this.inicializaCategoriasEProdutos();
	}
	
	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Produto getProdutoBusca() {
		return produtoBusca;
	}

	public void setProdutoBusca(Produto produtoBusca) {
		this.produtoBusca = produtoBusca;
	}

	public Categoria getCategoriaBusca() {
		return categoriaBusca;
	}

	public void setCategoriaBusca(Categoria categoriaBusca) {
		this.categoriaBusca = categoriaBusca;
	}

	public Produto getProdutoAdicionar() {
		return produtoAdicionar;
	}

	public void setProdutoAdicionar(Produto produtoAdicionar) {
		this.produtoAdicionar = produtoAdicionar;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}

	public ProdutoService getProdutoService() {
		return produtoService;
	}

	public FlowPane getProdutosPanel() {
		return produtosPanel;
	}

	public FlowPane getCategoriasPanel() {
		return categoriasPanel;
	}

	public TableView<ItemVendaModel> getDataTable() {
		return dataTable;
	}
}