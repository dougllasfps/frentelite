/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui.produto;

import java.io.File;
import java.io.IOException;

import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.frentelite.exception.ConverterException;
import br.com.virilcorp.frentelite.exception.FotoGrandeException;
import br.com.virilcorp.frentelite.model.Categoria;
import br.com.virilcorp.frentelite.model.Foto;
import br.com.virilcorp.frentelite.model.Medida;
import br.com.virilcorp.frentelite.model.Produto;
import br.com.virilcorp.frentelite.service.CategoriaService;
import br.com.virilcorp.frentelite.service.FileService;
import br.com.virilcorp.frentelite.service.FotoService;
import br.com.virilcorp.frentelite.service.ProdutoService;
import br.com.virilcorp.frentelite.ui.CrudController;
import br.com.virilcorp.frentelite.ui.util.ImageViewHandler;
import br.com.virilcorp.frentelite.ui.util.MaskUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author DOUGLLASFPS
 */
public class ProdutoController extends CrudController<Produto, ProdutoModel, ProdutoService> implements Initializable{

    @FXML   private TextField txtCodigo;
    @FXML   private TextField txtDescricao;
    @FXML   private TextField txtValorCusto;
    @FXML   private TextField txtValorVenda;
    
    @FXML   private ComboBox<Medida> comboMedida;
    @FXML 	private ComboBox<Categoria> comboCategorias;
    @FXML	private ImageView imageProdutoView;
    @FXML	private Label lblFotoStatus;
    
    @FXML   private TableColumn<ProdutoModel, String> codigoColumn;
    @FXML   private TableColumn<ProdutoModel, String> descricaoColumn;
    @FXML 	private TableColumn<ProdutoModel, String> valorCustoColumn;
    @FXML 	private TableColumn<ProdutoModel, String> valorVendaColumn;
    @FXML 	private TableColumn<ProdutoModel, String> unidadeMedidaColumn;
    @FXML 	private TableColumn<ProdutoModel, String> categoriaColumn;
    
    private CategoriaService catService = new CategoriaService();
    
    public void carregarFoto(MouseEvent evt){
    	File file = FileService.openFileChooser( getStage() );
    	if(file == null){	return;  }
    	
    	try {
			Foto foto = FotoService.createFoto(file);
			getBean().setFoto(foto);
			
        	Image image = ImageViewHandler.extractImage(getBean().getFoto());
        	imageProdutoView.setImage(image);
        	
        	updateFotoLabelStatus();
        	
    	} catch (IOException e1) {
			addErrorMessage("Ocorreu um erro: " + e1.getMessage());
		} catch (FotoGrandeException e) {
			addErrorMessage(e.getMessage());
		}
    }
    
    private void executeColumnsConfigurations(){
        codigoColumn.setCellValueFactory( cell -> cell.getValue().getCodigo());
        descricaoColumn.setCellValueFactory( cell -> cell.getValue().getDescricao() );
        valorCustoColumn.setCellValueFactory( cell -> cell.getValue().getValorCusto() );
        valorVendaColumn.setCellValueFactory( cell -> cell.getValue().getValorVenda() );
        categoriaColumn.setCellValueFactory(cell -> cell.getValue().getCategoria() );
        unidadeMedidaColumn.setCellValueFactory( cell -> cell.getValue().getUnidadeMedida() );
        
        codigoColumn.setSortable(false);
        descricaoColumn.setSortable(false);
        valorCustoColumn.setSortable(false);
        valorVendaColumn.setSortable(false);
        categoriaColumn.setSortable(false);
        unidadeMedidaColumn.setSortable(false);
    }
    
    @Override
    public void prepareInsert() {
    	super.prepareInsert();
    	txtCodigo.setDisable(false);
    	disableOnlyInsertFields(false);
    }
    
    @Override
    protected void prepareEdit() {
    	super.prepareEdit();
    	txtCodigo.setDisable(true);
    	disableOnlyInsertFields(false);
    }
    
    @Override
    public void cancel() {
    	super.cancel();
    	txtCodigo.setDisable(false);
    	disableOnlyInsertFields(true);
    }
    
    @Override
    protected void prepareSearch() {
    	super.prepareSearch();
    	disableOnlyInsertFields(true);
    }
    
	private void disableOnlyInsertFields(boolean value) {
		txtValorCusto.setDisable(value);
		txtValorVenda.setDisable(value);
		comboMedida.setDisable(value);
	}

	private void maskTextFields() {
		MaskUtils.monetaryField(txtValorCusto);
		MaskUtils.monetaryField(txtValorVenda);
		MaskUtils.numericField(txtCodigo);
		MaskUtils.maxField(txtDescricao, 70);
		MaskUtils.maxField(txtCodigo, 30);
		MaskUtils.upperCase(txtDescricao);
	}
    
	private void carregaListaProdutos() {
		Platform.runLater( () -> {
			ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList(getService().findAllObservableList());	
			getDataTable().setItems(produtos);
		});
	}
    
	private void updateFotoLabelStatus() {
		if(imageProdutoView == null || imageProdutoView.getImage() == null)
			lblFotoStatus.setText(FotoService.EMPTY_PICTURE_LABEL);
		else
			lblFotoStatus.setText(null);
	}
    
	@Override
    public void cleanForm(){
        final String emptyString = "";
        
        txtCodigo.setText(emptyString);
        txtDescricao.setText(emptyString);
        txtValorCusto.setText(emptyString);
        txtValorVenda.setText(emptyString);
        
        comboMedida.getSelectionModel().selectFirst();
        comboCategorias.getSelectionModel().selectFirst();
        
        imageProdutoView.setImage(null);
        
        updateFotoLabelStatus();
    }
    
	@Override
	public Produto populateBeanWithFormData(final Produto produto) {
		try{
            produto.setCodigo(txtCodigo.getText());
            produto.setDescricao(txtDescricao.getText());
           
            produto.setValorCusto( NumericConverter.convertBigDecimal(txtValorCusto.getText()) );
            produto.setValorVenda( NumericConverter.convertBigDecimal(txtValorVenda.getText()) );
            
            produto.setMedida( comboMedida.getSelectionModel().getSelectedItem());
           
            Object selectedCat = comboCategorias.getSelectionModel().getSelectedItem();
			if(selectedCat != null && selectedCat instanceof Categoria){
				produto.setCategoria( (Categoria) selectedCat );
			}else{
				produto.setCategoria(null);
			}
			
            return produto;
        }catch(ConverterException e){
            addErrorMessage(e.getMessage());
            return null;
        }        
	}

	@Override
	public void sendRegisterToForm(Produto produto) {
		
        txtCodigo.setText(produto.getCodigo());
        txtDescricao.setText(produto.getDescricao());
        
        String valorCustoString = produto.getValorCusto().toString();
        String valorVendaString = produto.getValorVenda().toString();

        txtValorCusto.setText(valorCustoString.replace(".", ","));
		txtValorVenda.setText(valorVendaString.replace(".", ","));

        comboMedida.getSelectionModel().select(produto.getMedida());
        comboCategorias.getSelectionModel().select(produto.getCategoria());
        
        if(produto.getFoto() == null || produto.getImage() != null){
        	imageProdutoView.setImage(produto.getImage());
        	updateFotoLabelStatus();
        	return;
        }
        
        try{
        	FotoService.loadFoto(produto);
        	imageProdutoView.setImage(produto.getImage());
        	updateFotoLabelStatus();
        }catch(Exception e){
        	addErrorMessage("Erro ao carregar imagem do Produto.");
        }
	}
	
	@Override
	public InsertActionCallBack getInsertAction() {
		return () -> { getService().insert(getBean()); };
	}
	
	@Override
	public UpdateActionCallBack getUpdateAction() {
		return () -> { getService().update(getBean()); };
	}

	@Override
	public FindActionCallBack getFindAction() {
		return () -> {
			ObservableList<ProdutoModel> produtos = getService().find(txtCodigo.getText(), txtDescricao.getText());
			FXCollections.observableArrayList(produtos);
			getDataTable().setItems(produtos);

			if (produtos == null || produtos.isEmpty()) {
				addInfoMessage("Item(ns) não encontrado(s).");
			}
		};
	}

	@Override
	public void postInitialize() {
		comboCategorias.getItems().addAll( FXCollections.observableArrayList(this.catService.getDao().findAll()));
		comboCategorias.getSelectionModel().selectFirst();
		comboMedida.setItems(Medida.getObservableList());
		comboMedida.getSelectionModel().selectFirst();
		disableOnlyInsertFields(true);
		
		executeColumnsConfigurations();
		updateFotoLabelStatus();
		carregaListaProdutos();
		maskTextFields();
	}   
}