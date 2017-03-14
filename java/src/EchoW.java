import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EchoW {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		boolean append = false;
		try {
			append = Boolean.parseBoolean(args[2]);
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try (FileOutputStream fos = new FileOutputStream(args[0], append)) {
			fos.write((args[1] + "\n").getBytes());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\techow file text [append (default false)]");
		}
	}
}
