package upanel.IO;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.prefs.Preferences;

import upanel.menu.SerialSettingsPanel;

public class SerialIO implements WiredDevice {

	private OutputStream outStream = null;
	private BufferedOutputStream outBufferedStream = null;
	private InputStream inStream = null;
	private static SerialPort port = null;
	private BufferedInputStream inBufferedReader = null;
	private static CommPortIdentifier portId = null;

	private static String portNameGlob = "";
	private static boolean configurePort = false, configureName, configureBaud, configureDatabits, configureStopBits, configureParity;// DODAC
																																		// sparwdzenie
																																		// czy
																																		// skonfigurowany
	private boolean isWorking;

	private final Preferences pref = Preferences.userNodeForPackage(SerialSettingsPanel.class).node("SerialSettings");

	private int baudrate, databits, stopbits, parity;

	private static SerialIO instance;

	public static SerialIO getInstance(final String portName) {

		if (instance == null)
			instance = new SerialIO();

		portNameGlob = portName;

		return instance;
	}

	public static SerialIO getInstance() {

		if (instance == null) {
			instance = new SerialIO();

		}

		return instance;
	}

	@Override
	public void setPortName(final String port_Name) {

		portNameGlob = port_Name;
	}

	@Override
	public boolean isConfigure() {

		if (configureName && configureBaud && configureDatabits && configureStopBits && configureParity) {

			configurePort = true;
		} else
			configurePort = false;

		return configurePort;
	}

	/**
	 * @parms baudrate, databits, stopbits, parity
	 */

	public void setPortParms(int baudrate, int databits, int stopbits, int parity) {

		this.baudrate = baudrate;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;

		SerialIO.configureBaud = true;
		SerialIO.configureBaud = true;
		SerialIO.configureDatabits = true;
		SerialIO.configureStopBits = true;
		SerialIO.configureParity = true;

		configurePort = true;
	}

	private void serialInit(final String port_Name) {

		System.out.print(configurePort);
		try {

			portId = CommPortIdentifier.getPortIdentifier(port_Name);
			port = (SerialPort) portId.open("App Name", 40000);// kolejnos??????
																// inicjalizaji

			// ARDUINO
			// port.setSerialPortParams(9600, SerialPort.DATABITS_8,
			// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			if (!configurePort) {

				port.setSerialPortParams(new Integer(pref.get("baudratePort", "9600")), new Integer(pref.get("dataBitsPort", new Integer(
					SerialPort.DATABITS_8).toString())), new Integer(pref.get("stopBitsPort", new Integer(SerialPort.STOPBITS_1).toString())), new Integer(
					pref.get("parityPort", new Integer(SerialPort.PARITY_NONE).toString())));
				configurePort = true;

				/*
				 * ("baudrate" ("stopBits" ("dataBits" ("parity",
				 */
			} else {

				port.setSerialPortParams(baudrate, databits, stopbits, parity);

			}

			port.notifyOnDataAvailable(true);

			outStream = port.getOutputStream();
			inStream = port.getInputStream();

			outBufferedStream = new BufferedOutputStream(outStream);

			isWorking = true;

			inBufferedReader = new BufferedInputStream(inStream);
		} catch (final UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block

			configurePort = false;
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void writeMessage(final String msg) {

		if (isWorking) {
			try {

				outStream.write(msg.getBytes("UTF-8"));// check encoded
				// outBufferedStream.write(msg.getBytes());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public BufferedOutputStream getStreamToWrite() {

		// TODO Auto-generated method stub

		return outBufferedStream;
	}

	@Override
	public BufferedInputStream getStreamToRead() {

		return inBufferedReader;
	}

	@Override
	public void close() {

		port.removeEventListener();
		port.close();

		try {

			// outStream.close();
			// outBufferedWriter.close();
			inStream.close();
			inBufferedReader.close();

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getPortList() {

		final Enumeration ports = CommPortIdentifier.getPortIdentifiers();

		final Set<String> portSet = new HashSet<String>();

		CommPortIdentifier port;

		while (ports.hasMoreElements()) {

			port = (CommPortIdentifier) ports.nextElement();

			if (port.getPortType() == CommPortIdentifier.PORT_SERIAL)
				portSet.add(port.getName());

		}

		return portSet;

	}

	private SerialPortEventListener listener;

	@Override
	public void setEventListener(SerialPortEventListener listener) {

		// TODO Auto-generated method stub

		this.listener = listener;

	}

	@Override
	public void start() {

		// TODO Auto-generated method stub

		serialInit(portNameGlob);

		try {
			port.addEventListener(listener);
		} catch (final TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		configurePort = true;
	}

	@Override
	public boolean isWorking() {

		// TODO Auto-generated method stub
		return isWorking;
	}

	private SerialIO() {

	}

	@Override
	public void stop() {

		// TODO Auto-generated method stub
		if (isWorking) {
			configureBaud = false;
			configureDatabits = false;
			configureName = false;
			configureParity = false;
			configureStopBits = false;
			configurePort = false;

			close();
			instance = new SerialIO();
		}

	}

}