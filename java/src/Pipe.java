import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import nohorjo.cli.CLIArgs;
import nohorjo.cli.InvalidCLIArgException;

public class Pipe {
	public static void main(String[] args) throws IOException {
		CLIArgs cli = new CLIArgs(args);
		boolean verbose = cli.getBoolean("verbose", false);
		try {
			List<String> commands = cli.getList("commands", "|");

			String pathext[] = (";" + System.getenv("PATHEXT")).split(";");

			Runtime r = Runtime.getRuntime();
			String outOfLast = "";
			for (String command : commands) {
				Process p = null;
				for (int i = 0; i < pathext.length; i++) {
					String ext = pathext[i];
					String commandToExecute = command.trim().replaceFirst(" ", ext + " ").replace("$out$", outOfLast);
					try {
						p = r.exec(commandToExecute);
						if (verbose) {
							System.out.println("Executed:\n" + commandToExecute);
						}
						break;
					} catch (IOException e) {
						if (!e.getMessage().contains("CreateProcess error=2") || i + 1 == pathext.length) {
							System.err.println("Cannot find program: " + command.split(" ")[0]);
							return;
						}
					}
				}
				outOfLast = "";
				try (InputStreamReader out = new InputStreamReader(p.getInputStream());
						BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {

					for (String line; (line = err.readLine()) != null;) {
						System.out.println(line);
					}
					for (int d; (d = out.read()) != -1;) {
						outOfLast += (char) d;
					}
				}
			}
			System.out.println(outOfLast);
		} catch (InvalidCLIArgException e2) {
			System.out.println("Usage:\n\tpipe commands=<list of commands seperated by | > [options...]");
			System.out.println("\nIn command args $out$ will be replaced by the std output of the last command");
			System.out.println("\nOptions:");
			System.out.print("verbose\t");
			System.out.println("(true|false) flag to set verbose output");
			return;
		}
	}
}
