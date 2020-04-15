// SendMessage.java - Sample application.
//
// This application shows you the basic procedure for sending messages.
// You will find how to send synchronous and asynchronous messages.
//
// For asynchronous dispatch, the example application sets a callback
// notification, to see what's happened with messages.

package org.ajwcc.pduUtils.test.integration;

import org.smslib.OutboundMessage;
import org.smslib.Service;

public class SendTextMessage extends AbstractTester {
    public static void main(String args[]) {
        SendTextMessage app = new SendTextMessage();
        try {
            app.initModem();
            app.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void test() throws Exception {
        OutboundMessage msg;
        // basic message
        msg = new OutboundMessage(MODEM_NUMBER, "Hello from SMSLib!");
        msg.setStatusReport(true);
        Service.getInstance().sendMessage(msg);
        System.out.println(msg);
        System.out.println("Now Sleeping - Hit <enter> to terminate.");
        System.in.read();
        Service.getInstance().stopService();
    }
}
