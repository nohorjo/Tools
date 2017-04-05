import java.io.File;

import nohorjo.cli.CLIArgs;

public class DU {
	public static void main(String[] args) {
		CLIArgs cli = new CLIArgs(args);
		File[] fs = new File(cli.getString("$1", ".")).listFiles();
		if (fs != null) {
			for (File f : fs) {
				System.out.println(humanReadableSize(getLength(f)) + "\t" + f.getName());
			}
		}
	}

	private static long getLength(File f) {
		long length = 0;
		if (f.isFile()) {
			length = f.length();
		} else if (f.isDirectory()) {
			File[] fs = f.listFiles();
			if (fs != null) {
				for (File f2 : fs) {
					length += getLength(f2);
				}
			}
		}
		return length;
	}

	private static String humanReadableSize(long size) {
		if (size > 0x40000000) {
			return Math.round((float) size / 0x40000000 * 100.0) / 100.0 + "GB";
		} else if (size > 0x100000) {
			return Math.round((float) size / 0x100000 * 100.0) / 100.0 + "MB";
		} else if (size > 0x400) {
			return Math.round((float) size / 0x400 * 100.0) / 100.0 + "KB";
		}
		return size + "B";
	}

}
