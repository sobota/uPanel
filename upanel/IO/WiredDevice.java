package upanel.IO;

import gnu.io.SerialPortEventListener;

import java.util.Set;

public interface WiredDevice extends IODevice {

	void setPortName(String portName);

	void setEventListener(SerialPortEventListener listener);

	Set getPortList();

}
