package upanel.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

import upanel.IO.IOAnalyzer;

public class LogMonitor extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static LogMonitor instance = new LogMonitor();

	private final ConsolPanel panel = ConsolPanel.getInstance();
	private Document doc;

	public void writeToConsole(final String msg) {

		doc = panel.textAreaLog.getDocument();
		try {
			doc.insertString(doc.getLength(), msg, null);
		} catch (final BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static LogMonitor getInstance() {

		return instance;
	}

	private void addElements() {

		// TODO Auto-generated method stub
		add(panel);

	}

	private LogMonitor() {

		super("Console");
		// TODO Auto-generated constructor stub
		addElements();
		pack();
	}

}


// /
class ConsolPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTextField fieldSend = new JTextField(35);
	protected JTextArea textAreaLog = new JTextArea(10, 40);
	// protected boolean autoscroll = true;

	private static ConsolPanel instance = new ConsolPanel();

	public static ConsolPanel getInstance() {

		return instance;
	}

	public void writeMessage(final String msg) {

		textAreaLog.append(msg);
		// setAutoscrollField(this.autoscroll);
	}

	public void setAutoscrollField(boolean autoscroll) {

		final DefaultCaret caret = (DefaultCaret) textAreaLog.getCaret();

		if (autoscroll) {
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		} else {
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		}
		repaint();
	}

	public JTextArea getLogArea() {

		return textAreaLog;
	}

	protected void addElements() {

		final JScrollPane scroll = new JScrollPane(textAreaLog);
		textAreaLog.setLineWrap(true);
		textAreaLog.setEditable(false);

		scroll.setAutoscrolls(true);
		fieldSend.setAutoscrolls(true);
		scroll.setWheelScrollingEnabled(true);

		setAutoscrollField(true);

		final JPanel sendPanel = new JPanel(new FlowLayout());
		final JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(this);

		fieldSend.addActionListener(this);

		sendPanel.add(fieldSend);
		sendPanel.add(sendBtn);

		add(sendPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(OptionsConsole.getInstance(textAreaLog, scroll), BorderLayout.SOUTH);

	}

	private ConsolPanel() {

		// TODO Auto-generated constructor stub

		setLayout(new BorderLayout());
		addElements();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		final String msg = fieldSend.getText();

		IOAnalyzer.getInstance().writeMessage(msg);

	}

}


// //
class OptionsConsole extends JToolBar implements ActionListener/*
																 * ,
																 * ItemListener
																 */{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static OptionsConsole instance;

	// protected JTextField maskField = new JTextField(15);
	// protected JCheckBox dontScroll = new JCheckBox("Scroll");
	protected JButton clearBtn = new JButton("Clear");

	protected JTextArea consoleArea;
	protected Document docData;
	protected static JScrollPane scrollPane;

	public static OptionsConsole getInstance(final JTextArea area, JScrollPane scroll) {

		scrollPane = scroll;
		instance = new OptionsConsole(area);

		return instance;
	}

	private void addElements() {

		setOrientation(SwingConstants.HORIZONTAL);
		setFloatable(false);

		// dontScroll.addItemListener(this);

		clearBtn.addActionListener(this);
		clearBtn.setName("Clear");

		// add(new JLabel("Mask"));
		// maskField.getDocument().addDocumentListener(new DocumentAdapter());

		// add(maskField);
		addSeparator(new Dimension(25, 10));

		// add(dontScroll);
		add(clearBtn);
	}

	private OptionsConsole(JTextArea area) {

		setLayout(new FlowLayout());
		this.consoleArea = area;
		docData = consoleArea.getDocument();
		addElements();
	}

	private void clearJTextArea() {

		try {
			docData.remove(0, docData.getLength());
		} catch (final BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub

		final Component comp = (Component) e.getSource();
		final String s = comp.getName();

		switch (s) {

			case "Clear":
				clearJTextArea();
				break;
		}

	}
}
/*
 * @Override public void itemStateChanged(ItemEvent e) { // TODO Auto-generated
 * method stub if(e.getStateChange() != ItemEvent.SELECTED){
 * 
 * }else{ ("Auto scroll TRUE");
 * ConsolPanel.getInstance().setAutoscrollField(true); } }
 */
// //////
/*
 * class DocumentAdapter implements DocumentListener {
 * 
 * @Override public void changedUpdate(DocumentEvent arg0) { // TODO
 * Auto-generated method stub Document doc = arg0.getDocument();
 * 
 * try { if(consoleArea.getText().matches(doc.getText(0, doc.getLength())))
 * System.out.println("SPE??NIA");
 * 
 * } catch (BadLocationException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } }
 * 
 * @Override public void insertUpdate(DocumentEvent arg0) {
 * 
 * // TODO Auto-generated method stub
 * 
 * }
 * 
 * @Override public void removeUpdate(DocumentEvent arg0) {
 * 
 * // TODO Auto-generated method stub
 * 
 * }
 * 
 * 
 * } }
 */