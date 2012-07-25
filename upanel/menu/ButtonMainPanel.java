package upanel.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import upanel.gui.MoveBtnPanel;

class ButtonMainPanel extends JScrollPane implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ButtonMainPanel instance = new ButtonMainPanel();
	private final OkCancelPanel okCancelPane = OkCancelPanel.getInstance();

	private final Preferences pref = Preferences.userNodeForPackage(SliderMainPanel.class).node("ButtonMainPanel");

	private final ButtonTableModel model = ButtonTableModel.getInstance();

	public static ButtonMainPanel getInstance() {

		return instance;
	}

	private void addElements() {

		final JTable table = new JTable(model);

		okCancelPane.addListenerAcceptBtn(this);

		for (int j = 0; j < model.getRowCount(); j++) {

			table.setValueAt(pref.get("command" + j, (String) model.getValueAt(j, 1)), j, 1);
			table.setValueAt(pref.get("label" + j, (String) model.getValueAt(j, 2)), j, 2);
			table.setValueAt(pref.get("key" + j, (String) model.getValueAt(j, 3)), j, 3);
		}
		MoveBtnPanel.getInstance().reloadData();

		setViewportView(table);

	}

	private ButtonMainPanel() {

		addElements();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// TODO Auto-generated method stub

		for (int j = 0; j < model.getRowCount(); j++) {

			final String data[] = model.getRowData(j);

			pref.put("command" + j, data[1]);
			pref.put("label" + j, data[2]);
			pref.put("key" + j, data[3]);

		}

	}

}
