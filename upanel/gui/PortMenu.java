package upanel.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import upanel.IO.IOAnalyzer;
import upanel.IO.SerialIO;
import upanel.IO.WiredDevice;
import upanel.menu.TablePatternModel;
import upanel.menu.TraySettingsPanel;
import upanel.tools.ProfileManager;

class PortMenu extends JMenu implements ItemListener, MenuListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static PortMenu instance = new PortMenu();
	private final IOAnalyzer analyzer = IOAnalyzer.getInstance();
	private final WiredDevice serial = SerialIO.getInstance();

	public static PortMenu getInstance(String title) {

		instance.setText(title);
		return instance;
	}

	public static PortMenu getInstance() {

		return instance;
	}

	protected void configureMenu() {

		addMenuListener(this);

	}

	private Set<String> portSet;
	private Iterator<String> portIter;
	private final ButtonGroup btnGroup = new ButtonGroup();
	private String selectedItem = "";
	private final JCheckBoxMenuItem closeDevice = new JCheckBoxMenuItem("Stop");

	private final Preferences trayPref = Preferences.userNodeForPackage(TraySettingsPanel.class).node("UISettings");
	private final Tray tray = Tray.getInstance(trayPref.getBoolean("TrayEnableOnStart", true));

	private boolean portSeleceted = false;

	private void addElements() {

		portSet = serial.getPortList();
		portIter = portSet.iterator();

		JRadioButtonMenuItem radioitem;

		while (portIter.hasNext()) {

			final String s = portIter.next();
			radioitem = new JRadioButtonMenuItem(s);
			btnGroup.add(radioitem);

			if (s.equals(selectedItem)) {
				radioitem.setSelected(true);

			}
			radioitem.addItemListener(this);

			add(radioitem);
		}
		addSeparator();

		btnGroup.add(closeDevice);

		closeDevice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {

				// TODO Auto-generated method stub
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					analyzer.stop();
					portSeleceted = false;
					tray.setDefaultLabel();
				}

			}
		});

		add(closeDevice);
	}

	private PortMenu() {

		addElements();
		configureMenu();
	}

	protected void refreshStateItem() {

		if (!serial.isWorking()) {

			portSet.addAll(serial.getPortList());
			removeAll();

			addElements();
		}
	}

	// Item Listener
	@Override
	public void itemStateChanged(ItemEvent e) {

		// TODO Auto-generated method stub
		final JRadioButtonMenuItem radio = (JRadioButtonMenuItem) e.getItem();

		if (radio.isSelected()) {

			if (portSeleceted) {
				// close
				analyzer.stop();
				tray.setDefaultLabel();
			}

			portSeleceted = true;

			selectedItem = radio.getText();

			tray.setDeviceLabel(selectedItem);
			serial.setPortName(radio.getText());
			analyzer.setWiredDevice(serial);

			final Preferences prefs = Preferences.userNodeForPackage(MainGui.class);

			TablePatternModel model = null;

			if (prefs.getBoolean("LoadTableModel", false)) {

				final String s = Preferences.userNodeForPackage(getClass()).get("PatternPath", "");
				model = (TablePatternModel) ProfileManager.loadPattern(new File(s));

				analyzer.setPattern(model.getPatternceSet());

				analyzer.setEndLineSigh(prefs.getBoolean("logEndLine", true));
			}

			serial.start();
		}
	}

	// Menu Listener
	@Override
	public void menuCanceled(MenuEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void menuSelected(MenuEvent e) {

		// TODO Auto-generated method stub

		refreshStateItem();

	}

}