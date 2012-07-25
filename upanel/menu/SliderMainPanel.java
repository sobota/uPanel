package upanel.menu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import upanel.gui.SliderPanel;

public class SliderMainPanel extends JPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final OkCancelPanel okCancel = OkCancelPanel.getInstance();

	private static SliderMainPanel instance = new SliderMainPanel();

	public static SliderMainPanel getInstance() {

		return instance;
	}

	private final GridBagConstraints constr = new GridBagConstraints();

	private void setSliderData() {

		constr.gridy++;
		constr.gridwidth = 4;

		final SpinnerModel sp1[] = { new SpinnerNumberModel(20, 0, 100, 1), new SpinnerNumberModel(80, 0, 100, 1) };
		final SpinnerModel sp2[] = { new SpinnerNumberModel(20, 0, 100, 1), new SpinnerNumberModel(80, 0, 100, 1) };

		add(new SpinnerPanel("Slider 1", sp1), constr);
		constr.gridy++;
		add(new SpinnerPanel("Slider 2", sp2), constr);

	}

	private SliderMainPanel() {

		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());
		setSliderData();

	}

}


// //
class MessagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Preferences pref = Preferences.userNodeForPackage(SliderMainPanel.class).node("ButtonMainPanel");
	private final JTextField msg = new JTextField(10);

	private void addElements() {

		final JLabel label = new JLabel("<html> Message Format<br> Token + Value");
		msg.setToolTipText("<html> Set Token<br>Value is get from slider");

		add(label);
		add(msg);
	}

	public void setToken(String token) {

		msg.setText(token);
	}

	public String getToken() {

		return msg.getText();
	}

	public MessagePanel() {

		// TODO Auto-generated constructor stub
		addElements();
	}

}


// ///
class SpinnerPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Preferences pref = Preferences.userNodeForPackage(SliderMainPanel.class).node("ButtonMainPanel");
	private final OkCancelPanel okCancel = OkCancelPanel.getInstance();
	private String label;

	private TextSpinner min, max;
	private MessagePanel message;

	private void addElements(String sliderLabel, SpinnerModel model[]) {

		final JSpinner spinner[] = { new JSpinner(model[0]), new JSpinner(model[1]) };

		okCancel.addListenerAcceptBtn(this);

		label = sliderLabel;

		final GridBagConstraints constr = new GridBagConstraints();

		constr.gridwidth = 2;
		constr.gridx = 1;

		add(new JLabel(sliderLabel), constr);

		constr.gridy++;
		constr.gridx = 0;
		constr.gridy++;

		min = new TextSpinner(spinner[0], "Min Value");

		add(min, constr);
		constr.gridy++;

		max = new TextSpinner(spinner[1], "Max Value");

		add(max, constr);

		constr.gridy++;

		message = new MessagePanel();
		add(message, constr);

		spinner[0].setValue(pref.getInt(label + "Min", 20));
		spinner[1].setValue(pref.getInt(label + "Max", 80));
		message.setToken(pref.get(label + "Token", label.trim()));

	}

	public SpinnerPanel(String sliderLabel, SpinnerModel model[]) {

		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addElements(sliderLabel, model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		pref.putInt(label + "Min", (Integer) min.getValue());
		pref.putInt(label + "Max", (Integer) max.getValue());
		pref.put(label + "Token", message.getToken());
		SliderPanel.getInstance().reloadData(new Integer(max.getValue().toString()), new Integer(min.getValue().toString()), label, message
			.getToken());
	}
}


// ///
class TextSpinner extends JComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel label = new JLabel();
	protected JSpinner spinner;

	public JSpinner getSpinner() {

		return spinner;
	}

	public void addChangeListener(ChangeListener listener) {

		spinner.addChangeListener(listener);
	}

	public Object getValue() {

		return this.spinner.getValue();
	}

	public void setSpinner(JSpinner spinner) {

		this.spinner = spinner;
	}

	public String getLabelText() {

		return label.getText();
	}

	public void setLabelText(String label) {

		this.label.setText(label);
	}

	public TextSpinner(String text) {

		setLayout(new FlowLayout());

		label.setText(text);
		add(label);

		spinner = new JSpinner();
		add(spinner);
	}

	public TextSpinner(SpinnerModel model, String text) {

		setLayout(new FlowLayout());

		spinner = new JSpinner(model);

		setLabelText(text);
		add(label);
		add(spinner);
	}

	public TextSpinner(JSpinner spinner) {

		this();
		this.spinner = spinner;
	}

	public TextSpinner(JSpinner spinner, String text) {

		setLayout(new FlowLayout());

		this.spinner = spinner;
		setLabelText(text);
		add(label);
		add(spinner);
	}

	public TextSpinner() {

		// TODO Auto-generated constructor stub
		setLayout(new FlowLayout());
		spinner = new JSpinner();

		add(label);
		add(spinner);
	}
}