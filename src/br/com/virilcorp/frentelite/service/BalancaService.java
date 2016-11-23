package br.com.virilcorp.frentelite.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import br.com.virilcorp.frentelite.exception.ValidationException;
import br.com.virilcorp.frentelite.model.configuracoes.Balanca;
import br.com.virilcorp.frentelite.util.CollectionUtils;
import br.com.virilcorp.properties.configuration.PropertiesFileHandler;

public class BalancaService extends GenericService<Balanca>{
	
	private PropertiesFileHandler propertiesHandler;
	
	public BalancaService() {
		try {
			this.propertiesHandler = new PropertiesFileHandler("/project.properties");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Balanca loadBalancaConfig(){
		Balanca balanca = new Balanca();
		
		String asci 		= getPropertiesHandler().get(BalancaConfigKeys.ASCI.getKey());
		String baudrate 	= getPropertiesHandler().get(BalancaConfigKeys.BAUD_RATE.getKey());
		String bytesToRead 	= getPropertiesHandler().get(BalancaConfigKeys.BYTES_TO_READ.getKey());
		String databits 	= getPropertiesHandler().get(BalancaConfigKeys.DATA_BITS.getKey());
		String parity 		= getPropertiesHandler().get(BalancaConfigKeys.PARITY.getKey());
		String serialPort 	= getPropertiesHandler().get(BalancaConfigKeys.SERIAL_PORT.getKey());
		String stopbits 	= getPropertiesHandler().get(BalancaConfigKeys.STOP_BITS.getKey());
		
		if(asci != null)			balanca.setAsciCode(Integer.valueOf(asci));
		if(baudrate != null)		balanca.setBaudRate(Integer.valueOf(baudrate));
		if(bytesToRead != null)		balanca.setBytes(Integer.valueOf(bytesToRead));
		if(databits != null)		balanca.setDataBits(Integer.valueOf(databits));
		if(parity != null)			balanca.setParity(Integer.valueOf(parity));
		if(serialPort != null)		balanca.setSerialPort(serialPort);
		if(stopbits != null)		balanca.setStopBits(Integer.valueOf(stopbits));
		
		return balanca;
	}
	
	public PropertiesFileHandler getPropertiesHandler() {
		return propertiesHandler;
	}

	public void saveOrUpdate(Balanca balanca){
		validate(balanca);
		Map<String, String> properties = extractProperties(balanca);
		for( String key : properties.keySet() ){
			getPropertiesHandler().saveOrUpdate(key, properties.get(key));
		}
	}
	
	private void validate(Balanca balanca) throws ValidationException{
		Field[] declaredFields = balanca.getClass().getDeclaredFields();
		
		StringBuilder msg = new StringBuilder();
		boolean error = false;
		
		for (Field field : declaredFields) {
			if(field.getName().equalsIgnoreCase("id")){
				return;
			}
			
			try {
				field.setAccessible(true);
				Object object = field.get(balanca);
				if(object == null){
					msg.append("O preenchimento do campo " + field.getName() + " é obrigatório. \n ");
					error = true;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		if(error){
			throw new ValidationException(msg.toString());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> extractProperties(Balanca balanca){
		
		Map map = CollectionUtils.createHashMap();
		
		map.put(BalancaConfigKeys.ASCI.getKey(), balanca.getAsciCode().toString());
		map.put(BalancaConfigKeys.BAUD_RATE.getKey(), balanca.getBaudRate().toString());
		map.put(BalancaConfigKeys.BYTES_TO_READ.getKey(), String.valueOf(balanca.getBytesSizeToRead()));
		map.put(BalancaConfigKeys.DATA_BITS.getKey(), balanca.getDataBits().toString());
		map.put(BalancaConfigKeys.PARITY.getKey(), balanca.getParity().toString());
		map.put(BalancaConfigKeys.SERIAL_PORT.getKey(), balanca.getSerialPort());
		map.put(BalancaConfigKeys.STOP_BITS.getKey(), balanca.getStopBits().toString());
		
		return map;
	}
	
}
