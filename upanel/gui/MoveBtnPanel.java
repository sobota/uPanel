package upanel.gui;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JPanel;

import upanel.IO.IOAnalyzer;
import upanel.menu.ButtonTableModel;
import upanel.menu.SliderMainPanel;

public class MoveBtnPanel extends JPanel implements ActionListener { // przyciski

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JButton button[] = { new JButton("Upper Button"), new JButton("Left Button"), new JButton("Right Button"), new JButton("Down Button") };

	private final IOAnalyzer analyzer = IOAnalyzer.getInstance();
	private final Preferences prefs = Preferences.userNodeForPackage(SliderMainPanel.class).node("ButtonMainPanel");
	private final String commandVal[] = new String[7];

	private final JButton downbtn[] = { new JButton("Dw Left"), new JButton("Dw Middle"), new JButton("Dw Right") };

	private static MoveBtnPanel instance = new MoveBtnPanel();

	public static MoveBtnPanel getInstance() {

		return instance;
	}

	protected ButtonTableModel tableModel = ButtonTableModel.getInstance();

	protected void addElements() {

		for (int i = 0; i < button.length; i++) {

			final String s = prefs.get("key" + i, (String) tableModel.getValueAt(i, 3));

			if (!s.isEmpty())
				button[i].setMnemonic(s.charAt(0));

			commandVal[i] = prefs.get("command" + i, (String) tableModel.getValueAt(i, 1));
			button[i].setToolTipText("<html>Key " + s + "<br>Command " + commandVal[i]);

			button[i].setMargin(new Insets(5, 5, 5, 5));
			button[i].addActionListener(this);
			add(new Label(""));
			button[i].setName(button[i].getText());
			button[i].setText(prefs.get("label" + i, (String) tableModel.getValueAt(i, 2)));
			add(button[i]);
		}
		add(new Label(""));
		// //

		JButton btn;
		for (int j = 0; j < downbtn.length; j++) {

			final int pos = j + 4;
			btn = downbtn[j];
			btn.setName(btn.getText());

			btn.setText(prefs.get("label" + pos, (String) tableModel.getValueAt(pos, 2)));
			final String s = prefs.get("key" + pos, (String) tableModel.getValueAt(pos, 3));

			if (!s.isEmpty())
				btn.setMnemonic(s.charAt(0));

			commandVal[pos] = prefs.get("command" + pos, (String) tableModel.getValueAt(pos, 1));
			btn.setToolTipText("<html>Key " + s + "<br>Command " + commandVal[pos]);

			btn.addActionListener(this);

			add(btn);
		}
	}

	public void reloadData() {

		for (int i = 0; i < button.length; i++) {

			final String s = prefs.get((String) tableModel.getValueAt(i, 3), (String) tableModel.getValueAt(i, 3));

			if (!s.isEmpty())
				button[i].setMnemonic(s.charAt(0));

			commandVal[i] = prefs.get((String) tableModel.getValueAt(i, 1), (String) tableModel.getValueAt(i, 1));
			button[i].setToolTipText("<html>Key " + s + "<br>Command " + commandVal[i]);

			button[i].setText(prefs.get((String) tableModel.getValueAt(i, 2), (String) tableModel.getValueAt(i, 2)));

		}

		// { "", "Command", "Label", "Key" };
		// {{ "Up", "A", "UP", "W" },
		// { "Left", "L", "LF", "A" },
		// { "Right", "D", "RG", "D" },
		// { "Down", "S", "DW", "S" },
		// {"Dw Left","X", "DL", "Q" },
		// {"Dw Middle","Y", "DM", "Z" },
		// {"Dw Right","Z", "DR", "E" }};

		// //
		JButton btn;
		for (int j = 0; j < downbtn.length; j++) {

			final int pos = j + 4;
			btn = downbtn[j];

			btn.setText(prefs.get((String) tableModel.getValueAt(pos, 2), (String) tableModel.getValueAt(pos, 2)));
			final String s = prefs.get((String) tableModel.getValueAt(pos, 3), (String) tableModel.getValueAt(pos, 3));

			if (!s.isEmpty())
				btn.setMnemonic(s.charAt(0));

			commandVal[pos] = prefs.get((String) tableModel.getValueAt(pos, 1), (String) tableModel.getValueAt(pos, 1));
			btn.setToolTipText("<html>Key " + s + "<br>Command " + commandVal[pos]);

		}

		revalidate();
		repaint();
	}

	private MoveBtnPanel() {

		// TODO Auto-generated constructor stub
		setLayout(new GridLayout(4, 3, 18, 18));
		addElements();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final JButton btn = (JButton) e.getSource();
		final String s = btn.getName();

		switch (s) {

			case "Upper Button":
				analyzer.writeMessage(commandVal[0]);
				break;
			case "Left Button":
				analyzer.writeMessage(commandVal[1]);
				break;
			case "Right Button":
				analyzer.writeMessage(commandVal[2]);
				break;
			case "Down Button":
				analyzer.writeMessage(commandVal[3]);
				break;
			case "Dw Left":
				analyzer.writeMessage(commandVal[4]);
				break;
			case "Dw Middle":
				analyzer.writeMessage(commandVal[5]);
				break;
			case "Dw Right":
				analyzer.writeMessage(commandVal[6]);
		}

	}
}
