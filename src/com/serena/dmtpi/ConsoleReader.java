package com.serena.dmtpi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;

/**
 * This class can prompt the user for a line of input or prompt the user
 * for a password (it will attempt to mask input with asterisks).
 * The masking can only really work for the console InputStream and
 * OutputStream (System.in and System.out) as it relies on the behaviour
 * of backspace characters moving the cursor position.
 */
final class ConsoleReader {
	private final InputStream in;
	private final OutputStream out;

	ConsoleReader(final InputStream in, final OutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	private static char[] internalReadLine(InputStream in) throws IOException {
		char[] lineBuffer;
		char[] buf;
		buf = lineBuffer = new char[128];
		int room = buf.length;
		int offset = 0;
		int c;
		InputStream in2 = in;
		loop: while (true) {
			switch (c = in2.read()) {
			case -1:
			case '\n':
				break loop;
			case '\r':
				int c2 = in2.read();
				if ((c2 != '\n') && (c2 != -1)) {
					if (!(in2 instanceof PushbackInputStream)) {
						in2 = new PushbackInputStream(in2);
					}
					((PushbackInputStream) in2).unread(c2);
				} else {
					break loop;
				}
			default:
				if (--room < 0) {
					buf = new char[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);
					Arrays.fill(lineBuffer, ' ');
					lineBuffer = buf;
				}
				buf[offset++] = (char) c;
				break;
			}
		}
		char[] ret = new char[offset];
		if (offset > 0) {
			System.arraycopy(buf, 0, ret, 0, offset);
		}
		Arrays.fill(buf, ' ');
		return ret;
	}
	
	String readLine() throws IOException {
		out.flush();
		char[] ret = internalReadLine(in);
		return new String(ret);
	}

	char[] readPassword() throws IOException {
		out.flush();
		ConsoleReader.ConsoleMasker masker = new ConsoleMasker(out);
		Thread thread = new Thread(masker);
		thread.start();
		char[] ret = internalReadLine(in);
		masker.stop();
		return ret;
	}
	/**
	 * This class attempts to erase characters echoed to the given
	 * OutputStream. The OutputStream in question needs to support
	 * the backspace character to reposition the print position
	 * backwards (System.out connected to a Console does, but not
	 * much else, so the utility of this is limited).
	 */
    private static final class ConsoleMasker implements Runnable {
		private volatile boolean stop;
		private final OutputStream out;

		ConsoleMasker(final OutputStream out) {
			this.out = out;
		}

		public void run() {
			stop = false;
			while (!stop) {
				try {
					// backspace then asterisk
					out.write((int) '\010');
					out.write((int) '*');
					out.flush();
				} catch (IOException ioe) {
					// not a lot we can do.
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException iex) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}

		void stop() {
			stop = true;
			try {
				Thread.sleep(50);
			} catch (InterruptedException iex) { 
			}
		}
    }
}