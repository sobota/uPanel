package upanel.menu;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

import upanel.IO.IOAnalyzer;
import upanel.tools.ProfileManager;

public class AnalyzePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static AnalyzePanel instance = new AnalyzePanel();

	private final Preferences prefs = Preferences.userNodeForPackage(getClass());

	public static AnalyzePanel getInstance() {

		return instance;
	}

	private final GridBagConstraints constr = new GridBagConstraints();
	private final JScrollPane scrollPane = new JScrollPane();

	private TablePatternModel model = TablePatternModel.getInstance();
	private final JTable table = new JTable(model);

	private final String action[] = { "Add Time", "Add Date", "Date Time", "Empty", "Delete" };
	private final String next[] = { "Only Console", "Log To File", "Log CSV", "Stdout \\n", "Stdout" };
	private JComboBox<String> actionBox, nextBox;

	private final OkCancelPanel okCancel = OkCancelPanel.getInstance();

	public void setTableModel(final TablePatternModel modelIn) {

		this.model = modelIn;

		final TableColumnModel columnModel = table.getColumnModel();

		try {
			table.setModel(model);
		} catch (final IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, "<html>File Corupted<br>Abort");
			return;
		}

		columnModel.getColumn(2).setCellEditor(new DefaultCellEditor(actionBox));
		columnModel.getColumn(3).setCellEditor(new DefaultCellEditor(nextBox));

		model.fireTableDataChanged();
		table.repaint();
	}

	public TablePatternModel getTableModel() {

		return model;
	}

	protected void addElements() {

		constr.gridx = 0;
		constr.gridy = 0;

		actionBox = new JComboBox<String>(action);
		nextBox = new JComboBox<String>(next);

		final TableColumnModel model = table.getColumnModel();

		model.getColumn(0).setPreferredWidth(10);

		// prefs.put("TableModelPatch", as"");

		okCancel.addListenerAcceptBtn(this);

		model.getColumn(2).setCellEditor(new DefaultCellEditor(actionBox));
		model.getColumn(3).setCellEditor(new DefaultCellEditor(nextBox));

		final JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		scrollPane.setWheelScrollingEnabled(true);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(table);

		split.add(scrollPane, JSplitPane.LEFT);
		split.add(new AnalyzeSettings(), JSplitPane.RIGHT);
		split.setDividerSize(3);
		split.setDividerLocation(350);

		add(split, BorderLayout.CENTER);

		if (prefs.getBoolean("LoadTableModel", false)) {
			final String s = prefs.get("PatternPath", "");
			setTableModel((TablePatternModel) ProfileManager.loadPattern(new File(s)));

			// IOAnalyzer.getInstance().setPattern(set);
		}
	}

	private AnalyzePanel() {

		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		addElements();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (prefs.getBoolean("LoadTableModel", false)) {
			final String s = prefs.get("PatternPath", "");
			ProfileManager.savePattern(new File(s), getTableModel());
		}
	}

	public void addRowTable() {

		// TODO Auto-generated method stub
		model.addRow();

	}

	public void removeRowTable() {

		model.removeRow();
	}

}


// ////////

class AnalyzeSettings extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IOAnalyzer analyzer = IOAnalyzer.getInstance();
	private final JButton addItem = new JButton("+");
	private final JButton removeItem = new JButton("-");

	private JButton saveBtn;
	private JTextField logfileField;
	private JCheckBox endLine;

	private final Preferences prefs = Preferences.userNodeForPackage(getClass()).node("AnalyzeSettings");

	private final OkCancelPanel okCancelPanel = OkCancelPanel.getInstance();

	private void addElements() {

		final GridBagConstraints constr = new GridBagConstraints();

		constr.gridx = 0;
		constr.gridy = 0;

		okCancelPanel.addListenerAcceptBtn(this);

		addItem.addActionListener(this);
		removeItem.addActionListener(this);

		add(addItem, constr);

		constr.gridx++;
		add(removeItem, constr);

		saveBtn = new JButton("Select Logfile");
		logfileField = new JTextField(15);
		endLine = new JCheckBox("End Line \'\\n\'");

		saveBtn.addActionListener(this);

		logfileField.setText(prefs.get("logfileName", "logfile.txt"));
		analyzer.setLogFile(logfileField.getText());
		endLine.setSelected(prefs.getBoolean("logEndLine", true));

		constr.gridy = 1;

		// constr.gridwidth = 2;
		add(logfileField, constr);

		constr.gridwidth = 1;
		constr.gridx++;
		add(saveBtn, constr);

		constr.gridy++;
		add(endLine, constr);
	}

	public AnalyzeSettings() {

		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());
		addElements();
	}

	@Override
	public void actionPerformed(ActionEvent e) {// for JButton

		// TODO Auto-generated method stub

		if (e.getSource().equals(addItem)) {

			AnalyzePanel.getInstance().addRowTable();

		}

		if (e.getSource().equals(removeItem)) {
			AnalyzePanel.getInstance().removeRowTable();
		}

		if (e.getSource().equals(saveBtn)) {
			File selectedFile = null;

			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setVisible(true);

			if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(getParent())) {

				selectedFile = fileChooser.getSelectedFile();
				analyzer.setLogFile(selectedFile.getAbsolutePath());
				logfileField.setText(selectedFile.getAbsolutePath());

			}

		}

		final JButton btn = (JButton) e.getSource();

		if (btn.getText().equals("OK")) {

			analyzer.clearPattern();

			final TablePatternModel model = AnalyzePanel.getInstance().getTableModel();

			analyzer.setPattern(model.getPatternceSet());

			prefs.putBoolean("logEndLine", endLine.isSelected());
			prefs.put("logfileName", logfileField.getText());

			analyzer.setLogFile(logfileField.getText());
			analyzer.setEndLineSigh(endLine.isSelected());

		}
	}

}