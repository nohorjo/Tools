import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class SmartCopy {

	public static void main(String[] args) throws IOException {
		try {
			String src = args[0];
			String dest = args[1];

			doCopy(src, dest);

			try {
				if (Boolean.parseBoolean(args[2])) {
					while (true) {
						Thread.sleep(1000);
						doCopy(src, dest);
					}
				}
			} catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\tsync src_dir dest_dir [keep_scanning_for_changes]");
		}
	}

	protected static void doCopy(String src, String dest) throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("Scanning...");
		Map<File, File> files = getFilesRecursively(src, dest);
		System.out.printf("\nFound %d file(s)\n", files.size());
		int i = 0;
		for (File srcFile : files.keySet()) {
			if (!srcFile.exists()) {
				System.err.println("FILE DOES NOT EXIST:" + src);
				continue;
			}
			File destFile = files.get(srcFile);
			destFile.getParentFile().mkdirs();
			Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Copied: " + srcFile.getAbsolutePath());
			System.out.printf("%d%%\r", (++i * 100) / files.size());
		}
		System.out.println("\n --- Completed in " + (System.currentTimeMillis() - start) + " millis\n");
	}

	private static Map<File, File> getFilesRecursively(String src, String dest) throws IOException {
		Map<File, File> files = new HashMap<>();
		File srcFile = new File(src);
		File destFile = new File(dest);
		if (!srcFile.exists()) {
			System.err.println("FILE DOES NOT EXIST:" + src);
		}
		if (srcFile.isDirectory()) {
			destFile.mkdirs();
			for (String sub : srcFile.list()) {
				files.putAll(getFilesRecursively(src + File.separator + sub, dest + File.separator + sub));
			}
		} else {
			if (!destFile.exists() || destFile.lastModified() < srcFile.lastModified()) {
				files.put(srcFile, destFile);
				System.out.printf("Found file: %s\r", srcFile.getAbsolutePath());
			}
		}
		return files;
	}
}
