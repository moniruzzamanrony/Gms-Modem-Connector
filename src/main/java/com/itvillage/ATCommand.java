package com.itvillage;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class ATCommand implements SerialPortEventListener {

    private static CommPortIdentifier portId = null;

    private Enumeration portList = null;

    private static InputStream inputStream = null;

    private static OutputStream out = null;

    private static SerialPort serialPort = null;

    private static int srate = 9600;//B-Rate

    private String data = null;

    public String response = "Not Readable Data";


    public String show(String portName,String cmd)
{
    sendATCommandLine(portName,cmd);
    return  sendATCommandLine(portName,cmd);
}

    public ATCommand() {

    }

    public  String sendATCommandLine(String portName,String cmd) {
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

        boolean portFound = false;
        //String defaultPort = "/dev/ttyUSB0";
        String defaultPort = port;
        try {
            portList = CommPortIdentifier.getPortIdentifiers();

            while (portList.hasMoreElements()) {

                CommPortIdentifier portID = (CommPortIdentifier) portList.nextElement();

                if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                    if (portID.getName().equals(defaultPort)) {
                        this.portId = portID;
                        System.out.println("Found port: " + portId.getName());
                        portFound = true;
                        break;
                    }
                }

            }
            if (!portFound) {
                System.out.println("port " + defaultPort + " not found.");
            }
        } catch (Exception e) {
            portFound = false;
        }

        return portFound;

    }

    public boolean initPort() {

        try {
            serialPort = (SerialPort) portId.open("SerialCommApp", 2000);
        } catch (PortInUseException e) {
            System.out.println("Port in use-->" + e);
        }

        try {
            inputStream = serialPort.getInputStream();
            out = serialPort.getOutputStream();
        } catch (IOException e) {
            System.out.println("IO Error-->" + e);
        }

        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            System.out.println("Too many LIstener-->" + e);
        }

        serialPort.notifyOnDataAvailable(true);

        try {
            serialPort.setSerialPortParams(srate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        } catch (UnsupportedCommOperationException e) {
            System.out.println("Error while setting parameters-->" + e);
        }

        System.out.println("Port Initialized....");
        return true;

    }

    public synchronized void serialEvent(SerialPortEvent event) {

        switch (event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                //   System.out.println("DATA_AVAILABLE");

                byte[] readBuffer = new byte[1024];
                int numBytes = 1024;
                data = "";

                try {
                    Thread.sleep(100);
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);//count of reading data
                        data = data + new String(readBuffer, 0, numBytes);
                        data = data.trim();
                        //this.recvdData+=data;
                        this.response = data;
                    }
                    //    System.out.println("data=========="+this.response);

                } catch (Exception e) {
                    System.out.println("Exception in serial event-->" + e);
                }

                break;//break from switch case 1:
        }//end of switch
    }

    public void sendMsg() {


    }

    public String sendAT(String cmd) {
        try {
            System.out.println("Sending: " + cmd);
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
            System.out.println(e);
        }
        return response;
    }
}
