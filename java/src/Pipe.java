import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Pipe {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Usage:\n\tpipe commands...");
			System.out.println("\nIn command args $out$ will be replaced by the std output of the last command");
			return;
		}
		String pathext[] = (";" + System.getenv("PATHEXT")).split(";");

		Runtime r = Runtime.getRuntime();
		String outOfLast = "";
		for (String command : args) {
			Process p = null;
			for (int i = 0; i < pathext.length; i++) {
				String ext = pathext[i];
				try {
					p = r.exec(command.replaceFirst(" ", ext + " ").replace("$out$", outOfLast));
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
	}
}
