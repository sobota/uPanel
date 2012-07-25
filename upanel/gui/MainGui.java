package upanel.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import upanel.IO.IOAnalyzer;
import upanel.menu.MainMenu;
import upanel.menu.TraySettingsPanel;

public class MainGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final static MainGui instance = new MainGui();

	protected MainMenu menu;

	protected void addElements() {

		addWindowListener(new WindowsCloser());

		setJMenuBar(new MenuBar());

		add(MoveBtnPanel.getInstance());
		add(SliderPanel.getInstance());

	}

	public static MainGui getInstance() {

		return instance;
	}

	private MainGui() {

		super("uPanel ");

		menu = MainMenu.getInstance();// init menu
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		addElements();
		pack();
		setSize(400, 300);
		setLocation(500, 600);
		setMinimumSize(getMinimumSize());

	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		final Preferences trayPref = Preferences.userNodeForPackage(TraySettingsPanel.class).node("UISettings");

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// TODO Auto-generated method stub

				if (trayPref.getBoolean("TrayEnableOnStart", true)) {

					Tray.getInstance(true);

					instance.setVisible(true);
				}
				if (trayPref.getBoolean("StartHidden", false)) {
					Tray.getInstance(true);
					instance.setVisible(false);
				}

				try {

					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (final ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

}


// ////////////

class MenuBar extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JMenu settingsMenu;
	protected JMenu portMenu;
	protected JMenu aboutMenu;
	protected JMenu toolsMenu;

	protected JMenuItem settingsItem[] = { new JMenuItem("Preferences"), new JMenuItem("Exit") };
	protected JMenuItem aboutItem[] = { new JMenuItem("About uPanel") };
	protected JMenuItem toolItem[] = { new JMenuItem("Open Console"), new JMenuItem("Open Log") };

	protected void addElements() {

		portMenu = PortMenu.getInstance("Port Menu");
		settingsMenu = new JMenu("Settings");
		aboutMenu = new JMenu("About");
		toolsMenu = new JMenu("Tools");

		add(settingsMenu);
		add(toolsMenu);
		add(portMenu);
		add(aboutMenu);
	}

	protected void addItem() {

		for (final JMenuItem item : settingsItem) {

			item.addActionListener(this);
		}

		settingsMenu.add(settingsItem[0]);
		settingsMenu.addSeparator();
		settingsMenu.add(settingsItem[1]);

		aboutItem[0].addActionListener(this);
		aboutMenu.add(aboutItem[0]);

		toolItem[0].addActionListener(this);
		toolItem[1].addActionListener(this);
		toolsMenu.add(toolItem[0]);
		toolsMenu.add(toolItem[1]);
	}

	//

	public MenuBar() {

		// TODO Auto-generated constructor stub
		super();
		addElements();
		addItem();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub

		final JMenuItem item = (JMenuItem) e.getSource();
		final String s = item.getText();
		switch (s) {

			case "Exit":
				IOAnalyzer.getInstance().stop(); // System
				System.exit(0); // dodac
								// zamukanie
								// port??????w
				break;

			case "Preferences":
				MainGui.getInstance().menu.setVisible(true);
				break;

			case "About uPanel":
				AboutDialog.getInstance().setVisible(true);
				break;

			case "Open Console":

				LogMonitor.getInstance().setVisible(true);
				break;
			case "Open Log":
				Tray.openEditor();
		}
	}

}


// //
class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AboutDialog instance;

	public static AboutDialog getInstance() {

		if (instance == null)
			instance = new AboutDialog();

		return instance;
	}

	private void addElements() {

		final JLabel aboutLabel = new JLabel(
			"<html>Micro Panel is small program for created fast prototype and testing.<br><br>This program is develop by Sobota in free time.<br> Author does not provide any warrant.<br><br>GPL license/ BeerWare");
		aboutLabel.setAlignmentX(80);
		aboutLabel.setAlignmentY(80);
		add(aboutLabel);
		setLocation(new Point(getParent().getLocation().x, getParent().getLocation().y + 70));
	}

	private AboutDialog() {

		// TODO Auto-generated constructor stub
		super(MainGui.getInstance());
		setTitle("uPanel v0.95 stable");
		addElements();
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		pack();
	}
}


// /////
class WindowsCloser extends WindowAdapter {

	private final Preferences pref = Preferences.userNodeForPackage(TraySettingsPanel.class).node("UISettings");

	@Override
	public void windowClosing(WindowEvent e) {// when window is closing

		if (pref.get("CloseAction", "Exit on close").equals("Exit on close")) {

			IOAnalyzer.getInstance().stop();
			System.exit(0);
		} else {

			MainGui.getInstance().setVisible(false);
		}
	}
}
