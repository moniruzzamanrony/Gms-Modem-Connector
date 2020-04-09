//import gnu.io.CommPortIdentifier;
//import gnu.io.PortInUseException;
//import gnu.io.SerialPort;
//import gnu.io.UnsupportedCommOperationException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Enumeration;
//import java.util.TooManyListenersException;
//
//public class Sms {
//    private Enumeration portList;
//    private CommPortIdentifier portId;
//    private String messageString1 = "AT";
//    private String messageString2 = "AT+CPIN=\"7078\"";
//    private String messageString3 = "AT+CMGF=1";
//    private String messageString4 = "AT+CMGS=\"+8801988841890\"";
//
//    private SerialPort serialPort;
//    private OutputStream outputStream;
//    private InputStream inputStream;
//
//    private char enter = 13;
//    private char CTRLZ = 26;
//
//    private int c;
//    private String response;
//
//    public void sandSmsRequest(String mgs,String port) throws InterruptedException
//    {
//        portList = CommPortIdentifier.getPortIdentifiers();
//        while (portList.hasMoreElements()) {
//            portId = (CommPortIdentifier) portList.nextElement();
//            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//
//                if (portId.getName().equals(port)) {
//
//                    try {
//                        serialPort = (SerialPort) portId.open(port, 2000);
//                    } catch (PortInUseException e) {
//                        System.out.println("err");
//                    }
//                    try {
//                        outputStream = serialPort.getOutputStream();
//                        inputStream = serialPort.getInputStream();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//                                                                SerialPort.PARITY_NONE);
//                    } catch (UnsupportedCommOperationException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//
//                        outputStream.write((messageString1 + enter).getBytes());
//                        Thread.sleep(100);
//                        outputStream.flush();
//
//                        outputStream.write((messageString2 + enter).getBytes());
//                        Thread.sleep(100);Thread.sleep(100);
//                        outputStream.flush();
//
//                        outputStream.write((messageString3 + enter).getBytes());
//                        Thread.sleep(100);
//                        outputStream.flush();
//
//
//                        outputStream.write((messageString4 + enter).getBytes());
//                        Thread.sleep(100);
//                        outputStream.flush();
//
//                        outputStream.write((mgs + CTRLZ).getBytes());
//                        outputStream.flush();
//
//
//                        outputStream.close();
//                        serialPort.close();
//                        System.out.println("Sent Successful");
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    public void readSmsRequest(String port) throws InterruptedException
//    {
//        portList = CommPortIdentifier.getPortIdentifiers();
//        while (portList.hasMoreElements()) {
//            portId = (CommPortIdentifier) portList.nextElement();
//            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//
//                if (portId.getName().equals(port)) {
//
//                    try {
//                        serialPort = (SerialPort) portId.open(port, 2000);
//                    } catch (PortInUseException e) {
//                        System.out.println("err");
//                    }
//                    try {
//                        outputStream = serialPort.getOutputStream();
//                        inputStream = serialPort.getInputStream();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//                                SerialPort.PARITY_NONE);
//                    } catch (UnsupportedCommOperationException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//
//                        outputStream.write(("AT+COPS?" + enter).getBytes());
//                        Thread.sleep(100);
//
//                        SerialCommandLine serialCommandLine =new SerialCommandLine();
//                        this.serialPort
//                                .addEventListener(new SerialCommandLine.SerialReader(
//                                        inputStream));
//                        serialCommandLine.
//                        outputStream.flush();
//
//                        outputStream.close();
//                        serialPort.close();
//                        System.out.println("Sent Successful");
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (TooManyListenersException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//
//}
