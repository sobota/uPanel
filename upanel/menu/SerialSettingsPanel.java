package upanel.menu;

import gnu.io.SerialPort;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import upanel.IO.SerialIO;

public class SerialSettingsPanel extends JPanel implements ActionListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static SerialSettingsPanel instance = new SerialSettingsPanel();

	private final Preferences pref = Preferences.userNodeForPackage(getClass()).node("SerialSettings");

	public static SerialSettingsPanel getInstance() {

		return instance;
	}

	private final String stopBits[] = { "Bits 1", "Bits 1 5", "Bits 2" };
	private final Integer stopValue[] = { SerialPort.STOPBITS_1, SerialPort.STOPBITS_1_5, SerialPort.STOPBITS_2 };
	private final Map<String, Integer> stopMap = new HashMap<String, Integer>();

	private final String parity[] = { "None", "Even", "Mark", "Odd", "Space" };
	private final Integer parityValue[] = { SerialPort.PARITY_NONE, SerialPort.PARITY_EVEN, SerialPort.PARITY_MARK, SerialPort.PARITY_ODD, SerialPort.PARITY_SPACE };
	private final Map<String, Integer> parityMap = new HashMap<String, Integer>();

	private final String dataBits[] = { "Data 5", "Data 6", "Data 7", "Data 8", };
	private final Integer dataValue[] = { SerialPort.DATABITS_5, SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8 };
	private final Map<String, Integer> dataMap = new HashMap<String, Integer>();

	private final Integer baudrate[] = { 300, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200 };
	private final String defineOwn = "Define Own";

	private final List<Integer> baudList = new LinkedList<Integer>(Arrays.asList(baudrate));

	private final JComboBox comboBox[] = { new JComboBox(), new JComboBox(stopBits), new JComboBox(dataBits), new JComboBox(parity) };

	private final OkCancelPanel okCancel = OkCancelPanel.getInstance();

	private void addSerialItem() {

		final Iterator<Integer> iter = baudList.iterator();

		while (iter.hasNext()) {
			comboBox[0].addItem(iter.next());
		}

		comboBox[0].addItem(defineOwn);
	}

	protected void addElements() {

		addSerialItem();

		final JLabel infoLabel[] = { new JLabel("Baudrate"), new JLabel("Stop Bits"), new JLabel("Data Bits"), new JLabel("Parity Bits") };
		final GridBagConstraints constr = new GridBagConstraints();

		for (int c = 0; c < parity.length; c++) {

			parityMap.put(parity[c], parityValue[c]);
		}

		for (int k = 0; k < stopBits.length; k++) {

			stopMap.put(stopBits[k], stopValue[k]);
		}

		for (int z = 0; z < dataBits.length; z++) {

			dataMap.put(dataBits[z], dataValue[z]);
		}

		constr.gridx = 0;
		constr.gridy = 0;

		for (int j = 0; j < comboBox.length; j++) {
			constr.gridx++;
			add(infoLabel[j], constr);
		}

		constr.gridy = 1;
		constr.gridx = 0;

		comboBox[0].addActionListener(this);

		for (int i = 0; i < comboBox.length; i++) {
			constr.gridx++;

			switch (i) {
				case 0:
					final Integer s = new Integer(pref.get("baudrate", "9600"));

					if (!baudList.contains(s)) {

						baudList.add(s);
						Collections.sort(baudList);
						comboBox[0].removeAllItems();
						addSerialItem();
					}

					comboBox[i].setSelectedItem(s);
					// port.setSerialPortParams(9600, SerialPort.DATABITS_8,
					// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					break;
				case 1:
					comboBox[i].setSelectedItem(pref.get("stopBits", stopBits[0]));
					break;
				case 2:
					comboBox[i].setSelectedItem(pref.get("dataBits", dataBits[3]));
					break;
				case 3:
					comboBox[i].setSelectedItem(pref.get("parity", parity[0]));
					break;
			}
			add(comboBox[i], constr);
		}

		final ActionListener okCancelListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				final JButton btn = (JButton) e.getSource();

				if ("OK".equals(btn.getName())) {

					SerialIO.getInstance().setPortParms((Integer) comboBox[0].getSelectedItem(), dataMap.get(comboBox[2].getSelectedItem()), stopMap
						.get(comboBox[1].getSelectedItem()), parityMap.get(comboBox[3].getSelectedItem()));

					pref.put("baudrate", comboBox[0].getSelectedItem().toString());
					pref.put("dataBits", (String) comboBox[2].getSelectedItem());
					pref.put("stopBits", (String) comboBox[1].getSelectedItem());
					pref.put("parity", (String) comboBox[3].getSelectedItem());

					pref.putInt("baudratePort", (Integer) comboBox[0].getSelectedItem());
					pref.putInt("dataBitsPort", dataMap.get(comboBox[2].getSelectedItem()));
					pref.putInt("stopBitsPort", stopMap.get(comboBox[1].getSelectedItem()));
					pref.putInt("parityPort", parityMap.get(comboBox[3].getSelectedItem()));

				}
			}
		};

		okCancel.addListenerAcceptBtn(okCancelListener);
		okCancel.addListenerCancelBtn(okCancelListener);

	}

	private SerialSettingsPanel() {

		// TODO Auto-generated constructor stub

		setLayout(new GridBagLayout());
		addElements();
	}

	protected static class BaudrateDialog extends JDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static BaudrateDialog instance;
		private static NumberFormat format = NumberFormat.getInstance();

		final static JButton addBtn = new JButton("Add");
		static final JFormattedTextField baudField = new JFormattedTextField(format);

		static BaudrateDialog getInstance(Point p, ActionListener listener) {

			if (instance == null)
				instance = new BaudrateDialog();
			addBtn.addActionListener(listener);
			instance.setLocation(p);
			return instance;
		}

		private BaudrateDialog() {

			// TODO Auto-generated constructor stub

			format.setGroupingUsed(false);
			baudField.setColumns(10);
			setModal(true);
			setSize(200, 60);
			setTitle("Set Own Baud");
			setLayout(new FlowLayout());
			add(baudField);
			add(addBtn);
			setVisible(false);

		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// TODO Auto-generated method stub
		final JComboBox<String> c = (JComboBox<String>) arg0.getSource();
		String str = "";
		if (c.getItemCount() > 0) {
			str = c.getSelectedItem().toString();
		}
		if (c.equals(comboBox[0])) {
			if (str.matches(defineOwn)) {

				BaudrateDialog.getInstance(getLocation(), new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						// TODO Auto-generated method stub
						final String s = BaudrateDialog.baudField.getText();
						final Integer asInt = new Integer(s);

						if (!baudList.contains(asInt) && !s.isEmpty()) {

							baudList.add(asInt);
							Collections.sort(baudList);
							comboBox[0].removeAllItems();

							addSerialItem();
						}
					}
				}).setVisible(true);

				c.repaint();
			}
		}

	}

}