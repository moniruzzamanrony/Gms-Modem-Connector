package com.itvillage;


import org.smslib.helper.CommPortIdentifier;
import org.smslib.helper.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;


public class PortFinder {

    static CommPortIdentifier portId;
    static Enumeration<CommPortIdentifier> portList;
    static int baudRates[] = {9600};//, 14400, 19200, 28800, 33600, 38400, 56000, 57600, 115200 };

    private Enumeration<CommPortIdentifier> getCleanPortIdentifiers() {
        return CommPortIdentifier.getPortIdentifiers();
    }

    public ArrayList<String> getActiveModemPorts() {

        System.out.println("Please wait searching for devices...");
        ArrayList<String> portsArrayList =new ArrayList<String>();
        portList = getCleanPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                for (int i = 0; i < baudRates.length; i++) {
                    SerialPort serialPort = null;
                    try {
                        InputStream inStream;
                        OutputStream outStream;
                        int c;
                        String response;
                        serialPort = portId.open("CommPortIdentifier", 1971);
                        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                        serialPort.setSerialPortParams(baudRates[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
                        StringBuilder sb = new StringBuilder();
                        c = inStream.read();

                        while (c != -1) {
                            sb.append((char) c);
                            c = inStream.read();
                        }
                        response = sb.toString();

                        if (response.indexOf("OK") >= 0) {
                            try {
                                System.out.println("Getting Info...");

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
                                /*
                                 * NOTE: Port Name get from 'portId.getName()'
                                 * NOTE: Get Response Message Form 'response' variable
                                 * */

                                portsArrayList.add(portId.getName());
                            } catch (Exception e) {

                            }
                        } else {

                        }
                    } catch (Exception e) {
                        Throwable cause = e;
                        while (cause.getCause() != null) {
                            cause = cause.getCause();
                        }
                    } finally {
                        if (serialPort != null) {
                            serialPort.close();
                        }
                    }
                }
            }
        }
        if (portsArrayList.size() == 0) {
            portsArrayList.add("No Modem Found");
        }
        return portsArrayList;
    }
}
