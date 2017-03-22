import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.PatternSyntaxException;

import nohorjo.cli.CLIArgs;

public class Search {
	private static CLIArgs cli;
	private static String inFileRegex;

	public static void main(String[] args) throws IOException {
		try {
			cli = new CLIArgs(args);
			String path = cli.getString("path", ".");
			String regex = cli.getString("$1");

			try {
				inFileRegex = cli.getString("in-file");
			} catch (NullPointerException e) {
			}

			search(regex, new File(path));

		} catch (NullPointerException e) {
			System.out.println("Usage:\n\tsearch [path=<path>] <file_name_pattern> [in-file=<search_pattern>]");
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
				String path = f.getCanonicalPath();
				if (f.isDirectory()) {
					search(regex, f);
				} else if (inFileRegex != null) {
					String contents = new String(Files.readAllBytes(f.toPath())).replaceAll("\\s", " ");
					if (contents.matches(inFileRegex)) {
						System.out.println("\r" + path);
					}
				} else {
					System.out.println(path);
				}
			}
		} else {
			System.err.println("Invalid path: " + file.getCanonicalPath());
		}
	}
}
