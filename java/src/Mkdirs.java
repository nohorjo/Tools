import java.io.File;

public class Mkdirs {
	public static void main(String[] args) {
		try {
			new File(args[0]).mkdirs();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\tmkdirs directory_structure");
		}
	}
}
