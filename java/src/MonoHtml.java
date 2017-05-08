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

		System.out.println(compile(new String(Files.readAllBytes(file))));
	}

	private static String compile(String source) throws IOException {
		return embedIframes(embedImages(embedJS(embedCSS(source))));
	}

	private static String embed(String input, String regex, String before, String after, String toEmbed)
			throws IOException {
		Matcher m = Pattern.compile(regex).matcher(input);

		while (m.find()) {
			String group = m.group();
			Matcher n = Pattern.compile("(href|src)=\"[^\"]*\"").matcher(group);
			String originName = null;
			if (n.find()) {
				originName = n.group().replace("\"", "").replaceAll("(href|src)=", "");
				byte[] origin = Files.readAllBytes(file.getParent().resolve(originName));
				String replace = null;
				switch (toEmbed) {
				case "iframes":
					before = group.replaceAll("src=\"[^\"]*\"", "");
					replace = "";
					Matcher o = Pattern.compile("id=\"[^\"]*\"").matcher(input);
					if (o.find()) {
						after = after.replace("##IFRAME_ID##", o.group().replaceAll("^id=", "").replace("\"", ""));
						after = after.replace("##IFRAME_CONTENT##",
								new String(origin).replace("'", "\'").replace("\n", "\\\n"));
					} else {
						throw new Error("Iframe has no id!");
					}
					break;
				case "images":
					replace = "data:image/png;base64, " + Base64.getEncoder().encodeToString(origin);
					break;
				case "css":
					origin = embed(new String(origin).replace("url(\"","url(src=\""), "url\\(src=\"[^\"]*\"", "url(\"", "\"", "images").getBytes();
				case "js":
					replace = new String(origin);
					break;
				}
				input = input.replace(group, before + replace + after);
			}
		}
		return input;
	}

	private static String embedImages(String input) throws IOException {
		return embed(input, "<img\\s*src=\"[^\"]*\"", "<img src=\"", "\"", "images");
	}

	private static String embedJS(String input) throws IOException {
		return embed(input, "<script\\s*src=\"[^<]*\".*</script>", "<script>", "</script>", "js");
	}

	private static String embedCSS(String input) throws IOException {
		return embed(input, "<link\\s*href=\"[^<]*\".*</link>", "<style>", "</style>", "css");
	}

	private static String embedIframes(String input) throws IOException {
		return embed(input, "<iframe[^<]*</iframe>", null,
				"<script>\nvar ifrm = document.getElementById('##IFRAME_ID##');"
						+ "ifrm = ifrm.contentWindow || ifrm.contentDocument.document || ifrm.contentDocument;"
						+ "ifrm.document.open();ifrm.document.write('\\\n##IFRAME_CONTENT##\\n');ifrm.document.close();</script>",
				"iframes");
	}

}
