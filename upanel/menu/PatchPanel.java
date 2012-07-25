package upanel.menu;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PatchPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static PatchPanel instance = new PatchPanel();

	public static PatchPanel getInstance() {

		return instance;
	}

	private final JLabel textEditor = new JLabel();
	private final JLabel homeDirectory = new JLabel();

	private void addElements() {

		final GridBagConstraints constr = new GridBagConstraints();
		constr.gridwidth = 4;

		final JButton setEditor = new JButton("Text Editor");
		final JButton setHomeDirectory = new JButton("Home Directory");

		setEditor.setName("editorBtn");
		setHomeDirectory.setName("homeBtn");

		setEditor.addActionListener(this);
		setHomeDirectory.addActionListener(this);

		textEditor.setText(Preferences.userNodeForPackage(getClass()).get("DefaultEditor", ""));
		homeDirectory.setText(Preferences.userNodeForPackage(getClass()).get("homeDir", ""));

		add(setEditor, constr);
		constr.gridy += 2;
		add(textEditor, constr);
		constr.gridy++;
		add(setHomeDirectory, constr);
		constr.gridy += 2;
		add(homeDirectory, constr);
	}

	private PatchPanel() {

		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());
		addElements();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		final JFileChooser chooser = new JFileChooser();
		final Component btn = (Component) arg0.getSource();

		final String name = btn.getName();
		String s = "";

		switch (name) {

			case "homeBtn":
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (chooser.showDialog(MainMenu.getInstance(), "Select") == JFileChooser.APPROVE_OPTION) {
					s = chooser.getSelectedFile().getAbsolutePath();
				}

				homeDirectory.setText(s);
				Preferences.userNodeForPackage(getClass()).put("homeDir", s);
				break;

			case "editorBtn":
				if (chooser.showDialog(MainMenu.getInstance(), "Select") == JFileChooser.APPROVE_OPTION) {

					s = chooser.getSelectedFile().getAbsolutePath();
				}

				textEditor.setText(s);
				Preferences.userNodeForPackage(getClass()).put("DefaultEditor", s);
				break;

		}
	}

}
