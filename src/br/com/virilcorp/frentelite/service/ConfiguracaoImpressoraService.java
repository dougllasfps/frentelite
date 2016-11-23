package br.com.virilcorp.frentelite.service;

import java.io.IOException;

import br.com.virilcorp.frentelite.model.ImpressoraConfig;
import br.com.virilcorp.properties.configuration.PropertiesFileHandler;

public class ConfiguracaoImpressoraService extends GenericService<ImpressoraConfig>{

	private PropertiesFileHandler propertiesHandler;
	
	public ConfiguracaoImpressoraService() {
		try {
			propertiesHandler = new PropertiesFileHandler(getClass().getResource("/project.properties").toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public PropertiesFileHandler getPropertiesHandler() {
		return propertiesHandler;
	}

	public void setPropertiesHandler(PropertiesFileHandler propertiesHandler) {
		this.propertiesHandler = propertiesHandler;
	}
	
	public static void main(String[] args) {
		ConfiguracaoImpressoraService s = new ConfiguracaoImpressoraService();
		String value = s.getPropertiesHandler().get("hibernate.dialect");
		System.out.println(value);
	}
}