package upanel.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import upanel.gui.MainGui;
import upanel.tools.ProfileManager;

class ProfileManagePane extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Preferences pref = Preferences.userNodeForPackage(MainGui.class);

	private static ProfileManagePane instance = new ProfileManagePane();

	static ProfileManagePane getInstance() {

		return instance;
	}

	private final JButton saveBtn = new JButton("Save Profile");
	private final JButton loadBtn = new JButton("Load Profile");
	private final JTextField profileName = new ProfileName();

	private final JCheckBox patternBox = new JCheckBox("Pattern");
	private final JCheckBox settingsBox = new JCheckBox("Settings");

	protected void addElements() {

		final GridBagConstraints constr = new GridBagConstraints();

		setNameForProfile(constr);

		saveBtn.addActionListener(this);
		loadBtn.addActionListener(this);

		add(patternBox);
		add(settingsBox);

		constr.gridx = 0;
		constr.gridy = 1;

		add(patternBox, constr);

		constr.gridy++;
		settingsBox.setSelected(true);
		add(settingsBox, constr);

		constr.gridwidth = 2;

		constr.gridy = 3;

		add(saveBtn, constr);

		constr.gridy = 4;

		add(loadBtn, constr);

	}

	private void setNameForProfile(GridBagConstraints constr) {

		add(new JLabel("Profile Name "), constr);

		add(profileName, constr);

	}

	private ProfileManagePane() {

		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());
		addElements();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File((Preferences.userNodeForPackage(getClass()).get("homeDir", ""))));

		// chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {

				// TODO Auto-generated method stub
				return ".uprof";
			}

			@Override
			public boolean accept(File f) {

				// TODO Auto-generated method stub
				return f.getName().endsWith(".uprof");
			}
		});

		if (!settingsBox.isSelected() && !patternBox.isSelected())
			settingsBox.setSelected(true);

		if (e.getSource().equals(saveBtn)) {// //save

			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {// save

				File s = chooser.getSelectedFile();
				// profile
				pref.put("ProfileName", profileName.getText());

				final String fileExt = chooser.getFileFilter().getDescription();

				if (s.exists()) {

					if (patternBox.isSelected() && settingsBox.isSelected())
						ProfileManager.save(s, AnalyzePanel.getInstance().getTableModel(), pref);

					if (patternBox.isSelected()) {
						ProfileManager.reSave(s, AnalyzePanel.getInstance().getTableModel());
					}

					if (settingsBox.isSelected()) {
						ProfileManager.reSave(s, pref);
					}
				} else {

					if (!s.getName().endsWith(fileExt))
						s = new File(s.getAbsolutePath() + fileExt);

					if (patternBox.isSelected() && settingsBox.isSelected())
						ProfileManager.save(s, AnalyzePanel.getInstance().getTableModel(), pref);

					if (patternBox.isSelected() && !settingsBox.isSelected()) {
						ProfileManager.savePattern(s, AnalyzePanel.getInstance().getTableModel());
					}

					if (settingsBox.isSelected() && !patternBox.isSelected()) {
						ProfileManager.saveProfile(s);
					}
				}
			}// ////////////////////////////////////////////////////////////////////////////
		}
		if (e.getSource().equals(loadBtn)) {

			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				final File s2 = chooser.getSelectedFile();

				if (patternBox.isSelected() && settingsBox.isSelected()) {
					ProfileManager.load(s2, pref);

					pref.put("PatternPath", s2.getAbsolutePath());
					pref.putBoolean("LoadTableModel", true);
				}
				if (patternBox.isSelected() && !settingsBox.isSelected()) {
					final TablePatternModel pat = (TablePatternModel) ProfileManager.loadPattern(s2);

					AnalyzePanel.getInstance().setTableModel(pat);

					pref.put("PatternPath", s2.getAbsolutePath());
					pref.putBoolean("LoadTableModel", true);
				}
				if (settingsBox.isSelected() && !patternBox.isSelected()) {
					ProfileManager.loadProfile(s2);

				}
				profileName.setText(pref.get("ProfileName", "[Default]"));

			}

		}

	}
}


// /////
class ProfileName extends JTextField implements DocumentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Preferences prefs = Preferences.userNodeForPackage(getClass());

	public ProfileName() {

		// TODO Auto-generated constructor stub
		super(10);
		this.setText(prefs.get("ProfileName", " default"));
		getDocument().addDocumentListener(this);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void insertUpdate(DocumentEvent e) {

		// TODO Auto-generated method stub

		prefs.put("ProfileName", this.getText());
	}

	@Override
	public void removeUpdate(DocumentEvent e) {

		// TODO Auto-generated method stub

		prefs.put("ProfileName", this.getText());
	}
}
