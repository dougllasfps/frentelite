/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author DOUGLLASFPS
 */
public class ApplicationFactory {

	public static final String FXML_PACKAGE_LOCATION = "/br/com/virilcorp/frentelite/ui/fxml/";
	public static final ApplicationType INITIAL_SCREEN = ApplicationType.LOGIN;
	public static final ApplicationType MAIN = ApplicationType.TELA_PRINCIPAL;

	public enum ApplicationType {

		LOGIN("Login.fxml", "Login", null),

		TELA_PRINCIPAL("MainWindow.fxml", "FrenteLite - Principal", null),

		CADASTRO_PRODUTO("CadastroProduto.fxml", "FrenteLite - Cadastro de Produtos", TELA_PRINCIPAL),

		CATEGORIA("Categoria.fxml", "FrenteLite - Cadastro de Categorias", TELA_PRINCIPAL),

		TARA("Tara.fxml", "FrenteLite - Tara", TELA_PRINCIPAL),

		USUARIO("Usuario.fxml", "FrenteLite - Cadastro de Usuários", TELA_PRINCIPAL),

		VENDA("Venda.fxml", "FrenteLite - Venda", TELA_PRINCIPAL),

		INPUT_QUANTIDADE_MODAL("InputQuantidade.fxml", "FrenteLite - Venda - Quantidade", VENDA),

		PAGAMENTO("Pagamento.fxml", "FrenteLite - Pagamento", VENDA),

		RELATORIO_VENDAS("PdfViewer.fxml", "FrenteLite - Relatório de Vendas", TELA_PRINCIPAL),

		LOADING("Load.fxml", "Carregando... Aguarde", null),

		CONFIG_BALANCA("Balanca.fxml", "Frente Lite - Balanca", TELA_PRINCIPAL),

		DELIVERY("Delivery.fxml", "Frente Lite - Delivery", VENDA),

		CONSULTA_DELIVERY("ConsultaDelivery.fxml", "Frente Lite - Consulta Delivery", TELA_PRINCIPAL),

		DETALHES_VENDA("DetalhesVenda.fxml", "Frente Lite - Detalhes da Venda", null),

		CONFIG_IMPRESSORA("Impressora.fxml", "Configurar Impressora", TELA_PRINCIPAL),

		CONFIG_ESTABELECIMENTO("Estabelecimento.fxml", "Frente Lite", TELA_PRINCIPAL),

		CONSULTA_VENDAS("ConsultaVenda.fxml", "Frente Lite - Consulta Vendas", TELA_PRINCIPAL),

		GERENCIAMENTO_CAIXA("GerenciamentoCaixa.fxml", "Frente Lite - Gerenciamento de Caixa", TELA_PRINCIPAL)

		;

		private final String resource;
		private final String title;
		private final ApplicationType owner;

		private ApplicationType(String resource, String title, ApplicationType owner) {
			this.resource = resource;
			this.title = title;
			this.owner = owner;
		}

		public String getResource() {
			return resource;
		}

		public String getTitle() {
			return title;
		}

		public boolean in(List<ApplicationType> list) {
			for (ApplicationType type : list) {
				if (type.equals(this)) {
					return true;
				}
			}
			return false;
		}

		public ApplicationType getOwner() {
			return owner;
		}
	}

	public static List<ApplicationType> getFinalizableApplications() {
		return Arrays.asList(ApplicationType.TELA_PRINCIPAL, ApplicationType.LOGIN);
	}
	
	public static List<ApplicationType> getMAximizableApplications() {
		return Arrays.asList(ApplicationType.TELA_PRINCIPAL, ApplicationType.VENDA, ApplicationType.CONSULTA_VENDAS);
	}

	public static List<ApplicationType> getModalApplications() {
		return Arrays.asList(ApplicationType.INPUT_QUANTIDADE_MODAL, ApplicationType.PAGAMENTO);
	}
}