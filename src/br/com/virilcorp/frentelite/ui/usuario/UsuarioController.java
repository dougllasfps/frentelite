package br.com.virilcorp.frentelite.ui.usuario;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.virilcorp.frentelite.ViewState;
import br.com.virilcorp.frentelite.model.Modulo;
import br.com.virilcorp.frentelite.model.ModuloUsuario;
import br.com.virilcorp.frentelite.model.TipoModulo;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.service.ModuloService;
import br.com.virilcorp.frentelite.service.UsuarioService;
import br.com.virilcorp.frentelite.ui.CrudController;
import br.com.virilcorp.frentelite.util.StringUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class UsuarioController extends CrudController<Usuario, UsuarioModel, UsuarioService> implements Initializable {

	@FXML	private TextField inputNome, inputLogin;
	@FXML	private PasswordField inputSenha, inputRepeatSenha;
	@FXML	private CheckBox checkModuloVenda;
	@FXML	private CheckBox checkModuloProdutos;
	@FXML	private CheckBox checkModuloUsuario;
	@FXML	private CheckBox checkModuloRelatorios;
	@FXML	private CheckBox checkAdmin;
	@FXML   private TableColumn<UsuarioModel, String> nomeColumn;
	@FXML   private TableColumn<UsuarioModel, String> loginColumn;
	@FXML   private TableColumn<UsuarioModel, String> adminColumn;
	
	private ModuloService moduloService;

	
	private void executeColumnsConfigurations(){
		nomeColumn.setCellValueFactory( c -> c.getValue().getNome() );
		loginColumn.setCellValueFactory( c -> c.getValue().getLogin() );
		adminColumn.setCellValueFactory( c -> c.getValue().getAdministrador() );
	}
	
	private void initAccelerators(){
		Platform.runLater( () ->{
			ObservableMap<KeyCombination, Runnable> accelerators = inputNome.getScene().getAccelerators();
			accelerators.put(new KeyCodeCombination(KeyCode.F10, KeyCombination.CONTROL_DOWN), () -> {
				
			} );
		});
	}
	
	@Override
	public InsertActionCallBack getInsertAction() {
		return () ->{
			onCadastroSubmit();
		};
	}
	
	@Override
	public br.com.virilcorp.frentelite.ui.CrudController.UpdateActionCallBack getUpdateAction() {
		return () ->{
			onCadastroSubmit();
		};
	}
	
	private void onCadastroSubmit() {
		if(StringUtils.isNullOrEmpty(inputSenha.getText()) || StringUtils.isNullOrEmpty(inputRepeatSenha.getText())){
			throw new RuntimeException("Digite a senha e repita no campo logo abaixo.");
		}
		
		if(!inputSenha.getText().equals(inputRepeatSenha.getText())){
			throw new RuntimeException("As senhas não batem.");
		}
		
		getService().validate(getBean());
		
		if(getBean().getId() == null)
			getService().save(getBean());
		else
			getService().update(getBean());
		
		addInfoMessage("Usuário cadastrado/Atualizado com sucesso!");
		
		setBean(null);
	}

	private void defineModulos( final Usuario usuario) {

		boolean vendaIsAllowed = checkModuloVenda.isSelected();
		boolean produtoIsAllowed = checkModuloProdutos.isSelected();
		boolean usuariosIsAllowed = checkModuloUsuario.isSelected();
		boolean relatorioIsAllowed = checkModuloRelatorios.isSelected();
		
		if (getBean().getModulos() == null) {
			getBean().setModulos(new ArrayList<>());
		}

		if (vendaIsAllowed) {
			Modulo moduloVendas = moduloService.findByNome(TipoModulo.VENDA);
			if (!getBean().hasAcessoModulo(moduloVendas))
				getBean().getModulos().add(new ModuloUsuario(usuario, moduloVendas));
		}
		
		if (usuariosIsAllowed) {
			Modulo moduloUsuarios = moduloService.findByNome(TipoModulo.USUARIOS);
			if (!getBean().hasAcessoModulo(moduloUsuarios))
				getBean().getModulos().add(new ModuloUsuario(usuario, moduloUsuarios));
		}
		
		if (relatorioIsAllowed) {
			Modulo moduloRelatorios = moduloService.findByNome(TipoModulo.RELATORIOS);
			if (!getBean().hasAcessoModulo(moduloRelatorios))
				getBean().getModulos().add(new ModuloUsuario(usuario, moduloRelatorios));
		}

		if (produtoIsAllowed) {
			Modulo moduloCadProd = moduloService.findByNome(TipoModulo.CADASTRO_PRODUTO);
			if (!getBean().hasAcessoModulo(moduloCadProd))
				getBean().getModulos().add(new ModuloUsuario(usuario, moduloCadProd));
		}

	}
	
	public Usuario showUsuariosDialog(){
		List<Usuario> usuarios = getService().getDao().findAll();

		ChoiceDialog<Usuario> dialog = new ChoiceDialog<>(null, usuarios);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose your letter:");

		Optional<Usuario> result = dialog.showAndWait();
		return result.get();
	}

	@Override
	public Usuario populateBeanWithFormData(Usuario usuario) {
		if(!StringUtils.isNullOrEmpty(inputLogin.getText()))
			usuario.setLogin(inputLogin.getText());
		
		if(!StringUtils.isNullOrEmpty(inputNome.getText()))
			usuario.setNome(inputNome.getText());
		
		if(ViewState.SEARCH.equals(getState())){
			return usuario;
		}
		
		usuario.setAdministrador(checkAdmin.isSelected());
		
		try {
			usuario.setSenha(getService().hashMD5String(inputSenha.getText()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		defineModulos(usuario);
		return usuario;
	}

	@Override
	public void sendRegisterToForm(Usuario usuario) {
		inputNome.setText(usuario.getNome());
		inputLogin.setText(usuario.getLogin());
		
		usuario.setModulos( moduloService.findByUsuario(usuario) );

		checkAdmin.setSelected( usuario.isAdministrador() );
		checkModuloVenda.setSelected( usuario.hasAcessoModulo(moduloService, TipoModulo.VENDA) );
		checkModuloProdutos.setSelected( usuario.hasAcessoModulo(moduloService, TipoModulo.CADASTRO_PRODUTO) );
		checkModuloRelatorios.setSelected( usuario.hasAcessoModulo(moduloService, TipoModulo.RELATORIOS)  );
		checkModuloUsuario.setSelected( usuario.hasAcessoModulo(moduloService, TipoModulo.USUARIOS)  );
	}

	@Override
	public void cleanForm() {
		String empty = "";
		inputNome.setText(empty);
		inputLogin.setText(empty);
		inputSenha.setText(empty);
		inputRepeatSenha.setText(empty);

		boolean notSelected = false;
		
		checkAdmin.setSelected( notSelected);
		checkModuloVenda.setSelected( notSelected );
		checkModuloProdutos.setSelected( notSelected );
		checkModuloRelatorios.setSelected( notSelected );
		checkModuloUsuario.setSelected( notSelected  );
	}

	@Override
	public FindActionCallBack getFindAction() {
		return () ->{
			setBean(populateBeanWithFormData(new Usuario()));
			List<Usuario> list = getService().getDao().findByExample(getBean());
			ObservableList<UsuarioModel> modelList = FXCollections.observableArrayList();
			list.forEach( u -> modelList.add(new UsuarioModel(u)));
			
			getDataTable().setItems(modelList);
			
			if(list == null || list.isEmpty()){
				addInfoMessage("Nenhum item encontrado.");
			}
		};
	}

	@Override
	public void postInitialize() {
		this.moduloService = new ModuloService();
		initAccelerators();
		executeColumnsConfigurations();
	}
}