package upanel.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class MainMenu extends JFrame implements Serializable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final static MainMenu instance = new MainMenu();
	protected OkCancelPanel okCancel = OkCancelPanel.getInstance();

	public static MainMenu getInstance() {

		return instance;
	}

	protected void settings() {

		add(SharedPanel.getInstance(), BorderLayout.CENTER);

		okCancel.addListenerCancelBtn(this);

		add(okCancel, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		final JButton btn = (JButton) e.getSource();

		if (btn.getName().equals("CANCEL"))
			setVisible(false);
	}

	public MainMenu() {

		// TODO Auto-generated constructor stub
		super("Settings");
		setLayout(new BorderLayout());
		settings();
		pack();
		setMinimumSize(new Dimension(620, 300));
		setSize(670, 500);
	}
}


// ///
class SharedPanel extends JSplitPane implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static SharedPanel instance = new SharedPanel();

	public static SharedPanel getInstance() {

		return instance;
	}

	protected void addElements() {

		add(new MenuSettingsPanel(), JSplitPane.LEFT);
		add(SerialSettingsPanel.getInstance(), JSplitPane.RIGHT);
		setDividerLocation(158);
		setResizeWeight(0);
		setOneTouchExpandable(true);
		setEnabled(false);
		setDividerSize(3);

		leftComponent.setMinimumSize(new Dimension(200, 120));
		rightComponent.setMinimumSize(new Dimension(300, 120));

	}

	public SharedPanel() {

		// TODO Auto-generated constructor stub
		super(JSplitPane.HORIZONTAL_SPLIT);

		setPreferredSize(new Dimension(550, 500));
		addElements();
		// setSize(getMaximumSize());
	}
}


// /

class MenuSettingsPanel extends JScrollPane implements TreeSelectionListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultMutableTreeNode topName = new DefaultMutableTreeNode("Menu");
	JTree menuTree = new JTree(topName);

	DefaultMutableTreeNode analyzeSettings = new DefaultMutableTreeNode("Analyze");
	DefaultMutableTreeNode serialSettings = new DefaultMutableTreeNode("Serial");
	DefaultMutableTreeNode sliderMainPanel = new DefaultMutableTreeNode("Slider");
	DefaultMutableTreeNode traySettingsPanel = new DefaultMutableTreeNode("Tray");
	DefaultMutableTreeNode profileManagePanel = new DefaultMutableTreeNode("Profile");
	DefaultMutableTreeNode buttonMainPanel = new DefaultMutableTreeNode("Buttons");
	DefaultMutableTreeNode patchPanel = new DefaultMutableTreeNode("Patch");

	protected JComponent addElements() {

		final DefaultMutableTreeNode genericSettingsNod = new DefaultMutableTreeNode("Other");
		topName.add(serialSettings);
		topName.add(analyzeSettings);

		final DefaultMutableTreeNode controlSettingsNod = new DefaultMutableTreeNode("Control");

		controlSettingsNod.add(sliderMainPanel);
		controlSettingsNod.add(buttonMainPanel);

		topName.add(controlSettingsNod);
		topName.add(genericSettingsNod);

		genericSettingsNod.add(traySettingsPanel);
		genericSettingsNod.add(profileManagePanel);
		genericSettingsNod.add(patchPanel);

		menuTree.addTreeSelectionListener(this);
		// getViewport().add(menuTree);

		for (int i = 0; i < menuTree.getRowCount(); i++) {
			menuTree.expandRow(i);
		}

		setViewportView(menuTree);
		return menuTree;
	}//

	public MenuSettingsPanel() {

		// TODO Auto-generated constructor stub

		setPreferredSize(new Dimension(500, 500));
		addElements();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		// TODO Auto-generated method stub

		if (menuTree.getLastSelectedPathComponent().equals(serialSettings)) {

			final SerialSettingsPanel pan = SerialSettingsPanel.getInstance();
			setRightComponent(pan);

		}
		if (menuTree.getLastSelectedPathComponent().equals(sliderMainPanel)) {

			final SliderMainPanel pan = SliderMainPanel.getInstance();
			setRightComponent(pan);

		}
		if (menuTree.getLastSelectedPathComponent().equals(analyzeSettings)) {

			final AnalyzePanel pan = AnalyzePanel.getInstance();
			setRightComponent(pan);

		}
		if (menuTree.getLastSelectedPathComponent().equals(traySettingsPanel)) {

			setRightComponent(TraySettingsPanel.getInstance());
		}
		if (menuTree.getLastSelectedPathComponent().equals(profileManagePanel)) {

			setRightComponent(ProfileManagePane.getInstance());
		}
		if (menuTree.getLastSelectedPathComponent().equals(buttonMainPanel)) {

			setRightComponent(ButtonMainPanel.getInstance());
		}
		if (menuTree.getLastSelectedPathComponent().equals(patchPanel)) {

			setRightComponent(PatchPanel.getInstance());
		}
	}

	protected void setRightComponent(Component comp) {

		final SharedPanel shared = SharedPanel.getInstance();
		final Component rightComponent = shared.getRightComponent();

		shared.remove(rightComponent);
		shared.add(comp, JSplitPane.RIGHT);
	}

}


// //
class OkCancelPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static OkCancelPanel instance;

	public static OkCancelPanel getInstance() {

		if (instance == null)
			instance = new OkCancelPanel();

		return instance;
	}

	private final JButton okBtn = new JButton("OK");
	private final JButton cancelButton = new JButton("Cancel");

	public void addListenerAcceptBtn(ActionListener listener) {

		okBtn.addActionListener(listener);
	}

	public void addListenerCancelBtn(ActionListener listener) {

		cancelButton.addActionListener(listener);
	}

	private void addElements() {

		okBtn.setName("OK");
		cancelButton.setName("CANCEL");

		add(okBtn);
		add(cancelButton);

	}

	private OkCancelPanel() {

		// TODO Auto-generated constructor stub
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		addElements();
	}

}


// /
interface NodNumber {

	int serialSettings = 0;
	int logSettings = 1;
	int profile = 2;
	int profileSaveAndLoad = 3;
	int buttonMainPanel = 4;

}
