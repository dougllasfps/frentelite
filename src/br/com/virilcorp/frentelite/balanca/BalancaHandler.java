/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.balanca;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import br.com.virilcorp.frentelite.balanca.exception.BalancaInativaException;
import br.com.virilcorp.frentelite.balanca.exception.PesoInstavelException;
import br.com.virilcorp.frentelite.balanca.exception.PesoNegativoException;
import br.com.virilcorp.frentelite.balanca.exception.SobreCargaPesoException;
import br.com.virilcorp.frentelite.serialport.SerialCommunication;
import br.com.virilcorp.frentelite.service.BalancaService;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author dougllas.sousa
 */
public class BalancaHandler {
    
    private BalancaEletronica balanca;
    private BalancaService service;
    private final SerialCommunication serialCommunication;
    
    private static final String PESO_INSTAVEL_VALUE = "II.III";
    private static final String PESO_NEGATIVO_VALUE = "NN.NNN";
    private static final String SOBRE_CARGA_VALUE = "SS.SSS";
    
    private static BalancaHandler instance;
    
    public static BalancaHandler getInstance() {
    	if(instance == null){
    		try {
    			instance = new BalancaHandler();
    		} catch (SerialPortException e) {
    			e.printStackTrace();
    			instance = null;
    		}
    	}
		return instance;
	}

    public BalancaHandler() throws SerialPortException {
    	service = new BalancaService();
        this.balanca = service.loadBalancaConfig();
        serialCommunication = new SerialCommunication(balanca);
        generateConnectionWithSerialPort();
    }
    
    public BigDecimal fazerLeituraPeso() throws SerialPortException, PesoInstavelException, PesoNegativoException, SobreCargaPesoException, BalancaInativaException{
    	try {
			getSerialCommunication().openConnection();
			byte[] asciRequest = new byte[]{0x05};
			byte[] buffer = getSerialCommunication().read( asciRequest, this.balanca.getBytesSizeToRead(), 5000);
			return extrairPeso(buffer);
		} catch (SerialPortTimeoutException e) {
			throw new BalancaInativaException("Erro ao acessar balança: " + e.getMessage());
		} finally {
			try {
				getSerialCommunication().close();
			} catch (SerialPortException e) {
				throw new BalancaInativaException("Balança não conectada.");
			}
		}
    }
    
	public BigDecimal extrairPeso(byte[] buffer) throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException {
		String weight = new String(buffer);
		weight = weight.substring(1, 3) + "." + weight.substring(3,weight.length() - 1) ;
		validar(weight);
		
		try{
			return new BigDecimal(weight);
		}catch(NumberFormatException e){
			throw new PesoInstavelException("Balança Não está regulada.");
		}
	}

	public void validar(String weight)  throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException{
		if(PESO_INSTAVEL_VALUE.equals(weight)){
    		throw new PesoInstavelException();
    	}
    	
    	if(PESO_NEGATIVO_VALUE.equals(weight)){
    		throw new PesoNegativoException();
    	}
    	
    	if(SOBRE_CARGA_VALUE.equals(weight)){
    		throw new SobreCargaPesoException();
    	}
	}
    
	public BalancaEletronica getBalanca() {
        return balanca;
    }

    public SerialCommunication getSerialCommunication() {
		return serialCommunication;
	}

	public void setBalanca(BalancaEletronica balanca) {
        this.balanca = balanca;
    }
    
    public static void main(String[] args) {
//		try {
//			BalancaHandler b = getInstance();
//			try {
//				System.out.println(b.fazerLeituraPeso().toPlainString());
//			} catch (PesoInstavelException | PesoNegativoException | SobreCargaPesoException | BalancaInativaException e) {
//				e.printStackTrace();
//			}
//		} catch (SerialPortException e) {
//			e.printStackTrace();
//		}
    	
    	byte[] asciRequest = new byte[]{ Byte.valueOf("5" ,16) };
    	System.out.println(asciRequest);
    	
	}

	private void generateConnectionWithSerialPort() {
		CommPortIdentifier portIdentifier;
		try {
			
			portIdentifier = CommPortIdentifier.getPortIdentifier( this.balanca.getSerialPort() );
			gnu.io.SerialPort port = (gnu.io.SerialPort) portIdentifier.open("frentelite", 50);
			port.setSerialPortParams(balanca.getBaudRate(), balanca.getDataBits(), balanca.getStopBits(), balanca.getParity());
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port.notifyOnDataAvailable(true);
			port.getOutputStream().write( new byte[0x05] );
			port.getOutputStream().flush();
			InputStream inputStream = port.getInputStream();
			
			byte[] bytes = new byte[ this.balanca.getBytesSizeToRead() ];
			inputStream.read(bytes);
			
			String string = new String(bytes);
			System.out.println(string);
			
			inputStream.close();
			port.getOutputStream().close();
			
			port.close();
			
		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
			
		}
	}
	
//  public BigDecimal fazerLeituraPeso() throws SerialPortException, PesoInstavelException, PesoNegativoException, SobreCargaPesoException, BalancaInativaException{
//  try {
//  	if(!this.serialPort.isOpened())
//  		this.serialPort.openPort();
//  	
//		this.serialPort.setParams(balanca.getBaudRate(), balanca.getDataBits(), balanca.getDataBits(), balanca.getParity());
//      this.serialPort.writeBytes( getBalanca().getASCIIRequestHex() );
//      
//      byte[] buffer = this.serialPort.readBytes( getBalanca().getBytesSizeToRead(), 5000 );
//      balanca.setBuffer(buffer);
//      return balanca.extrairPeso();
//      
//  } catch(SerialPortException | SerialPortTimeoutException e){
//  	throw new BalancaInativaException("Erro ao acessar balança: " + e.getMessage());
//  }finally{
//  	try{
//  		this.serialPort.closePort();
//  	}catch(SerialPortException e){
//      	throw new BalancaInativaException("Balança não conectada.");
//      }
//  }
//}
}
