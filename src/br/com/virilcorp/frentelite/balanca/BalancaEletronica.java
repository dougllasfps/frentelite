package br.com.virilcorp.frentelite.balanca;

import java.math.BigDecimal;

import br.com.virilcorp.frentelite.balanca.exception.PesoInstavelException;
import br.com.virilcorp.frentelite.balanca.exception.PesoNegativoException;
import br.com.virilcorp.frentelite.balanca.exception.SobreCargaPesoException;
import br.com.virilcorp.frentelite.serialport.SerialPortConfig;

public interface BalancaEletronica extends SerialPortConfig {
	void setBuffer(byte[] buffer);
    
    BigDecimal extrairPeso() throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException;
    
    void validar()  throws PesoInstavelException, PesoNegativoException, SobreCargaPesoException;
    
    byte[] getASCIIRequestHex();
    
    int getBytesSizeToRead();
}

