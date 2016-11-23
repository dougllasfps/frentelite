/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.balanca;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

/**
 *
 * @author dougllas.sousa
 */
public class SerialCom {
    
    protected String[] ports;
    @SuppressWarnings("rawtypes")
	protected Enumeration portsList;

    public SerialCom() {
        portsList = CommPortIdentifier.getPortIdentifiers();
    }

    public String[] getPorts() {
        return ports;
    }
    
    public void listPorts(){
        int i = 0;

        ports = new String[10];
        
        while (portsList.hasMoreElements()) {


        	CommPortIdentifier ips =
            ( CommPortIdentifier ) portsList.nextElement();

            ports[i] = ips.getName();
            
            System.out.println(ports[i]);
            
            i++;

        }
    }
    
    public boolean existPort(String port){
        String temp;

        boolean e = false;

        while ( portsList.hasMoreElements() ) {

            CommPortIdentifier ips = (CommPortIdentifier) portsList.nextElement();

            temp = ips.getName();

            if ( temp.equals(port) == true) {
                e = true;
            }

        }

        return e;
    }
    
    public static void main(String[] t){
        new SerialCom().listPorts();
        System.out.println("End");
    }
}
