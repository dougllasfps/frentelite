/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.balanca;

import java.math.BigDecimal;

import br.com.virilcorp.frentelite.balanca.exception.PesoInstavelException;
import br.com.virilcorp.frentelite.balanca.exception.PesoNegativoException;
import br.com.virilcorp.frentelite.balanca.exception.SobreCargaPesoException;
import jssc.SerialPort;

/**
 *
 * @author dougllas.sousa
 * 4800 baud, 1 Stop Bit, 8 Bits de dados e sem paridade.
 */
public class ToledoPrix3Light implements BalancaEletronica {
    
    private String port;
    private byte[] buffer;
    private String weight;
    
    private static final String PESO_INSTAVEL_VALUE = "II.III";
    private static final String PESO_NEGATIVO_VALUE = "NN.NNN";
    private static final String SOBRE_CARGA_VALUE = "SS.SSS";
    
    private final int baudRate = SerialPort.BAUDRATE_4800;
    private final int stopBits = SerialPort.STOPBITS_1;
    private final int dataBits = SerialPort.DATABITS_8;
    private final int parity   = SerialPort.PARITY_NONE;
    private final int bytesToRead = 7;
    
    
    public ToledoPrix3Light(String port) {
		this.port = port;
	}

    @Override
    public void setSerialPort(String port) {
       this.port = port;
    }

    @Override
    public String getSerialPort() {
       return this.port;
    }

    @Override
    public Integer getBaudRate() {
       return this.baudRate;
    }

    @Override
    public Integer getDataBits() {
       return this.dataBits;
    }

    @Override
    public Integer getStopBits() {
       return this.stopBits;
    }

    @Override
    public Integer getParity() {
       return this.parity;
    }

    @Override
    public byte[] getASCIIRequestHex() {
       return new byte[]{0x05};
    }

    @Override
    public int getBytesSizeToRead() {
        return this.bytesToRead;
    }

	@Override
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	@Override
	public BigDecimal extrairPeso() throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException {
		this.weight = new String(buffer);
		this.weight = weight.substring(1, 3) + "." + weight.substring(3,weight.length() - 1) ;
		validar();
		
		try{
			return new BigDecimal(this.weight);
		}catch(NumberFormatException e){
			throw new PesoInstavelException("Balança Não está regulada.");
		}
	}

	@Override
	public void validar()  throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException{
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
}
