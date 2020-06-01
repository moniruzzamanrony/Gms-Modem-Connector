import com.itvillage.Modem;
import org.junit.jupiter.api.Test;

public class ModemTest {

    @Test
    public void getPortsListTest() {
        Modem modem = new Modem();
        System.out.println(modem.getActiveModemPorts());
    }

    @Test
    public void sendUSSDCommand() {
        Modem modem = new Modem();
        System.out.println(modem.sendATCommand("COM24", "AT+CPBR=1,99"));
    }
}
