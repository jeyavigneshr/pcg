package mario.communication;

import static utility.Settings.printErrorMsg;
import static utility.Settings.printInfoMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public abstract class Comm extends Thread {

	public static final int THRESHOLD = 60000; // milliseconds

	protected boolean end;

	protected String threadName = "thread";
	protected BufferedReader reader;
	protected PrintStream writer;
	protected Process process;

	public Comm() {
		super();
		this.end = false;
	}

	public abstract void initBuffers();

	public void commSend(String msg) throws IOException {
		printInfoMsg("[" + this.threadName + "] Comm:commSend will send " + msg + " to GAN");
		writer.println(msg);
		writer.flush();
	}

	public String commRecv() {
		String msg = processCommRecv();
		return msg;
	}

	private String processCommRecv() {
		String msg = null;
		try {
			msg = reader.readLine();
			System.out.println("processCommRecv:" + msg);
			if (msg != null) {
				return msg;
			} else {
				printErrorMsg("processCommRecv: Null message.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		printErrorMsg("processCommRecv: exception.");
		return null;
	}
}
