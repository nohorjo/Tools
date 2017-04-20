import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonoHtml {
	private static String fileconts;
	private static Path file;

	public static void main(String[] args) throws IOException {
		file = new File(args[0]).toPath().toAbsolutePath();
		fileconts = new String(Files.readAllBytes(file)).replace('\n', ' ');

		embedImages();
		embedJS();
		embedCSS();

		System.out.println(fileconts);
	}

	private static void embed(String regex, String before, String after, String toEmbed) throws IOException {
		Matcher m = Pattern.compile(regex).matcher(fileconts);
		while (m.find()) {
			String group = m.group();
			Matcher n = Pattern.compile("\"[^\"]*\"").matcher(group);
			String originName = null;
			if (n.find()) {
				originName = n.group().replace("\"", "");
			}
			byte[] origin = Files.readAllBytes(file.getParent().resolve(originName));
			String replace = null;
			switch (toEmbed) {
			case "images":
				replace = "data:image/png;base64, " + Base64.getEncoder().encodeToString(origin);
				break;
			case "css":
			case "js":
				replace = new String(origin);
				break;

			}
			fileconts = fileconts.replace(group, before + replace + after);
		}
	}

	private static void embedImages() throws IOException {
		embed("<img\\s*src=\"[^\"]*\"", "<img src=\"", "\"", "images");
	}

	private static void embedJS() throws IOException {
		embed("<script\\s*src=\".*\".*t>", "<script>", "</script>", "js");
	}

	private static void embedCSS() throws IOException {
		embed("<link\\s*href=\".*\".*k>", "<style>", "</style>", "css");
	}

}
