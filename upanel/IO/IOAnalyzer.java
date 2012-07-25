package upanel.IO;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import upanel.gui.LogMonitor;

interface FieldNum {

	byte command = 0;
	byte actionItem = 1;
	byte next = 2;
}


public class IOAnalyzer implements FieldNum, SerialPortEventListener {

	protected String fileName = "logfile.txt";
	protected Set<String[]> patternSet = Collections.synchronizedSet(new HashSet<String[]>());
	protected WiredDevice port;
	// protected Queue<String> userInput = (Queue<String>)
	// Collections.synchronizedList(new LinkedList<String>());// add to analyze

	protected final Next nextAction = new Next();

	protected boolean endLine = false;
	protected static IOAnalyzer instance;

	public static IOAnalyzer getInstance() {

		if (instance == null)

			instance = new IOAnalyzer();

		return instance;
	}

	public String getLogFileName() {

		return fileName;
	}

	public void clearPattern() {

		patternSet.clear();
	}

	public void setWiredDevice(WiredDevice io) {

		port = io;
		port.setEventListener(this);
	}

	public IODevice getWiredDevice() {

		return port;
	}

	public void setLogFile(final String name) {

		fileName = name;
		nextAction.setFileName(fileName);
	}

	public void setPattern(Set set) {

		patternSet = set;
	}

	public void writeMessage(final String message) {

		if (port != null) {
			port.writeMessage(message);
		}
		// userInput.add(message);
	}

	public BufferedInputStream readMessage() {

		return null;
	}

	public void setEndLineSigh(boolean enable) {

		endLine = enable;
	}

	private final List<String> dataList = new CopyOnWriteArrayList<String>();

	private BufferedInputStream input;
	private InputStreamReader isR;
	private BufferedReader bR;

	protected void analyzeIn() {

		input = port.getStreamToRead();
		isR = new InputStreamReader(input);
		bR = new BufferedReader(isR);

		final Iterator<String[]> iterPattern = patternSet.iterator();// pattern

		try {
			final String s = bR.readLine();

			if (!s.isEmpty())
				dataList.add(s);

			if (dataList.isEmpty())
				return;
			// System.out.println("analyze In Called "+dataList.size());

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Iterator<String> dataIter = dataList.iterator();

		while (dataIter.hasNext()) {

			final String data = dataIter.next();

			LogMonitor.getInstance().writeToConsole(data + "\n");

			while (iterPattern.hasNext()) {
				// System.out.println("iter");
				final String tab[] = iterPattern.next();

				final Pattern pat = Pattern.compile(tab[command]);
				final Matcher match = pat.matcher(data);

				// ("Matches "+match.matches());
				if (match.matches()) {

					// ("Patttern Exist");

					final Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());

					switch (tab[actionItem]) {
					// "Add Time", "Add Date", "Date Time","Delete"};
						case "Date Time":

							final StringBuilder buildDateTime = new StringBuilder(calendar.getTime().toString());
							buildDateTime.append(" ");
							buildDateTime.append(data);

							makeAction(buildDateTime.toString(), tab[next]);
							break;

						case "Add Time":

							final StringBuilder buildTime = new StringBuilder(new Integer(calendar.get(Calendar.HOUR_OF_DAY)).toString());

							buildTime.append("-");
							buildTime.append(calendar.get(Calendar.MINUTE));
							buildTime.append("-");
							buildTime.append(calendar.get(Calendar.SECOND));
							buildTime.append(" ");
							buildTime.append(data);

							makeAction(buildTime.toString(), tab[next]);

							break;

						case "Add Date":

							final StringBuilder buildDate = new StringBuilder(new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString());

							buildDate.append("-");
							buildDate.append(calendar.get(Calendar.MONTH));
							buildDate.append("-");
							buildDate.append(calendar.get(Calendar.YEAR));
							buildDate.append(" ");
							buildDate.append(data);

							makeAction(buildDate.toString(), tab[next]);
							break;

						case "Empty":

							if (!tab[next].isEmpty())
								makeAction(data, tab[next]);
							break;

						case "Delete":

							break;
					}

				}
			}
		}
		dataList.clear();
	}

	private void makeAction(final String data, final String actionToken) {

		// "Only Console","Log To File",
		switch (actionToken) {

			case "Log To File":
				nextAction.writeToFile(data, endLine);
				break;
			case "Stdout \\n":
				System.out.println(data);
				break;
			case "Stdout":
				System.out.print(data);
				break;
			case "Log CSV":
				nextAction.writeLogAsCsv(data);
				break;
			case "Only Console":

		}

	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {

		if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			new Thread(new Runnable() {

				@Override
				public void run() {

					// TODO Auto-generated method stub
					analyzeIn();

				}
			}).start();
		}
	}

	public void stop() {

		if (input != null && isR != null && bR != null) {
			try {
				input.close();
				isR.close();
				bR.close();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (port != null)
			port.stop();

		dataList.clear();

	}

	private IOAnalyzer() {

	}

}


// /////

class Next {

	private String fileName = "logfile.txt";
	private final Calendar calendar = Calendar.getInstance();

	public String getFileName() {

		return fileName;
	}

	public void setFileName(String fName) {

		this.fileName = fName;
	}

	public void writeLogAsCsv(final String msg) {

		try (FileWriter fW = new FileWriter(fileName, true)) {
			// final BufferedWriter buffWriter = new BufferedWriter(fW);
			final StringBuilder builder = new StringBuilder(msg);
			builder.append(",");

			fW.write(builder.toString());

		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeToFile(final String msg, boolean endLine) {

		try (FileWriter fW = new FileWriter(fileName, true)) {
			// final BufferedWriter buffWriter = new BufferedWriter(fW);

			if (endLine)
				fW.write(new StringBuilder(msg).append("\n").toString());
			else
				fW.write(msg);

		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String addTime(String msg) {

		final StringBuilder buildString = new StringBuilder(msg);
		buildString.append(calendar.getTime().toString());

		return buildString.toString();
	}

}