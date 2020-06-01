package com.itvillage;


import org.smslib.helper.CommPortIdentifier;
import org.smslib.helper.SerialPort;
import org.smslib.helper.SerialPortEvent;
import org.smslib.helper.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class ATCommand implements SerialPortEventListener {

    private static CommPortIdentifier portId = null;
    private static InputStream inputStream = null;
    private static OutputStream out = null;
    private static SerialPort serialPort = null;
    private static int srate = 9600;//B-Rate

    private String data = null;
    private Enumeration portList = null;
    public String response = "Not Readable Data";


    public String send(String portName, String cmd) {
        return sendATCommandLine(portName, cmd);
    }


    public String sendATCommandLine(String portName, String cmd) {
        try {
            if (this.detectPort(portName)) {
                if (this.initPort()) {
                    return sendAT(cmd);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in SerialComm()-->" + e);
        }
        return response;
    }


    public boolean detectPort(String port) {
        boolean isPortFound = false;
        String currentActivePort = port;

        try {
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                CommPortIdentifier portID = (CommPortIdentifier) portList.nextElement();
                if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    if (portID.getName().equals(currentActivePort)) {
                        this.portId = portID;
                        isPortFound = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            isPortFound = false;
        }
        return isPortFound;
    }

    public boolean initPort() {
        serialPort = (SerialPort) portId.open("SerialCommApp", 2000);
        inputStream = serialPort.getInputStream();
        out = serialPort.getOutputStream();

        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
        serialPort.setSerialPortParams(srate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

        System.out.println("Port Initialized successful");
        return true;
    }

    public synchronized void serialEvent(SerialPortEvent event) {

        switch (event.getEventType()) {
            case 1:
                byte[] readBuffer = new byte[1024];
                int numBytes = 1024;
                data = "";
                try {
                    Thread.sleep(100);
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);
                        data = data + new String(readBuffer, 0, numBytes);
                        data = data.trim();
                        this.response = data;
                    }
                    // NOTE: For getting each response to print this.response variable
                } catch (Exception e) {
                    System.out.println("Exception in serial event-->" + e);
                }
                break;
        }
    }


    public String sendAT(String cmd) {
        try {
            this.response = "";
            String mysms = cmd;
            out.write(mysms.getBytes());
            out.write(13);
            Thread.sleep(500);

            if (this.response.contains("OK")) {
                return response;
            } else {
                sendAT(cmd);
            }
            return response;
        } catch (Exception e) {
            System.out.println("Failed: " + e);
        }
        return response;
    }
}
