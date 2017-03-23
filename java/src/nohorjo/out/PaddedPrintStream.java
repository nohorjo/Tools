package nohorjo.out;

import java.io.OutputStream;
import java.io.PrintStream;

public class PaddedPrintStream extends PrintStream {

	private int len;

	public PaddedPrintStream(OutputStream out) {
		super(out);
	}

	@Override
	public void print(String x) {
		boolean endsWithR = x.endsWith("\r");
		StringBuilder sb = new StringBuilder(x);
		for (int i = sb.length(); i <= len; i++) {
			if (endsWithR) {
				sb.insert(i-1, " ");
			} else {
				sb.append(" ");
			}
		}
		super.print(sb.toString());
		len = x.length();
	}

	@Override
	public void println(String x) {
		boolean endsWithR = x.endsWith("\r");
		StringBuilder sb = new StringBuilder(x);
		for (int i = sb.length(); i <= len; i++) {
			if (endsWithR) {
				sb.insert(i-1, " ");
			} else {
				sb.append(" ");
			}
		}
		super.println(sb.toString());
		len = x.length();
	}

	public static void main(String[] args) {
		System.setOut(new PaddedPrintStream(System.out));
		System.out.print("hello\r");
		System.out.print("yo\r");
	}
}
