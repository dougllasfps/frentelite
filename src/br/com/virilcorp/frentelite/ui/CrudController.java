package br.com.virilcorp.frentelite.ui;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import br.com.virilcorp.frentelite.ViewState;
import br.com.virilcorp.frentelite.exception.RegistroCadastradoException;
import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.BaseModel;
import br.com.virilcorp.frentelite.service.GenericService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public abstract class CrudController<T extends BaseModel, DM extends DataModel<T>, S extends GenericService<T>> extends Controller implements CrudView<T>{
	
	private T bean;
	private S service;
	private final Class<T> beanClass;
	private final Class<T> serviceClass;
	private ViewState state;
	
	@FXML private Button btnSalvar, btnPesquisar, btnExcluir, btnNovo;
	@FXML private TableView<DM> dataTable;
	
	@SuppressWarnings("unchecked")
	public CrudController()  {
		this.beanClass = (Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()) .getActualTypeArguments()[0];
		this.serviceClass = (Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()) .getActualTypeArguments()[2];
		this.state = ViewState.SEARCH;
		
		try {
			this.service = (S) this.serviceClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Platform.runLater( () -> { 
			getBtnSalvar().setDisable(true); 
			getBtnExcluir().setDisable(true); 
		});
	}
	
	@Override
	public void handleNewRegister(ActionEvent evt) {
		prepareInsert();
	}

	@Override
	public void handleSave(ActionEvent evt) {
		try {
			if(bean != null && bean.getId() != null){
				populateBeanWithFormData(bean);
				getUpdateAction().execute();
				this.bean = null;
			}else{
				populateBeanWithFormData(bean);
				getInsertAction().execute();
			}
			getDataTable().setItems	(null);
			prepareSearch();
			addInfoMessage("Salvo com sucesso.");
			
		} catch (RegistroCadastradoException e) {
			addErrorMessage(e.getMessage());
		} catch (ValidationException e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Ocorreu um erro." + e.getMessage());
		}
	}
 
	@Override
	public void handleSearch(ActionEvent evt) {
		try {
			getFindAction().execute();
		} catch (Exception e) {
			addErrorMessage("Ocorreu um erro." + e.getMessage());
		}
	}

	@Override
	public void handleDelete(ActionEvent evt) {
		try {
			boolean deleted = showDeleteConfirmDialog();
			
			if(!deleted){
				return;
			}
			
			getDataTable().setItems(null);
        	prepareSearch();
            addInfoMessage("Item Removido com sucesso!");
            
		} catch (Exception e) {
			addErrorMessage("Ocorreu um erro." + e.getMessage());
		}
	}

	@Override
	public void onRowSelect(MouseEvent evt) {
		DM selectedItem = getDataTable().getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			setBean(selectedItem.getData());
			sendRegisterToForm(getBean());
			prepareEdit();
		}
	}
	
	protected T getBean() {
		return bean;
	}

	protected void setBean(T bean) {
		this.bean = bean;
	}
	
	protected void prepareInsert(){
		try {
			setBean(beanClass.newInstance());
			btnPesquisar.setDisable(true);
			btnExcluir.setDisable(true);
			btnSalvar.setDisable(false);
			btnSalvar.setText("Salvar");
			cleanForm();
			setState(ViewState.INSERT);
			changeNewButtonBehavior();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void prepareEdit(){
		btnPesquisar.setDisable(true);
		btnExcluir.setDisable(false);
		btnSalvar.setDisable(false);
		btnSalvar.setText("Atualizar");
		setState(ViewState.UPDATE);
		changeNewButtonBehavior();
	}
	
	protected void prepareSearch(){
		btnExcluir.setDisable(true);
		btnPesquisar.setDisable(false);
		btnSalvar.setDisable(true);
		btnNovo.setDisable(false);
		setState(ViewState.SEARCH);
		changeNewButtonBehavior();
		cleanForm();
	}

	private void changeNewButtonBehavior() {
		if(ViewState.INSERT.equals(getState()) || ViewState.UPDATE.equals(getState())){
			btnNovo.setText("Cancelar");
			btnNovo.setOnAction( (evt) ->  cancel() );
		}else{
			btnNovo.setText("Novo");
			btnNovo.setOnAction( (evt) -> prepareInsert() );
		}
	}
	
	public void cancel(){
		prepareSearch();
	}
	
	private boolean showDeleteConfirmDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Exclusão de Item");
        alert.setContentText("Confirma a exclusão do Item selecionado?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
            	getDeleteAction().execute();
            	return true;
            }catch(Exception e){
                addErrorMessage(e.getMessage());
                return false;
            }
        } 
        return false;
    }

	public Button getBtnSalvar() {
		return btnSalvar;
	}

	public void setBtnSalvar(Button btnSalvar) {
		this.btnSalvar = btnSalvar;
	}

	public Button getBtnPesquisar() {
		return btnPesquisar;
	}

	public void setBtnPesquisar(Button btnPesquisar) {
		this.btnPesquisar = btnPesquisar;
	}

	public Button getBtnExcluir() {
		return btnExcluir;
	}

	public void setBtnExcluir(Button btnExcluir) {
		this.btnExcluir = btnExcluir;
	}

	public Button getBtnNovo() {
		return btnNovo;
	}

	public void setBtnNovo(Button btnNovo) {
		this.btnNovo = btnNovo;
	}

	public void setDataTable(TableView<DM> dataTable) {
		this.dataTable = dataTable;
	}
	
	public TableView<DM> getDataTable() {
		return dataTable;
	}
	
	public Class<T> getBeanClass() {
		return beanClass;
	}

	public DeleteActionCallBack getDeleteAction(){
		return () -> { getService().delete(getBean());};
	}
	
	public UpdateActionCallBack getUpdateAction(){
		return () -> { getService().update(getBean());};
	}
	
	public InsertActionCallBack getInsertAction(){
		return () -> { getService().save(getBean());};
	}
	
	public abstract FindActionCallBack getFindAction();

	public S getService() {
		return service;
	}

	public void setService(S service) {
		this.service = service;
	}

	public Class<T> getServiceClass() {
		return serviceClass;
	}

	public ViewState getState() {
		return state;
	}

	public void setState(ViewState state) {
		this.state = state;
	}

	public interface DeleteActionCallBack extends ActionCallBack{	}
	
	public interface UpdateActionCallBack extends ActionCallBack{	}
	
	public interface InsertActionCallBack extends ActionCallBack{	}
	
	public interface FindActionCallBack extends ActionCallBack{	}
	
	public interface ActionCallBack{
		void execute() throws Exception;
	}
}