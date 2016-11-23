package br.com.virilcorp.frentelite.model.configuracoes;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.virilcorp.frentelite.balanca.BalancaEletronica;
import br.com.virilcorp.frentelite.balanca.exception.PesoInstavelException;
import br.com.virilcorp.frentelite.balanca.exception.PesoNegativoException;
import br.com.virilcorp.frentelite.balanca.exception.SobreCargaPesoException;
import br.com.virilcorp.frentelite.model.BaseModel;
import br.com.virilcorp.frentelite.serialport.SerialPortConfig;

@Entity
@Table(schema="configuracao")
public class Balanca implements Serializable, SerialPortConfig, BaseModel, BalancaEletronica{
	private static final long serialVersionUID = -5116483091463066285L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String serialPort;
	
	@Column
	private Integer baudRate;
	
	@Column
	private Integer dataBits;
	
	@Column
	private Integer stopBits;
	
	@Column
	private Integer parity;
	
	@Column
	private Integer bytes;
	
	@Column
	private Integer asciCode;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSerialPort() {
		return serialPort;
	}
	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}
	public Integer getBaudRate() {
		return baudRate;
	}
	public void setBaudRate(Integer baudRate) {
		this.baudRate = baudRate;
	}
	public Integer getDataBits() {
		return dataBits;
	}
	public void setDataBits(Integer dataBits) {
		this.dataBits = dataBits;
	}
	public Integer getStopBits() {
		return stopBits;
	}
	public void setStopBits(Integer stopBits) {
		this.stopBits = stopBits;
	}
	public Integer getParity() {
		return parity;
	}
	public void setParity(Integer parity) {
		this.parity = parity;
	}
	public Integer getBytes() {
		return bytes;
	}
	public void setBytes(Integer bytes) {
		this.bytes = bytes;
	}
	public Integer getAsciCode() {
		return asciCode;
	}
	public void setAsciCode(Integer asciCode) {
		this.asciCode = asciCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baudRate == null) ? 0 : baudRate.hashCode());
		result = prime * result + ((dataBits == null) ? 0 : dataBits.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parity == null) ? 0 : parity.hashCode());
		result = prime * result + ((serialPort == null) ? 0 : serialPort.hashCode());
		result = prime * result + ((stopBits == null) ? 0 : stopBits.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Balanca other = (Balanca) obj;
		if (baudRate == null) {
			if (other.baudRate != null)
				return false;
		} else if (!baudRate.equals(other.baudRate))
			return false;
		if (dataBits == null) {
			if (other.dataBits != null)
				return false;
		} else if (!dataBits.equals(other.dataBits))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (parity == null) {
			if (other.parity != null)
				return false;
		} else if (!parity.equals(other.parity))
			return false;
		if (serialPort == null) {
			if (other.serialPort != null)
				return false;
		} else if (!serialPort.equals(other.serialPort))
			return false;
		if (stopBits == null) {
			if (other.stopBits != null)
				return false;
		} else if (!stopBits.equals(other.stopBits))
			return false;
		return true;
	}
	@Override
	public void setBuffer(byte[] buffer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public BigDecimal extrairPeso() throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validar() throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public byte[] getASCIIRequestHex() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getBytesSizeToRead() {
		// TODO Auto-generated method stub
		return 0;
	}
}