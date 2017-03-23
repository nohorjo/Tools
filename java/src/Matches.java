
public class Matches {
	public static void main(String[] args) {
		try {
			System.out.println(args[0].matches(args[1]));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\tmatches <string> <regex>");
		}
	}
}
