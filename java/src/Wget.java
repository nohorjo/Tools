import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Wget {
	public static void main(String[] args) throws IOException {
		HttpURLConnection.setFollowRedirects(true);
		StringBuilder result = new StringBuilder();
		URL Url;
		try {
			Url = new URL(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\twget url");
			return;
		}
		HttpURLConnection conn = (HttpURLConnection) Url.openConnection();

		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		conn.setRequestMethod("GET");

		try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));) {
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line + "\n");
			}
		}
		String resp = result.toString();
		try (FileOutputStream fos = new FileOutputStream(Url.getPath().replaceAll(".*/", ""))) {
			fos.write(resp.getBytes());
		}
	}
}
