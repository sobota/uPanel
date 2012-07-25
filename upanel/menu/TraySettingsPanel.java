package upanel.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TraySettingsPanel extends JPanel implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TraySettingsPanel instance = new TraySettingsPanel();
	private final Preferences pref = Preferences.userNodeForPackage(getClass()).node("UISettings");
	private boolean trayEnable = true, startHidden = false;
	private final GridBagConstraints constr = new GridBagConstraints();

	public static TraySettingsPanel getInstance() {

		return instance;
	}

	private final OkCancelPanel okCancelPan = OkCancelPanel.getInstance();
	private JCheckBox startInTray;

	private final JCheckBox trayCheck = new JCheckBox("Show In Tray");

	private void checkTraySupport() {

		if (SystemTray.isSupported()) {

			trayCheck.setSelected(pref.getBoolean("TrayEnableOnStart", true));
			trayCheck.addItemListener(this);
			add(trayCheck, constr);
			constr.gridy++;

		} else
			add(new JLabel("<html><b>Tray Unsupported!"), constr);
	}

	private void startInTray() {

		startInTray = new JCheckBox("Start Hidden in Tray");
		startInTray.setSelected(pref.getBoolean("StartHidden", false));

		if (pref.getBoolean("TrayEnableOnStart", false) || pref.getBoolean("StartHidden", false))
			trayCheck.setSelected(true);

		startInTray.addItemListener(this);

		constr.gridy++;
		add(startInTray, constr);
	}

	private void setForOkCancel() {

		okCancelPan.addListenerAcceptBtn(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// TODO Auto-generated method stub

				pref.putBoolean("TrayEnableOnStart", trayEnable);
				pref.putBoolean("StartHidden", startHidden);
				pref.put("CloseAction", defaultCloseOperations.getSelectedItem().toString());

			}
		});
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		// TODO Auto-generated method stub
		final JCheckBox checkBox = (JCheckBox) e.getSource();

		switch (checkBox.getText()) {

			case "Start Hidden in Tray":

				if (e.getStateChange() == ItemEvent.SELECTED) {

					startHidden = true;
					pref.putBoolean("StartHidden", startHidden);
					pref.putBoolean("TrayEnableOnStart", true);
					startInTray.setSelected(true);

				} else {

					startHidden = false;
					pref.putBoolean("StartHidden", startHidden);
				}
				break;

			case "Show In Tray":

				if (e.getStateChange() == ItemEvent.SELECTED) {

					trayEnable = true;
				} else {
					trayEnable = false;
				}
				break;
		}

	}

	private final String closeStr[] = { "Exit on close", "Hide to tray" };
	private final JComboBox<String> defaultCloseOperations = new JComboBox<String>(closeStr);

	private void exitOperations() {

		defaultCloseOperations.setSelectedItem(pref.get("CloseAction", "CloseAction"));

		constr.gridy++;
		add(new JLabel("Exit Operations"), constr);
		constr.gridy++;
		add(defaultCloseOperations, constr);
	}

	private TraySettingsPanel() {

		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());
		checkTraySupport();
		setForOkCancel();
		startInTray();
		exitOperations();
	}

}
