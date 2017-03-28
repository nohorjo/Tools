import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import nohorjo.cli.CLIArgs;
import nohorjo.cli.InvalidCLIArgException;

public class Wget {
	public static void main(String[] args) throws IOException {
		HttpURLConnection.setFollowRedirects(true);
		StringBuilder result = new StringBuilder();
		CLIArgs cli = new CLIArgs(args);
		URL Url;
		try {
			Url = new URL(cli.getString("$1"));
			HttpURLConnection conn = (HttpURLConnection) Url.openConnection();

			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			conn.setRequestMethod(cli.getString("method", "GET"));
			try {
				for (String header : cli.getList("headers", ",")) {
					String[] h = header.split(":");
					conn.setRequestProperty(h[0], h[1]);
				}
			} catch (InvalidCLIArgException e) {
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new InvalidCLIArgException("Invalid headers");
			}
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				try {
					wr.write(cli.getString("data").getBytes());
				} catch (InvalidCLIArgException e) {
					wr.write(Files.readAllBytes(Paths.get(cli.getString("file"))));
				}
			}
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));) {
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line + "\n");
				}
			}
			String resp = result.toString();
			if (cli.getBoolean("console", false)) {
				System.out.println(resp);
				return;
			}
			try (FileOutputStream fos = new FileOutputStream(Url.getPath().replaceAll(".*/", ""))) {
				fos.write(resp.getBytes());
			}
		} catch (InvalidCLIArgException e) {
			e.printMessage();
			System.out.println("Usage:\n\twget url [Options...]");
			System.out.println("Options:");
			System.out.print("console\t");
			System.out.println("(true|false) flag to set if output should be to console.");
			System.out.print("headers\t");
			System.out.println("Comma seperated list of headers, where header and value is split by colon.");
			System.out.print("method\t");
			System.out.println("HTTP method to use, defaults to GET.");
			System.out.print("data\t");
			System.out.println("data to send over.");
			System.out.print("file\t");
			System.out.println("file to send over.");
		}
	}
}
