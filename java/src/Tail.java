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
			try (BufferedReader r = new BufferedReader(new FileReader(f))) {
				try {
					System.out.println(FileUtils.readLastNLines(f, Integer.parseInt(args[1])));
					r.skip(f.length());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				while (true) {
					String l = r.readLine();
					if (l != null) {
						System.out.println(l);
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Usage:\n\ttail filename [last n lines]");
		}
	}
}
