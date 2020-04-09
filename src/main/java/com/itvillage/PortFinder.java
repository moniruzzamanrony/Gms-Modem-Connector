package com.itvillage;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 *
 * @author monirozzamanroni
 */
public class PortFinder {

    private static final String _NO_DEVICE_FOUND = "  no device found";

    static CommPortIdentifier portId;

    static Enumeration<CommPortIdentifier> portList;

    static int bauds[] = { 9600};//, 14400, 19200, 28800, 33600, 38400, 56000, 57600, 115200 };

    /**
     * Wrapper around {@link CommPortIdentifier#getPortIdentifiers()} to be
     * avoid unchecked warnings.
     */
    private Enumeration<CommPortIdentifier> getCleanPortIdentifiers() {
        return CommPortIdentifier.getPortIdentifiers();
    }

    public ArrayList<String> getActiveModemPorts() {
        System.out.println("\nSearching for devices...");
        ArrayList<String> portsArrayList =new ArrayList<String>();
        portList = getCleanPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                for (int i = 0; i < bauds.length; i++) {
                    SerialPort serialPort = null;
                    // System.out.println("\nTrying at " + bauds[i]);
                    try {
                        InputStream inStream;
                        OutputStream outStream;
                        int c;
                        String response;
                        serialPort = portId.open("SMSLibCommTester", 1971);
                        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                        serialPort.setSerialPortParams(bauds[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        inStream = serialPort.getInputStream();
                        outStream = serialPort.getOutputStream();
                        serialPort.enableReceiveTimeout(1000);
                        c = inStream.read();
                        while (c != -1)
                            c = inStream.read();
                        outStream.write('A');
                        outStream.write('T');
                        outStream.write('\r');
                        Thread.sleep(1000);
                        response = "";
                        StringBuilder sb = new StringBuilder();
                        c = inStream.read();
                        while (c != -1) {
                            sb.append((char) c);
                            c = inStream.read();
                        }
                        response = sb.toString();
                        if (response.indexOf("OK") >= 0) {
                            try {
                                System.out.println("  Getting Info...");

                                outStream.write('A');
                                outStream.write('T');
                                outStream.write('+');
                                outStream.write('C');
                                outStream.write('G');
                                outStream.write('M');
                                outStream.write('I');
                                outStream.write('\r');

                                outStream.write('A');
                                outStream.write('T');
                                outStream.write('+');
                                outStream.write('C');
                                outStream.write('G');
                                outStream.write('M');
                                outStream.write('M');
                                outStream.write('\r');
                                response = "";
                                c = inStream.read();
                                while (c != -1) {
                                    response += (char) c;
                                    c = inStream.read();
                                }
                                //  System.out.println("\nFound: " + response.replaceAll("\\s+OK\\s+", "").replaceAll("\n", "").replaceAll("\r", "")+"---->"+ portId.getName());
                                portsArrayList.add(portId.getName());
                            } catch (Exception e) {
                                //System.out.println(_NO_DEVICE_FOUND);

                            }
                        } else {
                            //  System.out.println(_NO_DEVICE_FOUND);

                        }
                    } catch (Exception e) {
                        //  System.out.print(_NO_DEVICE_FOUND);
                        Throwable cause = e;
                        while (cause.getCause() != null) {
                            cause = cause.getCause();
                        }
                        //  System.out.println(" (" + cause.getMessage() + ")");

                    } finally {
                        if (serialPort != null) {
                            serialPort.close();
                        }
                    }
                }
            }
        }
        return portsArrayList;
    }
}
