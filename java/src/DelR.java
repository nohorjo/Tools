import java.io.File;

import nohorjo.cli.CLIArgs;
import nohorjo.cli.InvalidCLIArgException;

public class DelR {
	public static void main(String[] args) {
		CLIArgs cli = new CLIArgs(args);
		File d = new File(".");
		try {
			deleteRecursively(d, cli.getString("$1"));
		} catch (InvalidCLIArgException e) {
			e.getMessage();
			System.out.println("Usage:\n\tdelr filename");
		}
	}

	private static void deleteRecursively(File d, String name) {
		File[] fs = d.listFiles();
		if (fs != null) {
			for (File f : fs) {
				if (f.isFile()) {
					if (name.equals(f.getName())) {
						if (!f.delete()) {
							System.err.println("Could not delete: " + f.getAbsolutePath());
						}
					}
				} else if (f.isDirectory()) {
					deleteRecursively(f, name);
				}
			}
		}

	}
}
