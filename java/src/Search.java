import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

public class Search {
	public static void main(String[] args) throws IOException {
		try {
			String path = args[0];
			String regex = args[1];

			search(regex, new File(path));

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\tsearch path pattern");
		} catch (PatternSyntaxException e) {
			System.err.println("Invalid pattern");
		}
	}

	private static void search(String regex, File file) throws IOException {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().matches(regex);
			}
		};
		File[] files = file.listFiles(filter);
		if (files != null) {
			for (File f : files) {

				if (f.isDirectory()) {
					search(regex, f);
				} else {
					System.out.println(f.getCanonicalPath());
				}
			}
		} else {
			System.err.println("Invalid path: " + file.getCanonicalPath());
		}
	}
}
