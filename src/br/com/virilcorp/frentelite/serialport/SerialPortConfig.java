/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.serialport;

public interface SerialPortConfig {
    
    void setSerialPort(String port);
    
    String getSerialPort();
    
    Integer getBaudRate();
    
    Integer getDataBits();
    
    Integer getStopBits();
    
    Integer getParity();
    
}
