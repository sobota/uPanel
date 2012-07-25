package upanel.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;

import upanel.IO.IOAnalyzer;
import upanel.menu.SliderMainPanel;

public class SliderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IOAnalyzer analyze = IOAnalyzer.getInstance();
	private final Preferences prefs = Preferences.userNodeForPackage(SliderMainPanel.class).node("ButtonMainPanel");
	private final JSlider sliderUp = new JSlider(JSlider.HORIZONTAL);
	private final JSlider sliderDown = new JSlider(JSlider.HORIZONTAL);

	private static SliderPanel instance = new SliderPanel();

	public static SliderPanel getInstance() {

		return instance;
	}

	protected void addElements() {

		sliderUp.setName(prefs.get("Slider 1Token", "sl1"));
		sliderDown.setName(prefs.get("Slider 2Token", "sl2"));

		// ////
		class MySliderListener extends MouseAdapter {

			@Override
			public void mouseReleased(MouseEvent e) {

				// TODO Auto-generated method stub
				final JSlider slider = (JSlider) e.getSource();

				analyze.writeMessage(slider.getName() + slider.getValue());

			}

		}

		// panelLabel.add(new JLabel("Move Bar"));

		sliderUp.addMouseListener(new MySliderListener());
		sliderDown.addMouseListener(new MySliderListener());

		sliderUp.setPaintLabels(true);
		sliderDown.setPaintLabels(true);

		sliderUp.setMajorTickSpacing(5);
		sliderDown.setMajorTickSpacing(5);

		sliderUp.setMinorTickSpacing(5);
		sliderDown.setMinorTickSpacing(5);

		sliderUp.setPaintLabels(true);
		sliderDown.setPaintLabels(true);

		sliderUp.setMaximum(prefs.getInt("Slider 1Max", 100));
		sliderDown.setMaximum(prefs.getInt("Slider 2Max", 100));

		sliderUp.setMinimum(prefs.getInt("Slider 1Min", 0));
		sliderDown.setMinimum(prefs.getInt("Slider 2Min", 0));

		add(sliderUp);
		add(sliderDown);

	}

	public void reloadData(int upMax, int upMin, String slider, String token) {

		if (slider.equals("Slider 1")) {
			sliderUp.setMaximum(upMax);
			sliderUp.setMinimum(upMin);
			sliderUp.setName(token);
		} else {
			sliderDown.setMaximum(upMax);
			sliderDown.setMinimum(upMin);
			sliderDown.setName(token);
		}
		repaint();
	}

	private SliderPanel() {

		// TODO Auto-generated constructor stub

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		addElements();

	}

}