package nohorjo.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Collection of common file operations
 * 
 * @author muhammed
 *
 */
public class FileUtils {
	/**
	 * Reads the last n number of lines from a file
	 * 
	 * @param file
	 *            the {@link File} to read from
	 * @param n
	 *            the number of lines to return
	 * @return a {@link String} containing the lines
	 * @throws IOException
	 *             on errors reading the file
	 */
	public static String readLastNLines(File file, int n) throws IOException {
		try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
			int lineCount = 0;
			StringBuilder rtnn = new StringBuilder();
			for (long cursor = f.length() - 1; cursor >= 0; cursor--) {
				f.seek(cursor);
				byte b = f.readByte();

				rtnn.insert(0, (char) b);
				if (rtnn.charAt(0) == '\n') {
					if (++lineCount == n + 1) {
						break;
					}
				}
			}
			try {
				return rtnn.substring(1);
			} catch (StringIndexOutOfBoundsException e) {
				return "";
			}
		}
	}
}
