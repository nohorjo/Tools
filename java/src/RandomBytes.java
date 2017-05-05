import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class RandomBytes {
	public static void main(String[] args) throws IOException {
		try {
			double length = Double.parseDouble(args[1]);
			try {
				switch (args[2]) {
				case "G":
					length *= 0x400;
				case "M":
					length *= 02000;
				case "K":
					length *= 0b10000000000;
				default:
					break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			byte[] bytes = new byte[(int) length];
			new SecureRandom().nextBytes(bytes);
			try (FileOutputStream fos = new FileOutputStream(args[0])) {
				fos.write(bytes);
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\trandombytes filename size [K|M|G]");
		}
	}
}
