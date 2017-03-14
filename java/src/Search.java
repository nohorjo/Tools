import java.io.File;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

public class Search {
	public static void main(String[] args) throws IOException {
		try {
			String path = args[0];
			String regex = args[1];
			File[] files = new File(path).listFiles();

			search(regex, files);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\tsearch path pattern");
		} catch (PatternSyntaxException e) {
			System.err.println("Invalid pattern");
		}
	}

	private static void search(String regex, File[] files) throws IOException {
		if (files != null) {
			for (File f : files) {
				if (f.getName().matches(regex)) {
					System.out.println(f.getCanonicalPath());
				}
				if (f.isDirectory()) {
					search(regex, f.listFiles());
				}
			}
		} else {
			System.err.println("Invalid path");
		}
	}
}
