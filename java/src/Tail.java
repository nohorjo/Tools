import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import nohorjo.file.FileUtils;

public class Tail {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		try {
			File f = new File(args[0]);
			int skip = Integer.MAX_VALUE;
			try {
				skip = Integer.parseInt(args[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			while (true) {
				try (BufferedReader r = new BufferedReader(new FileReader(f))) {
					System.out.println(FileUtils.readLastNLines(f, skip));
					r.skip(f.length());
					long len = f.length();
					while (len <= f.length()) {
						String l = r.readLine();
						if (l != null) {
							System.out.println(l);
							len = f.length();
						}
					}
					skip = Integer.MAX_VALUE;
				}
			}
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Usage:\n\ttail filename [last n lines]");
		}
	}
}
