package upanel.gui;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import upanel.IO.IOAnalyzer;
import upanel.menu.PatchPanel;
import upanel.menu.TraySettingsPanel;

class Tray implements ActionListener {

	private static Tray instance;

	private TrayIcon trayIcon;
	private static boolean trayVisible = false;

	private final Preferences pref = Preferences.userNodeForPackage(TraySettingsPanel.class).node("UISettings");

	private SystemTray tray;

	public static boolean isVisible() {

		return trayVisible;
	}

	public static void setVisible(boolean visible) {

		trayVisible = visible;
	}

	public boolean isTrayVisible() {

		return trayVisible;
	}

	public static Tray getInstance(boolean show) {

		if (instance == null) {
			instance = new Tray(show);
		} else
			setVisible(show);

		return instance;
	}

	public static Tray getInstance() {

		if (instance == null)
			instance = new Tray(trayVisible);

		return instance;
	}

	public void setDeviceLabel(final String label) {

		final StringBuilder trayHints = new StringBuilder();
		trayHints.append("Show uPanel  Active: ");
		trayHints.append(label);
		try {
			trayIcon.setToolTip(trayHints.toString());
		} catch (final NullPointerException e) {}
	}

	public void setDefaultLabel() {

		try {
			trayIcon.setToolTip("Show uPanel");

		} catch (final NullPointerException e) {}
	}

	@Override
	public String toString() {

		// TODO Auto-generated method stub

		return "Visible " + trayVisible + " Supported " + SystemTray.isSupported();
	}

	private void trayCreate() {

		if (SystemTray.isSupported()) {

			pref.putBoolean("isSupported", true);

			final URL url = getClass().getResource("/Icon/TrayIcon.png");
			final ImageIcon icon = new ImageIcon(url);

			final PopupMenu menu = new PopupMenu();
			trayIcon = new TrayIcon(icon.getImage(), "Show uPanel");

			tray = SystemTray.getSystemTray();

			trayIcon.addMouseListener(new TrayMouseListener());
			menuConf(menu);

			try {
				tray.add(trayIcon);
			} catch (final AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			trayIcon.setPopupMenu(menu);
		} else
			pref.putBoolean("isSupported", false);

	}

	private void menuConf(PopupMenu menu) {

		final MenuItem itemMenu[] = { new MenuItem("Show uPanel"), new MenuItem("Open Log File") };
		final MenuItem exitItem = new MenuItem("Exit");

		for (int i = 0; i < itemMenu.length; i++) {
			menu.add(itemMenu[i]);
			itemMenu[i].addActionListener(this);
		}

		exitItem.addActionListener(this);
		menu.addSeparator();
		menu.add(exitItem);
	}

	private Tray(boolean visible) {

		// TODO Auto-generated constructor stub
		Tray.trayVisible = visible;

		if (trayVisible)
			trayCreate();
	}

	public static void openEditor() {

		final Preferences localPrefs = Preferences.userNodeForPackage(PatchPanel.class);

		if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT) && (localPrefs.get("DefaultEditor", null) == null)) {

			final JFileChooser chooser = new JFileChooser();
			chooser.setName("Select Editor Name");
			final int val = chooser.showDialog(MainGui.getInstance(), "Select Editor");

			if (val == JFileChooser.APPROVE_OPTION) {

				final File defEdit = chooser.getSelectedFile();
				localPrefs.put("DefaultEditor", defEdit.getAbsolutePath());
			}
		}

		if (Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {

			try {
				Desktop.getDesktop().edit(new File(IOAnalyzer.getInstance().getLogFileName()));
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (localPrefs.get("DefaultEditor", null) != null) {

			final String editor = localPrefs.get("DefaultEditor", null);
			final StringBuilder editorAndFile = new StringBuilder(editor);
			editorAndFile.append(" ");
			editorAndFile.append(IOAnalyzer.getInstance().getLogFileName());

			try {
				Runtime.getRuntime().exec(editorAndFile.toString());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {// for menu Item

		// TODO Auto-generated method stub

		final MenuItem item = (MenuItem) e.getSource();
		final String s = item.getLabel();

		switch (s) {

			case "Show uPanel":
				MainGui.getInstance().setVisible(true);
				break;
			case "Open Log File":
				openEditor();
				break;

			case "Exit":
				final int selection = JOptionPane
					.showConfirmDialog(MainGui.getInstance(), "Are you sure you want to quit", "Exit", JOptionPane.YES_NO_OPTION);

				if (selection == JOptionPane.OK_OPTION) {
					IOAnalyzer.getInstance().stop();
					System.exit(0);
				}
		}
	}

}


// //////
class TrayMouseListener extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent e) {

		final MainGui gui = MainGui.getInstance();

		if (e.getClickCount() == 2) {

			if (!gui.isVisible())
				gui.setVisible(true);
		} else {
			// System.out.println(gui);
			gui.setVisible(false);
		}
	}

}