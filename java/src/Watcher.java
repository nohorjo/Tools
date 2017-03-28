import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import nohorjo.cli.CLIArgs;
import nohorjo.cli.InvalidCLIArgException;

public class Watcher {
	public static void main(String[] args) {
		CLIArgs cli = new CLIArgs(args);
		try {
			File f = new File(cli.getString("$1"));
			long lm = f.lastModified();
			while (true) {
				if (lm != f.lastModified()) {
					String command = cli.getString("$2");
					try {
						String line;
						Process p = Runtime.getRuntime().exec(command);
						try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
								BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
							while ((line = input.readLine()) != null) {
								System.out.println(line);
							}
							while ((line = err.readLine()) != null) {
								System.out.println(line);
							}
						}
					} catch (Exception err) {
						err.printStackTrace();
					}
					lm = f.lastModified();
				}
			}
		} catch (InvalidCLIArgException e) {
			e.printMessage();
			System.out.println("Usage:\n\twatcher file command");
		}
	}
}
