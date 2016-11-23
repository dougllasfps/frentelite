package br.com.virilcorp.frentelite.serialport;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

import org.jboss.logging.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class SerialCommunication {

	private final SerialPortConfig portConfig;
	private SerialPort serialPort;
	private boolean paramsSeted;
	
	public SerialCommunication( SerialPortConfig portConfig ) {
		this.portConfig = portConfig;
		this.serialPort = new SerialPort( portConfig.getSerialPort() );
	}
	
	@SuppressWarnings("rawtypes")
	public void openConnection() throws SerialPortException{
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier nextElement = null;
		Logger logger = Logger.getLogger(getClass());
		
		logger.info("INICIALIZANDO REGISTRO DA PORTA DA BALANÇA");
		
		while(!this.serialPort.isOpened() && portIdentifiers.hasMoreElements()){
			nextElement = (CommPortIdentifier) portIdentifiers.nextElement();
			String porta = nextElement.getName();
			this.serialPort = new SerialPort(porta);
			this.serialPort.openPort();
		}
	}
	
	public void write(byte[] buffer) throws SerialPortException{
		if(!paramsSeted){
			configParams();
		}
		this.serialPort.writeBytes(buffer);
	}

	private boolean configParams() throws SerialPortException {
		return paramsSeted = this.serialPort.setParams( portConfig.getBaudRate(), portConfig.getDataBits(), portConfig.getStopBits(), portConfig.getParity() );
	}
	
	public byte[] read(byte[] asciRequest, int size, int timeout) throws SerialPortException, SerialPortTimeoutException{
		if(!paramsSeted){
			configParams();
		}
		
		if(asciRequest != null){
			write(asciRequest);
		}
		return this.serialPort.readBytes(size, timeout);
	}
	
	public byte[] read(int size, int timeout) throws SerialPortException, SerialPortTimeoutException{
		return read(null, size, timeout);
	}
	
	public void close() throws SerialPortException{
		this.serialPort.closePort();
	}
	
}

