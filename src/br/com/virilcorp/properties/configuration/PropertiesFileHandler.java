package br.com.virilcorp.properties.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;

public class PropertiesFileHandler {
	
	private File file;
	private FileBasedConfigurationBuilder<PropertiesConfiguration> builder;
	private PropertiesConfiguration configuration;

	public PropertiesFileHandler(String filePath) throws IOException {
		this.file = new File(filePath);
		try {
			Configurations configs = new Configurations();
			builder = configs.propertiesBuilder(file);
			configuration = builder.getConfiguration();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveOrUpdate(String property, String value) {
		try {
			getConfiguration().setProperty(property, value);
			getBuilder().save();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String get(String property){
		return (String) getConfiguration().getProperty(property);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public PropertiesConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PropertiesConfiguration configuration) {
		this.configuration = configuration;
	}

	public FileBasedConfigurationBuilder<PropertiesConfiguration> getBuilder() {
		return builder;
	}

	public void setBuilder(FileBasedConfigurationBuilder<PropertiesConfiguration> builder) {
		this.builder = builder;
	}

	
}