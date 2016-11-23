package br.com.virilcorp.frentelite.balanca;

public class BalancaTests {
//    private static Balanca instance;
//    String[] portNames = SerialPortList.getPortNames();
//    for (String portName : portNames) {
//        System.out.println(portName);
//    }
//
//    SerialPort serialPort = new SerialPort("COM1");
//    try {
//        System.out.println("Port opened: " + serialPort.openPort());
//        System.out.println("Params setted: " + serialPort.setParams(9600, 8, 2, 0));
//        System.out.println("successfully writen to port: " + serialPort.writeBytes(new byte[]{0x04}));
//        byte[] buffer = serialPort.readBytes(46);//Read 10 bytes from serial port
//        System.out.println(new String(buffer));
//        System.out.println("Port closed: " + serialPort.closePort());
//    } catch (SerialPortException ex) {
//        System.out.println(ex);
//    }
//    }
//       String[] list =  SerialPortList.getPortNames();
//       
//        for (String string : list) {
//            System.out.println(string);
//        }
//        
//        jssc.SerialPort serialPort = new jssc.SerialPort("COM1");
//        
//        try {
//        System.out.println("Port opened: " + serialPort.openPort());
//        System.out.println("Params setted: " +
//                
//        serialPort.setParams(
//                jssc.SerialPort.BAUDRATE_4800,
//                SerialPort.DATABITS_8,
//                SerialPort.STOPBITS_1, 
//                SerialPort.PARITY_NONE)
//        );
//        
//        System.out.println("successfully writen to port: " + serialPort.writeBytes("ENQ".getBytes()) );
//        byte[] buffer = serialPort.readBytes(17);
//        String value = new String(buffer);
//        System.out.println(value);
//        System.out.println("Port closed: " + serialPort.closePort());
//    } catch (SerialPortException ex) {
//        System.out.println(ex);
//    }  
//        catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(BalancaFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        
//        CommPortIdentifier comPort;
//        try {
//            comPort = CommPortIdentifier.getPortIdentifier("COM1");
//            SerialPort port = (SerialPort) comPort.open("BalancaFactory", 500);
//            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
//            port.getOutputStream().write(new byte[]{0x04});
//            
//            byte[] bytes = new byte[99];
//            port.getInputStream().read(bytes);
//            port.notifyOnDataAvailable(true);
//            
//            port.getInputStream().read(bytes);
//            
//            String value = new String(bytes);
//            
//            System.out.println(value);
//            
//        } catch (NoSuchPortException ex) {
//            Logger.getLogger(BalancaFactory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (PortInUseException ex) {
//            Logger.getLogger(BalancaFactory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedCommOperationException ex) {
//            Logger.getLogger(BalancaFactory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(BalancaFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    	
//        try {
//        	Balanca balanca = new ToledoPrix3Light("COM1");
//            BalancaHandler handler = new BalancaHandler(balanca);
//            BigDecimal leitura =  handler.fazerLeituraPeso() ;
//            System.out.println(leitura.toString());
//        } catch (SerialPortException  ex) {
//            Logger.getLogger(BalancaTests.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
