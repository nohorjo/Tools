import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonoHtml {
	private static Path file;

	public static void main(String[] args) throws IOException {
		file = new File(args[0]).toPath().toAbsolutePath();
		
		System.out.println(embedImages(embedJS(embedCSS(new String(Files.readAllBytes(file))))));
	}

	private static String embed(String input, String regex, String before, String after, String toEmbed) throws IOException {
		Matcher m = Pattern.compile(regex).matcher(input);
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
				origin = embed(new String(origin),"url\\(\"[^\"]*\"","url(\"","\"","images").getBytes();
			case "js":
				replace = new String(origin);
				break;

			}
			input = input.replace(group, before + replace + after);
		}
		return input;
	}

	private static String embedImages(String input) throws IOException {
		return embed(input,"<img\\s*src=\"[^\"]*\"", "<img src=\"", "\"", "images");
	}

	private static String embedJS(String input) throws IOException {
		return embed(input,"<script\\s*src=\"[^<]*\".*</script>", "<script>", "</script>", "js");
	}

	private static String embedCSS(String input) throws IOException {
		return embed(input,"<link\\s*href=\"[^<]*\".*</link>", "<style>", "</style>", "css");
	}

}
