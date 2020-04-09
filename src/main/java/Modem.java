import java.util.ArrayList;

public class Modem {

    public static String sendATCommand(String portName,String cmd)
    {
        ATCommand application = new ATCommand();
        return application.show(portName,cmd);
    }

    public static ArrayList<String> getActiveModemPorts()
    {
        PortFinder portFinder=new PortFinder();
         return portFinder.getActiveModemPorts();
    }
}
