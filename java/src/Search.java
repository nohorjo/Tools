import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import nohorjo.cli.CLIArgs;
import nohorjo.cli.InvalidCLIArgException;
import nohorjo.out.PaddedPrintStream;

public class Search {
	private static String inFileRegex, startingPath;
	private static List<String> exclude = new ArrayList<>();
	private static boolean verbose, fullPath, symLink, clearFile, recursive;
	private static int outLine;

	public static void main(String[] args) throws IOException {
		System.setOut(new PaddedPrintStream(System.out));
		try {
			CLIArgs cli = new CLIArgs(args);
			File f = new File(cli.getString("path", "."));
			startingPath = f.getCanonicalPath();
			verbose = cli.getBoolean("verbose", false);
			fullPath = cli.getBoolean("full-path", false);
			outLine = (int) cli.getLong("out-line", 0);
			symLink = cli.getBoolean("sym-link", false);
			clearFile = cli.getBoolean("clear", false);
			recursive = cli.getBoolean("recursive", true);

			String regex;
			try {
				regex = cli.getString("$1");
			} catch (InvalidCLIArgException e1) {
				regex = ".*" + Pattern.quote(cli.getString("lit")) + ".*";
			}

			try {
				inFileRegex = ".*" + Pattern.quote(cli.getString("in-file-lit")) + ".*";
			} catch (InvalidCLIArgException e) {
				inFileRegex = cli.getString("in-file", null);
			}

			try {
				exclude = cli.getList("exclude", "/");
			} catch (InvalidCLIArgException e) {
			}
			try {
				for (String lit : cli.getList("exclude-lit", "/")) {
					exclude.add(".*" + Pattern.quote(lit) + ".*");
				}
			} catch (InvalidCLIArgException e) {
			}

			search(regex, f);

		} catch (InvalidCLIArgException e) {
			System.out.println("Usage:\n\tsearch [path=<path>] (<file_name_pattern> | lit=<literal_search>) [OPTIONS]");
			System.out.println("\nOptions:\n");
			System.out.print("in-file-lit\t");
			System.out.println("Literal text to search for in files.");
			System.out.print("in-file\t\t");
			System.out.println("Regex to search for. Ignored if in-file-lit is set.");
			System.out.print("verbose\t\t");
			System.out.println("(true|false) flag to set verbose.");
			System.out.print("exclude-lit\t");
			System.out.println(
					"Slash [/] seperated list of strings such that if a file name includes one then it will be ignored.");
			System.out.print("exclude\t\t");
			System.out.println(
					"Slash [/] seperated list of regular expressions such that if a file name matches one then it will be ignored.");
			System.out.print("full-path\t");
			System.out.println("(true|false) flag to set if files should display full or relative paths.");
			System.out.print("out-line\t");
			System.out.println(
					"(0|1|2) 0 = do not output line, 1 = output line, 2 = output only line (no filename or line numbers)");
			System.out.print("sym-link\t");
			System.out.println("(true|false) flag to set if symbolic linked directories should be followed");
			System.out.print("clear\t\t");
			System.out.println("(true|false) flag to set if the file should be cleared afterwards");
			System.out.print("recursive\t");
			System.out.println("(true|false) flag to set if subdirectories should be scanned recursively");
		} catch (PatternSyntaxException e) {
			System.err.println("Invalid pattern");
		}
	}

	private static void search(String regex, File file) throws IOException {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName();
				for (String regex : exclude) {
					if (name.matches(regex)) {
						return false;
					}
				}
				return pathname.isDirectory() || name.matches(regex);
			}
		};
		File[] files = file.listFiles(filter);
		if (files != null) {
			for (File f : files) {
				String path = f.getCanonicalPath();
				if (!fullPath) {
					path = path.replace(startingPath, "");
				}
				Path nPath = f.toPath();
				if (f.isDirectory()) {
					if (recursive) {
						if (Files.isSymbolicLink(nPath) && !symLink) {
							System.out.println("Skipping symbolic link: " + path);
						} else {
							if (verbose) {
								System.out.print("Searching in " + path + "\r");
							}
							search(regex, f);
						}
					}
				} else if (inFileRegex != null) {
					if (verbose) {
						System.out.print("Searching file: " + path + "\r");
					}
					int ln = 0;
					try {
						for (String line : Files.readAllLines(nPath, Charset.defaultCharset())) {
							ln++;
							if (line.matches(inFileRegex)) {
								System.out.println((outLine == 2 ? "\n" : path + "\tline:" + ln + "\t")
										+ (outLine == 0 ? "" : line));
							}
						}
						if (clearFile) {
							Files.write(nPath, new byte[] {});
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
