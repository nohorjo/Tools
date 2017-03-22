import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.util.regex.PatternSyntaxException;

import nohorjo.cli.CLIArgs;

public class Search {
	private static String inFileRegex;
	private static String startingPath;

	public static void main(String[] args) throws IOException {
		try {
			CLIArgs cli = new CLIArgs(args);
			File f = new File(cli.getString("path", "."));
			startingPath = f.getCanonicalPath();
			String regex = cli.getString("$1");

			try {
				inFileRegex = cli.getString("in-file");
			} catch (NullPointerException e) {
			}

			search(regex, f);

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
				String path = f.getCanonicalPath().replace(startingPath, "");
				if (f.isDirectory()) {
					search(regex, f);
				} else if (inFileRegex != null) {
					int ln = 0;
					try {
						for (String line : Files.readAllLines(f.toPath(), Charset.defaultCharset())) {
							ln++;
							if (line.matches(inFileRegex)) {
								System.out.println(path + "\tline:" + ln);
							}
						}
					} catch (UnmappableCharacterException e) {
						// skip binary file
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
