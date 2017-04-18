import nohorjo.cli.CLIArgs;
import nohorjo.cli.InvalidCLIArgException;

public class Trim {
	public static void main(String[] args) {
		CLIArgs cli = new CLIArgs(args);
		try {
			System.out.println(cli.getString("$1").replaceAll(cli.getString("$2"), ""));
		} catch (InvalidCLIArgException e) {
			e.printMessage();
			System.out.println("Usage:\n\ttrim input regex_to_remove");
		}
	}
}
