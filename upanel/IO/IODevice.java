package upanel.IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public interface IODevice {

	void start();

	void stop();

	void close();

	BufferedInputStream getStreamToRead();

	BufferedOutputStream getStreamToWrite();

	boolean isWorking();

	boolean isConfigure();

	void writeMessage(final String message);

}